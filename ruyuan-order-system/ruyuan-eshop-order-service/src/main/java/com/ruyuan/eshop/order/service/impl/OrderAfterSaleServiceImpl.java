package com.ruyuan.eshop.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ruyuan.eshop.common.constants.RedisLockKeyConstants;
import com.ruyuan.eshop.common.constants.RocketMqConstant;
import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.common.enums.*;
import com.ruyuan.eshop.common.exception.BaseBizException;
import com.ruyuan.eshop.common.message.ActualRefundMessage;
import com.ruyuan.eshop.common.redis.RedisLock;
import com.ruyuan.eshop.common.utils.ParamCheckUtil;
import com.ruyuan.eshop.common.utils.RandomUtil;
import com.ruyuan.eshop.customer.domain.request.CustomerReceiveAfterSaleRequest;
import com.ruyuan.eshop.market.domain.request.ReleaseUserCouponRequest;
import com.ruyuan.eshop.order.converter.AfterSaleConverter;
import com.ruyuan.eshop.order.converter.OrderConverter;
import com.ruyuan.eshop.order.dao.*;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderItemDTO;
import com.ruyuan.eshop.order.domain.dto.CancelOrderRefundAmountDTO;
import com.ruyuan.eshop.order.domain.dto.OrderInfoDTO;
import com.ruyuan.eshop.order.domain.dto.OrderItemDTO;
import com.ruyuan.eshop.order.domain.entity.*;
import com.ruyuan.eshop.order.domain.request.*;
import com.ruyuan.eshop.order.enums.*;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
import com.ruyuan.eshop.order.manager.OrderNoManager;
import com.ruyuan.eshop.order.mq.producer.DefaultProducer;
import com.ruyuan.eshop.order.remote.PayRemote;
import com.ruyuan.eshop.order.manager.AfterSaleManager;
import com.ruyuan.eshop.order.service.OrderAfterSaleService;
import com.ruyuan.eshop.pay.domain.request.PayRefundRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
@Slf4j
public class OrderAfterSaleServiceImpl implements OrderAfterSaleService {

    @Autowired
    private OrderPaymentDetailDAO orderPaymentDetailDAO;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private PayRemote payRemote;

    @Autowired
    private OrderInfoDAO orderInfoDAO;

    @Autowired
    private OrderNoManager orderNoManager;

    @Autowired
    private AfterSaleInfoDAO afterSaleInfoDAO;

    @Autowired
    private OrderItemDAO orderItemDAO;

    @Autowired
    private AfterSaleLogDAO afterSaleLogDAO;

    @Autowired
    private AfterSaleRefundDAO afterSaleRefundDAO;

    @Autowired
    private DefaultProducer defaultProducer;

    @Autowired
    private AfterSaleItemDAO afterSaleItemDAO;

    @Autowired
    private OrderAmountDAO orderAmountDAO;

    @Autowired
    private AfterSaleOperateLogFactory afterSaleOperateLogFactory;

    @Autowired
    private AfterSaleManager afterSaleManager;

    @Autowired
    private OrderConverter orderConverter;

    @Autowired
    private AfterSaleConverter afterSaleConverter;

    /**
     * 取消订单/超时未支付取消
     */
    @Override
    public JsonResult<Boolean> cancelOrder(CancelOrderRequest cancelOrderRequest) {
        //  入参检查
        checkCancelOrderRequestParam(cancelOrderRequest);
        //  分布式锁
        String orderId = cancelOrderRequest.getOrderId();
        String key = RedisLockKeyConstants.CANCEL_KEY + orderId;
        boolean lock = redisLock.tryLock(key);
        if (!lock) {
            throw new OrderBizException(OrderErrorCodeEnum.CANCEL_ORDER_REPEAT);
        }
        try {
            //  执行取消订单
            return executeCancelOrder(cancelOrderRequest, orderId);
        } catch (Exception e) {
            log.error("biz error", e);
            throw new OrderBizException(e.getMessage());
        } finally {
            redisLock.unlock(key);
        }
    }

    @Override
    public JsonResult<Boolean> executeCancelOrder(CancelOrderRequest cancelOrderRequest, String orderId) {
        // 1、组装数据
        OrderInfoDO orderInfoDO = findOrderInfo(orderId);
        CancelOrderAssembleRequest cancelOrderAssembleRequest = buildAssembleRequest(orderId, cancelOrderRequest, orderInfoDO);
        if (cancelOrderAssembleRequest.getOrderInfoDTO().getOrderStatus() >= OrderStatusEnum.OUT_STOCK.getCode()) {
            throw new OrderBizException(OrderErrorCodeEnum.CURRENT_ORDER_STATUS_CANNOT_CANCEL);
        }

        TransactionMQProducer producer = defaultProducer.getProducer();
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                try {
                    //  2、执行履约取消、更新订单状态、新增订单日志操作
                    afterSaleManager.cancelOrderFulfillmentAndUpdateOrderStatus(cancelOrderAssembleRequest);
                    return LocalTransactionState.COMMIT_MESSAGE;
                } catch (Exception e) {
                    log.error("system error", e);
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                //  查询订单状态是否已更新为"已取消"
                OrderInfoDO orderInfoByDatabase = orderInfoDAO.getByOrderId(orderId);
                if (OrderStatusEnum.CANCELED.getCode().equals(orderInfoByDatabase.getOrderStatus())) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        });

        try {
            Message message = new Message(RocketMqConstant.RELEASE_ASSETS_TOPIC,
                    JSONObject.toJSONString(cancelOrderAssembleRequest).getBytes(StandardCharsets.UTF_8));
            // 3、发送事务消息 释放权益资产
            TransactionSendResult result = producer.sendMessageInTransaction(message, cancelOrderAssembleRequest);
            if (!result.getLocalTransactionState().equals(LocalTransactionState.COMMIT_MESSAGE)) {
                throw new OrderBizException(OrderErrorCodeEnum.CANCEL_ORDER_PROCESS_FAILED);
            }
            return JsonResult.buildSuccess(true);
        } catch (Exception e) {
            throw new OrderBizException(OrderErrorCodeEnum.SEND_TRANSACTION_MQ_FAILED);
        }
    }

    /**
     * 组装 取消订单 数据
     */
    private CancelOrderAssembleRequest buildAssembleRequest(String orderId, CancelOrderRequest cancelOrderRequest, OrderInfoDO orderInfoDO) {
        Integer cancelType = cancelOrderRequest.getCancelType();
        OrderInfoDTO orderInfoDTO = orderConverter.orderInfoDO2DTO(orderInfoDO);
        orderInfoDTO.setCancelType(String.valueOf(cancelType));
        List<OrderItemDTO> orderItemDTOList = findOrderItemInfo(orderId);
        CancelOrderAssembleRequest cancelOrderAssembleRequest = orderConverter.convertCancelOrderRequest(cancelOrderRequest);
        cancelOrderAssembleRequest.setOrderId(orderId);
        cancelOrderAssembleRequest.setOrderInfoDTO(orderInfoDTO);
        cancelOrderAssembleRequest.setOrderItemDTOList(orderItemDTOList);
        cancelOrderAssembleRequest.setAfterSaleType(AfterSaleTypeEnum.RETURN_MONEY.getCode());

        return cancelOrderAssembleRequest;
    }


    /**
     * 获取订单条目
     */
    private List<OrderItemDTO> findOrderItemInfo(String orderId) {
        List<OrderItemDO> orderItemDOList = orderItemDAO.listByOrderId(orderId);
        if (orderItemDOList == null) {
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_ITEM_IS_NULL);
        }
        return orderConverter.orderItemDO2DTO(orderItemDOList);
    }

    /**
     * 获取订单信息
     */
    private OrderInfoDO findOrderInfo(String orderId) {
        OrderInfoDO orderInfoDO = orderInfoDAO.getByOrderId(orderId);
        if (orderInfoDO == null) {
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_NOT_FOUND);
        }
        return orderInfoDO;
    }

    /**
     * 更新支付退款回调售后信息
     */
    public void updatePaymentRefundCallbackAfterSale(RefundCallbackRequest payRefundCallbackRequest,
                                                     Integer afterSaleStatus, Integer refundStatus, String refundStatusMsg) {
        Long afterSaleId = Long.valueOf(payRefundCallbackRequest.getAfterSaleId());
        //  更新 订单售后表
        afterSaleInfoDAO.updateStatus(afterSaleId, AfterSaleStatusEnum.REFUNDING.getCode(), afterSaleStatus);

        //  新增 售后单变更表
        AfterSaleInfoDO afterSaleInfoDO = afterSaleInfoDAO.getOneByAfterSaleId(afterSaleId);
        AfterSaleLogDO afterSaleLogDO = afterSaleOperateLogFactory.get(afterSaleInfoDO,
                AfterSaleStatusChangeEnum.getBy(AfterSaleStatusEnum.REFUNDING.getCode(), afterSaleStatus));

        afterSaleLogDAO.save(afterSaleLogDO);

        //  更新 售后退款单表
        AfterSaleRefundDO afterSaleRefundDO = new AfterSaleRefundDO();
        afterSaleRefundDO.setAfterSaleId(payRefundCallbackRequest.getAfterSaleId());
        afterSaleRefundDO.setRefundStatus(refundStatus);
        afterSaleRefundDO.setRefundPayTime(payRefundCallbackRequest.getRefundTime());
        afterSaleRefundDO.setRemark(refundStatusMsg);

        afterSaleRefundDAO.updateAfterSaleRefundStatus(afterSaleRefundDO);
    }

    private void insertReturnGoodsAfterSaleLogTable(String afterSaleId, Integer preAfterSaleStatus, Integer currentAfterSaleStatus) {

        AfterSaleLogDO afterSaleLogDO = new AfterSaleLogDO();
        afterSaleLogDO.setAfterSaleId(afterSaleId);
        afterSaleLogDO.setPreStatus(preAfterSaleStatus);
        afterSaleLogDO.setCurrentStatus(currentAfterSaleStatus);
        //  售后退货的业务值
        afterSaleLogDO.setRemark(ReturnGoodsTypeEnum.AFTER_SALE_RETURN_GOODS.getMsg());

        afterSaleLogDAO.save(afterSaleLogDO);
        log.info("新增售后单变更信息, 售后单号:{},状态:PreStatus{},CurrentStatus:{}", afterSaleLogDO.getAfterSaleId(),
                afterSaleLogDO.getPreStatus(), afterSaleLogDO.getCurrentStatus());
    }

    /**
     * 更新售后单状态
     */
    public void updateAfterSaleStatus(AfterSaleInfoDO afterSaleInfoDO, Integer fromStatus, Integer toStatus) {
        //  更新 订单售后表
        afterSaleInfoDAO.updateStatus(afterSaleInfoDO.getAfterSaleId(), fromStatus, toStatus);

        //  新增 售后单变更表
        AfterSaleLogDO afterSaleLogDO = afterSaleOperateLogFactory.get(afterSaleInfoDO, AfterSaleStatusChangeEnum.getBy(fromStatus, toStatus));
        log.info("保存售后变更记录,售后单号:{},fromStatus:{}, toStatus:{}", afterSaleInfoDO.getAfterSaleId(), fromStatus, toStatus);

        afterSaleLogDAO.save(afterSaleLogDO);
    }


    /**
     * 售后退货流程 插入订单销售表
     */
    private void insertReturnGoodsAfterSaleInfoTable(OrderInfoDO orderInfoDO, Integer afterSaleType,
                                                     Integer cancelOrderAfterSaleStatus, AfterSaleInfoDO afterSaleInfoDO,
                                                     String afterSaleId) {

        afterSaleInfoDO.setAfterSaleId(Long.valueOf(afterSaleId));
        afterSaleInfoDO.setBusinessIdentifier(BusinessIdentifierEnum.SELF_MALL.getCode());
        afterSaleInfoDO.setOrderId(orderInfoDO.getOrderId());
        afterSaleInfoDO.setOrderSourceChannel(BusinessIdentifierEnum.SELF_MALL.getCode());
        afterSaleInfoDO.setUserId(orderInfoDO.getUserId());
        afterSaleInfoDO.setOrderType(OrderTypeEnum.NORMAL.getCode());
        afterSaleInfoDO.setApplyTime(new Date());
        afterSaleInfoDO.setAfterSaleStatus(cancelOrderAfterSaleStatus);
        //  用户售后退货的业务值
        afterSaleInfoDO.setApplySource(AfterSaleApplySourceEnum.USER_RETURN_GOODS.getCode());
        afterSaleInfoDO.setRemark(ReturnGoodsTypeEnum.AFTER_SALE_RETURN_GOODS.getMsg());
        afterSaleInfoDO.setApplyReasonCode(AfterSaleReasonEnum.USER.getCode());
        afterSaleInfoDO.setApplyReason(AfterSaleReasonEnum.USER.getMsg());
        afterSaleInfoDO.setAfterSaleTypeDetail(AfterSaleTypeDetailEnum.PART_REFUND.getCode());

        //  退货流程 只退订单的一笔条目
        if (AfterSaleTypeEnum.RETURN_GOODS.getCode().equals(afterSaleType)) {
            afterSaleInfoDO.setAfterSaleType(AfterSaleTypeEnum.RETURN_GOODS.getCode());
        }
        //  退货流程 退订单的全部条目 后续按照整笔退款逻辑处理
        if (AfterSaleTypeEnum.RETURN_MONEY.getCode().equals(afterSaleType)) {
            afterSaleInfoDO.setAfterSaleType(AfterSaleTypeEnum.RETURN_MONEY.getCode());
        }

        afterSaleInfoDAO.save(afterSaleInfoDO);

        log.info("新增订单售后记录,订单号:{},售后单号:{},订单售后状态:{}", orderInfoDO.getOrderId(), afterSaleId,
                afterSaleInfoDO.getAfterSaleStatus());
    }


    /**
     * 入参检查
     *
     * @param cancelOrderRequest 取消订单入参
     */
    private void checkCancelOrderRequestParam(CancelOrderRequest cancelOrderRequest) {
        ParamCheckUtil.checkObjectNonNull(cancelOrderRequest);

        //  订单状态
        Integer orderStatus = cancelOrderRequest.getOrderStatus();
        ParamCheckUtil.checkObjectNonNull(orderStatus, OrderErrorCodeEnum.ORDER_STATUS_IS_NULL);

        if (orderStatus.equals(OrderStatusEnum.CANCELED.getCode())) {
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_STATUS_CANCELED);
        }

        if (orderStatus >= OrderStatusEnum.OUT_STOCK.getCode()) {
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_STATUS_CHANGED);
        }

        //  订单ID
        String orderId = cancelOrderRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId, OrderErrorCodeEnum.CANCEL_ORDER_ID_IS_NULL);

        //  业务线标识
        Integer businessIdentifier = cancelOrderRequest.getBusinessIdentifier();
        ParamCheckUtil.checkObjectNonNull(businessIdentifier, OrderErrorCodeEnum.BUSINESS_IDENTIFIER_IS_NULL);

        //  订单取消类型
        Integer cancelType = cancelOrderRequest.getCancelType();
        ParamCheckUtil.checkObjectNonNull(cancelType, OrderErrorCodeEnum.CANCEL_TYPE_IS_NULL);

        //  用户ID
        String userId = cancelOrderRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId, OrderErrorCodeEnum.USER_ID_IS_NULL);

        //  订单类型
        Integer orderType = cancelOrderRequest.getOrderType();
        ParamCheckUtil.checkObjectNonNull(orderType, OrderErrorCodeEnum.ORDER_TYPE_IS_NULL);

    }

    public CancelOrderRefundAmountDTO calculatingCancelOrderRefundAmount(CancelOrderAssembleRequest cancelOrderAssembleRequest) {
        OrderInfoDTO orderInfoDTO = cancelOrderAssembleRequest.getOrderInfoDTO();
        CancelOrderRefundAmountDTO cancelOrderRefundAmountDTO = new CancelOrderRefundAmountDTO();

        //  退整笔订单的实际支付金额
        cancelOrderRefundAmountDTO.setOrderId(orderInfoDTO.getOrderId());
        cancelOrderRefundAmountDTO.setTotalAmount(orderInfoDTO.getTotalAmount());
        cancelOrderRefundAmountDTO.setReturnGoodAmount(orderInfoDTO.getPayAmount());

        return cancelOrderRefundAmountDTO;
    }

    /**
     * 缺品退款
     * todo 调用
     *
     * @return
     */
    private JsonResult<BigDecimal> lackRefund(String orderId, Long lackAfterSaleId) {
        AfterSaleInfoDO lackAfterSaleInfo = afterSaleInfoDAO.getOneByAfterSaleId(lackAfterSaleId);
        return JsonResult.buildSuccess(new BigDecimal(lackAfterSaleInfo.getRealRefundAmount()));
    }

    @Override
    public JsonResult<Boolean> processCancelOrder(CancelOrderAssembleRequest cancelOrderAssembleRequest) {
        String orderId = cancelOrderAssembleRequest.getOrderId();
        //  分布式锁
        String key = RedisLockKeyConstants.REFUND_KEY + orderId;
        try {
            boolean lock = redisLock.tryLock(key);
            if (!lock) {
                throw new OrderBizException(OrderErrorCodeEnum.PROCESS_REFUND_REPEAT);
            }

            //  执行退款前的准备工作
            //  生成售后订单号
            OrderInfoDTO orderInfoDTO = cancelOrderAssembleRequest.getOrderInfoDTO();
            OrderInfoDO orderInfoDO = orderConverter.orderInfoDTO2DO(orderInfoDTO);
            String afterSaleId = orderNoManager.genOrderId(OrderNoTypeEnum.AFTER_SALE.getCode(), orderInfoDO.getUserId());

            //  1、计算 取消订单 退款金额
            CancelOrderRefundAmountDTO cancelOrderRefundAmountDTO = calculatingCancelOrderRefundAmount(cancelOrderAssembleRequest);
            cancelOrderAssembleRequest.setCancelOrderRefundAmountDTO(cancelOrderRefundAmountDTO);

            TransactionMQProducer producer = defaultProducer.getProducer();
            producer.setTransactionListener(new TransactionListener() {
                @Override
                public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                    try {
                        //  2、取消订单操作 记录售后信息
                        afterSaleManager.insertCancelOrderAfterSale(cancelOrderAssembleRequest, AfterSaleStatusEnum.REVIEW_PASS.getCode(),
                                orderInfoDO, afterSaleId);
                        return LocalTransactionState.COMMIT_MESSAGE;
                    } catch (Exception e) {
                        log.error("system error", e);
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                    }
                }

                @Override
                public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                    //  查询售后数据是否插入成功
                    AfterSaleInfoDO afterSaleInfoDO = afterSaleInfoDAO.getOneByAfterSaleId(Long.valueOf(afterSaleId));
                    List<AfterSaleItemDO> afterSaleItemDOList = afterSaleItemDAO.listByAfterSaleId(Long.valueOf(afterSaleId));
                    List<AfterSaleLogDO> afterSaleLogDOList = afterSaleLogDAO.listByAfterSaleId(Long.valueOf(afterSaleId));
                    List<AfterSaleRefundDO> afterSaleRefundDOList = afterSaleRefundDAO.listByAfterSaleId(Long.valueOf(afterSaleId));
                    if (afterSaleInfoDO != null
                            && !afterSaleItemDOList.isEmpty()
                            && !afterSaleLogDOList.isEmpty()
                            && !afterSaleRefundDOList.isEmpty()) {
                        return LocalTransactionState.COMMIT_MESSAGE;
                    }
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            });

            try {
                //  3、组装事务MQ消息
                ActualRefundMessage actualRefundMessage = new ActualRefundMessage();
                actualRefundMessage.setOrderId(cancelOrderAssembleRequest.getOrderId());
                actualRefundMessage.setLastReturnGoods(cancelOrderAssembleRequest.isLastReturnGoods());
                actualRefundMessage.setAfterSaleId(Long.valueOf(afterSaleId));
                Message message = new Message(RocketMqConstant.ACTUAL_REFUND_TOPIC,
                        JSONObject.toJSONString(actualRefundMessage).getBytes(StandardCharsets.UTF_8));

                // 4、发送事务MQ消息
                TransactionSendResult result = producer.sendMessageInTransaction(message, actualRefundMessage);
                if (!result.getLocalTransactionState().equals(LocalTransactionState.COMMIT_MESSAGE)) {
                    throw new OrderBizException(OrderErrorCodeEnum.PROCESS_REFUND_FAILED);
                }
                return JsonResult.buildSuccess(true);
            } catch (Exception e) {
                throw new OrderBizException(OrderErrorCodeEnum.SEND_TRANSACTION_MQ_FAILED);
            }
        } finally {
            redisLock.unlock(key);
        }
    }


    @Override
    public JsonResult<Boolean> sendRefundMobileMessage(String orderId) {
        log.info("发退款通知短信,订单号:{}", orderId);
        return JsonResult.buildSuccess();
    }

    @Override
    public JsonResult<Boolean> sendRefundAppMessage(String orderId) {
        log.info("发退款通知APP信息,订单号:{}", orderId);
        return JsonResult.buildSuccess();
    }

    @Override
    public JsonResult<Boolean> refundMoney(ActualRefundMessage actualRefundMessage) {
        Long afterSaleId = actualRefundMessage.getAfterSaleId();
        String key = RedisLockKeyConstants.REFUND_KEY + afterSaleId;
        try {
            boolean lock = redisLock.tryLock(key);
            if (!lock) {
                throw new OrderBizException(OrderErrorCodeEnum.REFUND_MONEY_REPEAT);
            }
            AfterSaleInfoDO afterSaleInfoDO = afterSaleInfoDAO.getOneByAfterSaleId(actualRefundMessage.getAfterSaleId());
            AfterSaleRefundDO afterSaleRefundDO = afterSaleRefundDAO.findAfterSaleRefundByfterSaleId(String.valueOf(afterSaleId));

            //  1、封装调用支付退款接口的数据
            PayRefundRequest payRefundRequest = buildPayRefundRequest(actualRefundMessage, afterSaleRefundDO);

            //  2、执行退款
            payRemote.executeRefund(payRefundRequest);

            //  3、本次售后的订单条目是当前订单的最后一笔，发送事务MQ退优惠券,此时isLastReturnGoods标记是true
            if (actualRefundMessage.isLastReturnGoods()) {
                TransactionMQProducer producer = defaultProducer.getProducer();
                //  组装事务MQ消息体
                ReleaseUserCouponRequest releaseUserCouponRequest = buildLastOrderReleasesCouponMessage(producer, afterSaleInfoDO,
                        afterSaleId, actualRefundMessage);
                try {
                    // 4、发送事务消息 释放优惠券
                    Message message = new Message(RocketMqConstant.CANCEL_RELEASE_PROPERTY_TOPIC,
                            JSONObject.toJSONString(releaseUserCouponRequest).getBytes(StandardCharsets.UTF_8));
                    TransactionSendResult result = producer.sendMessageInTransaction(message, releaseUserCouponRequest);
                    if (!result.getLocalTransactionState().equals(LocalTransactionState.COMMIT_MESSAGE)) {
                        throw new OrderBizException(OrderErrorCodeEnum.REFUND_MONEY_RELEASE_COUPON_FAILED);
                    }
                    return JsonResult.buildSuccess(true);
                } catch (Exception e) {
                    throw new OrderBizException(OrderErrorCodeEnum.SEND_TRANSACTION_MQ_FAILED);
                }
            } else {
                //  当前售后条目非本订单的最后一笔 和 取消订单,在此更新售后状态后流程结束
                //  更新售后单状态
                updateAfterSaleStatus(afterSaleInfoDO, AfterSaleStatusEnum.REVIEW_PASS.getCode(), AfterSaleStatusEnum.REFUNDING.getCode());
                return JsonResult.buildSuccess(true);
            }

        } catch (OrderBizException e) {
            log.error("system error", e);
            return JsonResult.buildError(e.getMessage());
        } finally {
            redisLock.unlock(key);
        }
    }


    private ReleaseUserCouponRequest buildLastOrderReleasesCouponMessage(TransactionMQProducer producer, AfterSaleInfoDO afterSaleInfoDO,
                                                                         Long afterSaleId, ActualRefundMessage actualRefundMessage) {
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                try {
                    //  更新售后单状态
                    updateAfterSaleStatus(afterSaleInfoDO, AfterSaleStatusEnum.REVIEW_PASS.getCode(), AfterSaleStatusEnum.REFUNDING.getCode());
                    return LocalTransactionState.COMMIT_MESSAGE;
                } catch (Exception e) {
                    log.error("system error", e);
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                //  查询售后单状态是"退款中"
                AfterSaleInfoDO afterSaleInfoDO = afterSaleInfoDAO.getOneByAfterSaleId(afterSaleId);
                if (AfterSaleStatusEnum.REFUNDING.getCode().equals(afterSaleInfoDO.getAfterSaleStatus())) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        });

        //  组装释放优惠券权益消息数据
        String orderId = actualRefundMessage.getOrderId();
        OrderInfoDO orderInfoDO = orderInfoDAO.getByOrderId(orderId);
        ReleaseUserCouponRequest releaseUserCouponRequest = new ReleaseUserCouponRequest();
        releaseUserCouponRequest.setCouponId(orderInfoDO.getCouponId());
        releaseUserCouponRequest.setUserId(orderInfoDO.getUserId());

        return releaseUserCouponRequest;
    }

    /**
     * 当前业务限制说明：
     * 目前业务限定，一笔订单包含多笔订单条目，每次手动售后只能退一笔条目,不支持单笔条目多次退不同数量
     * <p>
     * 举例：
     * 一笔订单包含订单条目A（购买数量10）和订单条目B（购买数量1），每一次可单独发起 售后订单条目A or 售后订单条目B，
     * 如果是售后订单条目A，那么就是把A中购买数量10全部退掉
     * 如果是售后订单条目B，那么就是把B中购买数量1全部退款
     * <p>
     * 暂不支持第一次退A中的3条，第二次退A中的2条，第三次退A中的5条这种退法
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<Boolean> processApplyAfterSale(ReturnGoodsOrderRequest returnGoodsOrderRequest) {
        //  参数校验
        checkAfterSaleRequestParam(returnGoodsOrderRequest);
        try {
            //  1、售后单状态验证
            //  用order id和sku code查到售后id
            String orderId = returnGoodsOrderRequest.getOrderId();
            String skuCode = returnGoodsOrderRequest.getSkuCode();
            /*
                场景校验逻辑：
                第一种场景：订单条目A是第一次发起手动售后，此时售后订单条目表没有该订单的记录，orderIdAndSkuCodeList 是空，正常执行后面的售后逻辑
                第二种场景：订单条目A已发起过售后，非"撤销成功"状态的售后单不允许重复发起售后
             */
            List<AfterSaleItemDO> orderIdAndSkuCodeList = afterSaleItemDAO.getOrderIdAndSkuCode(orderId, skuCode);
            if (!orderIdAndSkuCodeList.isEmpty()) {
                //  查询订单条目所属的售后单状态
                Long afterSaleId = orderIdAndSkuCodeList.get(0).getAfterSaleId();
                AfterSaleInfoDO afterSaleInfoDO = afterSaleInfoDAO.getOneByAfterSaleId(afterSaleId);
                if (!AfterSaleStatusEnum.REVOKE.getCode().equals(afterSaleInfoDO.getAfterSaleStatus())) {
                    //  非"撤销成功"状态的售后单不能重复发起售后
                    throw new OrderBizException(OrderErrorCodeEnum.PROCESS_APPLY_AFTER_SALE_CANNOT_REPEAT);
                }
            }

            // 2、封装数据
            ReturnGoodsAssembleRequest returnGoodsAssembleRequest = buildReturnGoodsData(returnGoodsOrderRequest);

            // 3、计算退货金额
            returnGoodsAssembleRequest = calculateReturnGoodsAmount(returnGoodsAssembleRequest);

            TransactionMQProducer producer = defaultProducer.getProducer();
            ReturnGoodsAssembleRequest finalReturnGoodsAssembleRequest = returnGoodsAssembleRequest;

            // 4、生成售后订单号
            OrderInfoDTO orderInfoDTO = returnGoodsAssembleRequest.getOrderInfoDTO();
            OrderInfoDO orderInfoDO = orderConverter.orderInfoDTO2DO(orderInfoDTO);
            String afterSaleId = orderNoManager.genOrderId(OrderNoTypeEnum.AFTER_SALE.getCode(), orderInfoDO.getUserId());

            producer.setTransactionListener(new TransactionListener() {
                @Override
                public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                    try {
                        // 5、售后数据落库
                        insertReturnGoodsAfterSale(finalReturnGoodsAssembleRequest, AfterSaleStatusEnum.COMMITED.getCode(),
                                afterSaleId, orderInfoDO, orderInfoDTO);
                        return LocalTransactionState.COMMIT_MESSAGE;
                    } catch (Exception e) {
                        log.error("system error", e);
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                    }
                }

                @Override
                public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                    //  查询售后数据是否插入成功
                    AfterSaleInfoDO afterSaleInfoDO = afterSaleInfoDAO.getOneByAfterSaleId(Long.valueOf(afterSaleId));
                    List<AfterSaleItemDO> afterSaleItemDOList = afterSaleItemDAO.listByAfterSaleId(Long.valueOf(afterSaleId));
                    List<AfterSaleLogDO> afterSaleLogDOList = afterSaleLogDAO.listByAfterSaleId(Long.valueOf(afterSaleId));
                    List<AfterSaleRefundDO> afterSaleRefundDOList = afterSaleRefundDAO.listByAfterSaleId(Long.valueOf(afterSaleId));
                    if (afterSaleInfoDO != null
                            && !afterSaleItemDOList.isEmpty()
                            && !afterSaleLogDOList.isEmpty()
                            && !afterSaleRefundDOList.isEmpty()) {
                        return LocalTransactionState.COMMIT_MESSAGE;
                    }
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            });

            try {
                // 6、组装发送消息数据
                CustomerReceiveAfterSaleRequest customerReceiveAfterSaleRequest
                        = orderConverter.convertReturnGoodsAssembleRequest(returnGoodsAssembleRequest);
                customerReceiveAfterSaleRequest.setAfterSaleId(afterSaleId);
                Message message = new Message(RocketMqConstant.AFTER_SALE_CUSTOMER_AUDIT_TOPIC,
                        JSONObject.toJSONString(customerReceiveAfterSaleRequest).getBytes(StandardCharsets.UTF_8));
                // 7、发起客服审核
                TransactionSendResult result = producer.sendMessageInTransaction(message, customerReceiveAfterSaleRequest);
                if (!result.getLocalTransactionState().equals(LocalTransactionState.COMMIT_MESSAGE)) {
                    throw new OrderBizException(OrderErrorCodeEnum.SEND_AFTER_SALE_CUSTOMER_AUDIT_MQ_FAILED);
                }
                return JsonResult.buildSuccess(true);
            } catch (Exception e) {
                throw new OrderBizException(OrderErrorCodeEnum.SEND_TRANSACTION_MQ_FAILED);
            }

        } catch (BaseBizException e) {
            log.error("system error", e);
            return JsonResult.buildError(e.getMessage());
        }
    }

    private void checkAfterSaleRequestParam(ReturnGoodsOrderRequest returnGoodsOrderRequest) {
        ParamCheckUtil.checkObjectNonNull(returnGoodsOrderRequest);

        String orderId = returnGoodsOrderRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId, OrderErrorCodeEnum.ORDER_ID_IS_NULL);

        String userId = returnGoodsOrderRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId, OrderErrorCodeEnum.USER_ID_IS_NULL);

        Integer businessIdentifier = returnGoodsOrderRequest.getBusinessIdentifier();
        ParamCheckUtil.checkObjectNonNull(businessIdentifier, OrderErrorCodeEnum.BUSINESS_IDENTIFIER_IS_NULL);

        Integer returnGoodsCode = returnGoodsOrderRequest.getReturnGoodsCode();
        ParamCheckUtil.checkObjectNonNull(returnGoodsCode, OrderErrorCodeEnum.RETURN_GOODS_CODE_IS_NULL);

        String skuCode = returnGoodsOrderRequest.getSkuCode();
        ParamCheckUtil.checkStringNonEmpty(skuCode, OrderErrorCodeEnum.SKU_IS_NULL);

    }

    private void insertReturnGoodsAfterSale(ReturnGoodsAssembleRequest finalReturnGoodsAssembleRequest, Integer afterSaleStatus,
                                            String afterSaleId, OrderInfoDO orderInfoDO, OrderInfoDTO orderInfoDTO) {
        Integer afterSaleType = finalReturnGoodsAssembleRequest.getAfterSaleType();

        //  售后退货过程中的 申请退款金额 和 实际退款金额 是计算出来的，金额有可能不同
        AfterSaleInfoDO afterSaleInfoDO = new AfterSaleInfoDO();
        Integer applyRefundAmount = finalReturnGoodsAssembleRequest.getApplyRefundAmount();
        afterSaleInfoDO.setApplyRefundAmount(applyRefundAmount);
        Integer returnGoodAmount = finalReturnGoodsAssembleRequest.getReturnGoodAmount();
        afterSaleInfoDO.setRealRefundAmount(returnGoodAmount);

        //  1、新增售后订单表
        Integer cancelOrderAfterSaleStatus = AfterSaleStatusEnum.COMMITED.getCode();
        insertReturnGoodsAfterSaleInfoTable(orderInfoDO, afterSaleType, cancelOrderAfterSaleStatus, afterSaleInfoDO, afterSaleId);

        //  2、新增售后条目表
        insertAfterSaleItemTable(orderInfoDO.getOrderId(), finalReturnGoodsAssembleRequest.getRefundOrderItemDTO(), afterSaleId);

        //  3、新增售后变更表
        insertReturnGoodsAfterSaleLogTable(afterSaleId, AfterSaleStatusEnum.UN_CREATED.getCode(), afterSaleStatus);

        //  4、新增售后支付表
        insertAfterSaleRefundTable(orderInfoDTO, afterSaleId, afterSaleInfoDO);
    }

    private ReturnGoodsAssembleRequest buildReturnGoodsData(ReturnGoodsOrderRequest returnGoodsOrderRequest) {
        ReturnGoodsAssembleRequest returnGoodsAssembleRequest = orderConverter.returnGoodRequest2AssembleRequest(returnGoodsOrderRequest);
        String orderId = returnGoodsAssembleRequest.getOrderId();

        //  封装 订单信息
        OrderInfoDO orderInfoDO = orderInfoDAO.getByOrderId(orderId);
        OrderInfoDTO orderInfoDTO = orderConverter.orderInfoDO2DTO(orderInfoDO);
        returnGoodsAssembleRequest.setOrderInfoDTO(orderInfoDTO);

        //  封装 订单条目
        List<OrderItemDO> orderItemDOList = orderItemDAO.listByOrderId(orderId);
        List<OrderItemDTO> orderItemDTOList = orderConverter.orderItemDO2DTO(orderItemDOList);
        returnGoodsAssembleRequest.setOrderItemDTOList(orderItemDTOList);

        //  封装 订单售后条目
        List<AfterSaleItemDO> afterSaleItemDOList = afterSaleItemDAO.listByOrderId(Long.valueOf(orderId));
        List<AfterSaleOrderItemDTO> afterSaleOrderItemRequestList = afterSaleConverter.afterSaleOrderItemDO2DTO(afterSaleItemDOList);
        returnGoodsAssembleRequest.setAfterSaleOrderItemDTOList(afterSaleOrderItemRequestList);

        return returnGoodsAssembleRequest;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<Boolean> receivePaymentRefundCallback(RefundCallbackRequest payRefundCallbackRequest) {
        String afterSaleId = payRefundCallbackRequest.getAfterSaleId();
        String key = RedisLockKeyConstants.REFUND_KEY + afterSaleId;
        try {
            boolean lock = redisLock.tryLock(key);
            if (!lock) {
                throw new OrderBizException(OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_REPEAT);
            }
            //  1、入参校验
            checkRefundCallbackParam(payRefundCallbackRequest);

            //  2、获取三方支付退款的返回结果
            Integer afterSaleStatus;
            Integer refundStatus;
            String refundStatusMsg;
            if (RefundStatusEnum.REFUND_SUCCESS.getCode().equals(payRefundCallbackRequest.getRefundStatus())) {
                afterSaleStatus = AfterSaleStatusEnum.REFUNDED.getCode();
                refundStatus = RefundStatusEnum.REFUND_SUCCESS.getCode();
                refundStatusMsg = RefundStatusEnum.REFUND_SUCCESS.getMsg();
            } else {
                afterSaleStatus = AfterSaleStatusEnum.FAILED.getCode();
                refundStatus = RefundStatusEnum.REFUND_FAIL.getCode();
                refundStatusMsg = RefundStatusEnum.REFUND_FAIL.getMsg();
            }

            //  3、更新售后记录，支付退款回调更新售后信息
            updatePaymentRefundCallbackAfterSale(payRefundCallbackRequest, afterSaleStatus, refundStatus, refundStatusMsg);

            //  4、发短信
            sendRefundMobileMessage(afterSaleId);

            //  5、发APP通知
            sendRefundAppMessage(afterSaleId);

            return JsonResult.buildSuccess();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new OrderBizException(OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_FAILED);
        } finally {
            redisLock.unlock(key);
        }
    }

    private void checkRefundCallbackParam(RefundCallbackRequest payRefundCallbackRequest) {
        ParamCheckUtil.checkObjectNonNull(payRefundCallbackRequest);

        String orderId = payRefundCallbackRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId, OrderErrorCodeEnum.CANCEL_ORDER_ID_IS_NULL);

        String batchNo = payRefundCallbackRequest.getBatchNo();
        ParamCheckUtil.checkStringNonEmpty(batchNo, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_BATCH_NO_IS_NULL);

        Integer refundStatus = payRefundCallbackRequest.getRefundStatus();
        ParamCheckUtil.checkObjectNonNull(refundStatus, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_STATUS_NO_IS_NUL);

        Integer refundFee = payRefundCallbackRequest.getRefundFee();
        ParamCheckUtil.checkObjectNonNull(refundFee, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_FEE_NO_IS_NUL);

        Integer totalFee = payRefundCallbackRequest.getTotalFee();
        ParamCheckUtil.checkObjectNonNull(totalFee, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_TOTAL_FEE_NO_IS_NUL);

        String sign = payRefundCallbackRequest.getSign();
        ParamCheckUtil.checkStringNonEmpty(sign, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_SIGN_NO_IS_NUL);

        String tradeNo = payRefundCallbackRequest.getTradeNo();
        ParamCheckUtil.checkStringNonEmpty(tradeNo, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_TRADE_NO_IS_NUL);

        String afterSaleId = payRefundCallbackRequest.getAfterSaleId();
        ParamCheckUtil.checkStringNonEmpty(afterSaleId, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_AFTER_SALE_ID_IS_NULL);

        Date refundTime = payRefundCallbackRequest.getRefundTime();
        ParamCheckUtil.checkObjectNonNull(refundTime, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_AFTER_SALE_REFUND_TIME_IS_NULL);

        //  数据库中当前售后单不是未退款状态，表示已经退款成功 or 失败，那么本次就不能再执行支付回调退款
        AfterSaleRefundDO afterSaleByDatabase = afterSaleRefundDAO.findAfterSaleRefundByfterSaleId(afterSaleId);
        if (!RefundStatusEnum.UN_REFUND.getCode().equals(afterSaleByDatabase.getRefundStatus())) {
            throw new OrderBizException(OrderErrorCodeEnum.REPEAT_CALLBACK);
        }
    }

    /**
     * 一笔订单只有一个条目：整笔退
     * 一笔订单有多个条目，每次退一条，退完最后一条补退运费和优惠券
     */
    public ReturnGoodsAssembleRequest calculateReturnGoodsAmount(ReturnGoodsAssembleRequest returnGoodsAssembleRequest) {
        String skuCode = returnGoodsAssembleRequest.getSkuCode();
        String orderId = returnGoodsAssembleRequest.getOrderId();
        //  为便于以后扩展，这里封装成list
        List<OrderItemDTO> refundOrderItemDTOList = Lists.newArrayList();
        List<OrderItemDTO> orderItemDTOList = returnGoodsAssembleRequest.getOrderItemDTOList();
        List<AfterSaleOrderItemDTO> afterSaleOrderItemDTOList = returnGoodsAssembleRequest.getAfterSaleOrderItemDTOList();
        //  订单条目数
        int orderItemNum = orderItemDTOList.size();
        //  售后订单条目数
        int afterSaleOrderItemNum = afterSaleOrderItemDTOList.size();
        //  订单条目数，只有一条，退整笔 ，本次售后类型：全部退款
        if (orderItemNum == 1) {
            OrderItemDTO orderItemDTO = orderItemDTOList.get(0);
            returnGoodsAssembleRequest.setAfterSaleType(AfterSaleTypeEnum.RETURN_MONEY.getCode());
            return calculateWholeOrderFefundAmount(
                    orderId,
                    orderItemDTO.getPayAmount(),
                    orderItemDTO.getOriginAmount(),
                    returnGoodsAssembleRequest
            );
        }
        //  该笔订单还有其他条目，本次售后类型：退货
        returnGoodsAssembleRequest.setAfterSaleType(AfterSaleTypeEnum.RETURN_GOODS.getCode());
        //  skuCode和orderId查询本次要退的条目
        OrderItemDO orderItemDO = orderItemDAO.getOrderItemBySkuIdAndOrderId(orderId, skuCode);
        //  该笔订单条目数 = 已存的售后订单条目数 + 本次退货的条目 (每次退 1 条)
        if (orderItemNum == afterSaleOrderItemNum + 1) {
            //  当前条目是订单的最后一笔
            returnGoodsAssembleRequest = calculateWholeOrderFefundAmount(
                    orderId,
                    orderItemDO.getPayAmount(),
                    orderItemDO.getOriginAmount(),
                    returnGoodsAssembleRequest
            );
        } else {
            //  只退当前条目
            returnGoodsAssembleRequest.setReturnGoodAmount(orderItemDO.getPayAmount());
            returnGoodsAssembleRequest.setApplyRefundAmount(orderItemDO.getOriginAmount());
            returnGoodsAssembleRequest.setLastReturnGoods(false);
        }
        refundOrderItemDTOList.add(orderConverter.orderItemDO2DTO(orderItemDO));
        returnGoodsAssembleRequest.setRefundOrderItemDTO(refundOrderItemDTOList);

        return returnGoodsAssembleRequest;
    }

    private ReturnGoodsAssembleRequest calculateWholeOrderFefundAmount(String orderId, Integer payAmount,
                                                                       Integer originAmount,
                                                                       ReturnGoodsAssembleRequest returnGoodsAssembleRequest) {
        //  模拟取运费
        OrderAmountDO deliveryAmount = orderAmountDAO.getOne(orderId, AmountTypeEnum.SHIPPING_AMOUNT.getCode());
        Integer freightAmount = (deliveryAmount == null || deliveryAmount.getAmount() == null) ? 0 : deliveryAmount.getAmount();
        //  最终退款金额 = 实际退款金额 + 运费
        Integer returnGoodAmount = payAmount + freightAmount;
        returnGoodsAssembleRequest.setReturnGoodAmount(returnGoodAmount);
        returnGoodsAssembleRequest.setApplyRefundAmount(originAmount);
        returnGoodsAssembleRequest.setAfterSaleType(AfterSaleTypeEnum.RETURN_MONEY.getCode());
        returnGoodsAssembleRequest.setLastReturnGoods(true);

        return returnGoodsAssembleRequest;

    }

    private PayRefundRequest buildPayRefundRequest(ActualRefundMessage actualRefundMessage, AfterSaleRefundDO afterSaleRefundDO) {
        String orderId = actualRefundMessage.getOrderId();
        PayRefundRequest payRefundRequest = new PayRefundRequest();
        payRefundRequest.setOrderId(orderId);
        payRefundRequest.setAfterSaleId(actualRefundMessage.getAfterSaleId());
        payRefundRequest.setRefundAmount(afterSaleRefundDO.getRefundAmount());

        return payRefundRequest;
    }

    private void insertAfterSaleItemTable(String orderId, List<OrderItemDTO> orderItemDTOList, String afterSaleId) {

        List<AfterSaleItemDO> afterSaleItemDOList = Lists.newArrayList();
        for (OrderItemDTO orderItem : orderItemDTOList) {
            AfterSaleItemDO afterSaleItemDO = new AfterSaleItemDO();
            afterSaleItemDO.setAfterSaleId(Long.valueOf(afterSaleId));
            afterSaleItemDO.setOrderId(orderId);
            afterSaleItemDO.setSkuCode(orderItem.getSkuCode());
            afterSaleItemDO.setProductName(orderItem.getProductName());
            afterSaleItemDO.setProductImg(orderItem.getProductImg());
            afterSaleItemDO.setReturnQuantity(orderItem.getSaleQuantity());
            afterSaleItemDO.setOriginAmount(orderItem.getOriginAmount());
            afterSaleItemDO.setApplyRefundAmount(orderItem.getOriginAmount());
            afterSaleItemDO.setRealRefundAmount(orderItem.getPayAmount());

            afterSaleItemDOList.add(afterSaleItemDO);
        }
        afterSaleItemDAO.saveBatch(afterSaleItemDOList);
    }

    private AfterSaleRefundDO insertAfterSaleRefundTable(OrderInfoDTO orderInfoDTO, String afterSaleId, AfterSaleInfoDO afterSaleInfoDO) {
        String orderId = orderInfoDTO.getOrderId();
        OrderPaymentDetailDO paymentDetail = orderPaymentDetailDAO.getPaymentDetailByOrderId(orderId);

        AfterSaleRefundDO afterSaleRefundDO = new AfterSaleRefundDO();
        afterSaleRefundDO.setAfterSaleId(afterSaleId);
        afterSaleRefundDO.setOrderId(orderId);
        afterSaleRefundDO.setAccountType(AccountTypeEnum.THIRD.getCode());
        afterSaleRefundDO.setRefundStatus(RefundStatusEnum.UN_REFUND.getCode());
        afterSaleRefundDO.setRemark(RefundStatusEnum.UN_REFUND.getMsg());
        afterSaleRefundDO.setRefundAmount(afterSaleInfoDO.getRealRefundAmount());
        afterSaleRefundDO.setAfterSaleBatchNo(orderId + RandomUtil.genRandomNumber(10));

        if (paymentDetail != null) {
            afterSaleRefundDO.setOutTradeNo(paymentDetail.getOutTradeNo());
            afterSaleRefundDO.setPayType(paymentDetail.getPayType());
        }
        afterSaleRefundDAO.save(afterSaleRefundDO);
        log.info("新增售后支付信息,订单号:{},售后单号:{},状态:{}", orderId, afterSaleId, afterSaleRefundDO.getRefundStatus());
        return afterSaleRefundDO;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void revokeAfterSale(RevokeAfterSaleRequest request) {

        //1、查询售后单
        Long afterSaleId = request.getAfterSaleId();
        AfterSaleInfoDO afterSaleInfo = afterSaleInfoDAO.getOneByAfterSaleId(afterSaleId);
        ParamCheckUtil.checkObjectNonNull(afterSaleInfo, OrderErrorCodeEnum.AFTER_SALE_ID_IS_NULL);

        //2、校验售后单是否可以撤销：只有提交申请状态才可以撤销
        if (!AfterSaleStatusEnum.COMMITED.getCode().equals(afterSaleInfo.getAfterSaleStatus())) {
            throw new OrderBizException(OrderErrorCodeEnum.AFTER_SALE_CANNOT_REVOKE);
        }

        //3、更新售后单状态为："已撤销"
        afterSaleInfoDAO.updateStatus(afterSaleInfo.getAfterSaleId(), AfterSaleStatusEnum.COMMITED.getCode(),
                AfterSaleStatusEnum.REVOKE.getCode());

        //4、增加一条售后单操作日志
        afterSaleLogDAO.save(afterSaleOperateLogFactory.get(afterSaleInfo, AfterSaleStatusChangeEnum.AFTER_SALE_REVOKE));

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void receiveCustomerAuditReject(CustomerAuditAssembleRequest customerAuditAssembleResult) {
        AfterSaleInfoDO afterSaleInfoDO = afterSaleInfoDAO.getOneByAfterSaleId(customerAuditAssembleResult.getAfterSaleId());
        //  幂等校验：防止客服重复审核订单
        if (afterSaleInfoDO.getAfterSaleStatus() > AfterSaleStatusEnum.COMMITED.getCode()) {
            throw new OrderBizException(OrderErrorCodeEnum.CUSTOMER_AUDIT_CANNOT_REPEAT);
        }

        //  更新售后信息
        customerAuditAssembleResult.setReviewReason(CustomerAuditResult.REJECT.getMsg());
        afterSaleInfoDAO.updateCustomerAuditAfterSaleResult(AfterSaleStatusEnum.REVIEW_REJECTED.getCode(), customerAuditAssembleResult);

        //  记录售后日志
        AfterSaleLogDO afterSaleLogDO = afterSaleOperateLogFactory.get(afterSaleInfoDO, AfterSaleStatusChangeEnum.AFTER_SALE_CUSTOMER_AUDIT_REJECT);
        afterSaleLogDAO.save(afterSaleLogDO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void receiveCustomerAuditAccept(CustomerAuditAssembleRequest customerAuditAssembleResult) {
        AfterSaleInfoDO afterSaleInfoDO = afterSaleInfoDAO.getOneByAfterSaleId(customerAuditAssembleResult.getAfterSaleId());
        //  幂等校验：防止客服重复审核订单
        if (afterSaleInfoDO.getAfterSaleStatus() > AfterSaleStatusEnum.COMMITED.getCode()) {
            throw new OrderBizException(OrderErrorCodeEnum.CUSTOMER_AUDIT_CANNOT_REPEAT);
        }

        //  更新售后信息
        customerAuditAssembleResult.setReviewReason(CustomerAuditResult.ACCEPT.getMsg());
        afterSaleInfoDAO.updateCustomerAuditAfterSaleResult(AfterSaleStatusEnum.REVIEW_PASS.getCode(), customerAuditAssembleResult);

        //  记录售后日志
        AfterSaleLogDO afterSaleLogDO = afterSaleOperateLogFactory.get(afterSaleInfoDO, AfterSaleStatusChangeEnum.AFTER_SALE_CUSTOMER_AUDIT_PASS);
        afterSaleLogDAO.save(afterSaleLogDO);

    }

    @Override
    public Integer findCustomerAuditAfterSaleStatus(Long afterSaleId) {
        AfterSaleInfoDO afterSaleInfoDO = afterSaleInfoDAO.getOneByAfterSaleId(afterSaleId);
        return afterSaleInfoDO.getAfterSaleStatus();
    }
}
