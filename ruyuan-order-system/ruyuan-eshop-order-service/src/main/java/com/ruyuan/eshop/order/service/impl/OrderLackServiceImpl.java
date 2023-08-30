package com.ruyuan.eshop.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.constants.RocketMqConstant;
import com.ruyuan.eshop.common.enums.AfterSaleTypeDetailEnum;
import com.ruyuan.eshop.common.enums.AfterSaleTypeEnum;
import com.ruyuan.eshop.common.enums.OrderStatusEnum;
import com.ruyuan.eshop.common.exception.BaseBizException;
import com.ruyuan.eshop.common.message.ActualRefundMessage;
import com.ruyuan.eshop.common.utils.ExtJsonUtil;
import com.ruyuan.eshop.common.utils.ParamCheckUtil;
import com.ruyuan.eshop.fulfill.domain.request.ReceiveFulfillRequest;
import com.ruyuan.eshop.order.dao.AfterSaleInfoDAO;
import com.ruyuan.eshop.order.dao.OrderInfoDAO;
import com.ruyuan.eshop.order.dao.OrderItemDAO;
import com.ruyuan.eshop.order.domain.dto.*;
import com.ruyuan.eshop.order.domain.entity.*;
import com.ruyuan.eshop.order.domain.request.LackItemRequest;
import com.ruyuan.eshop.order.domain.request.LackRequest;
import com.ruyuan.eshop.order.enums.*;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
import com.ruyuan.eshop.order.manager.OrderNoManager;
import com.ruyuan.eshop.order.mq.producer.DefaultProducer;
import com.ruyuan.eshop.order.remote.ProductRemote;
import com.ruyuan.eshop.order.service.OrderLackService;
import com.ruyuan.eshop.order.service.amount.AfterSaleAmountService;
import com.ruyuan.eshop.product.domain.dto.ProductSkuDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ruyuan.eshop.common.constants.RocketMqConstant.ACTUAL_REFUND_TOPIC;
import static com.ruyuan.eshop.common.constants.RocketMqConstant.TRIGGER_ORDER_FULFILL_TOPIC;

@Service
@Slf4j
public class OrderLackServiceImpl implements OrderLackService {

    @Autowired
    private OrderInfoDAO orderInfoDAO;

    @Autowired
    private OrderItemDAO orderItemDAO;

    @Autowired
    private OrderNoManager orderNoManager;

    @Autowired
    private AfterSaleAmountService afterSaleAmountService;

    @Autowired
    private ProductRemote productRemote;

    @Autowired
    private DefaultProducer defaultProducer;

    @Autowired
    private OrderLackProcessor orderLackProcessor;

    @Autowired
    private AfterSaleInfoDAO afterSaleInfoDAO;

    @Override
    public CheckLackDTO checkRequest(LackRequest request) throws OrderBizException {

        //1、查询订单
        OrderInfoDO order = orderInfoDAO.getByOrderId(request.getOrderId());
        ParamCheckUtil.checkObjectNonNull(order, OrderErrorCodeEnum.ORDER_NOT_FOUND);

        //2、校验订单是否可以发起缺品
        // 可以发起缺品的前置条件：
        // （1) 订单的状态为："已出库"
        //  (2) 订单未发起过缺品

        // 解释一下为啥是"已库存"状态才能发起缺品：
        // 缺品的业务逻辑是这样的，当订单支付后，进入履约流程，仓库人员捡货当时候发现现有商品无法满足下单所需，即"缺品"了
        // 仓库人员首先会将通知订单系统将订单的状态变为"已出库"，然后会再来调用这个缺品的接口
        if (!OrderStatusEnum.canLack().contains(order.getOrderStatus()) || isOrderLacked(order)) {
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_NOT_ALLOW_TO_LACK);
        }

        //3、查询订单item
        List<OrderItemDO> orderItems = orderItemDAO.listByOrderId(request.getOrderId());

        //4、校验具体的缺品项
        List<LackItemDTO> lackItems = new ArrayList<>();
        for (LackItemRequest itemRequest : request.getLackItems()) {
            lackItems.add(checkLackItem(order, orderItems, itemRequest));
        }

        //5、构造返参
        return new CheckLackDTO(order, lackItems);
    }

    @Override
    public boolean isOrderLacked(OrderInfoDO order) {
        OrderExtJsonDTO orderExtJson = ExtJsonUtil.parseExtJson(order.getExtJson(), OrderExtJsonDTO.class);
        if (null != orderExtJson) {
            return orderExtJson.getLackFlag();
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LackDTO executeLackRequest(LackRequest request, CheckLackDTO checkLackItemDTO) throws Exception {
        OrderInfoDO order = checkLackItemDTO.getOrder();
        List<LackItemDTO> lackItems = checkLackItemDTO.getLackItems();

        //1、生成缺品售后单
        AfterSaleInfoDO lackAfterSaleOrder = buildLackAfterSaleInfo(order);

        //2、生成缺品售后单item
        List<AfterSaleItemDO> afterSaleItems = new ArrayList<>();
        lackItems.forEach(item -> {
            afterSaleItems.add(buildLackAfterSaleItem(order, lackAfterSaleOrder, item));
        });

        //3、计算订单缺品退款总金额
        Integer lackApplyRefundAmount = afterSaleAmountService.calculateOrderLackApplyRefundAmount(afterSaleItems);
        Integer lackRealRefundAmount = afterSaleAmountService.calculateOrderLackRealRefundAmount(afterSaleItems);
        lackAfterSaleOrder.setApplyRefundAmount(lackApplyRefundAmount);
        lackAfterSaleOrder.setRealRefundAmount(lackRealRefundAmount);

        //4、构造售后退款单
        AfterSaleRefundDO afterSaleRefund = buildLackAfterSaleRefundDO(order, lackAfterSaleOrder);

        //5、构造订单缺品扩展信息
        OrderExtJsonDTO lackExtJson = buildOrderLackExtJson(request, order, lackAfterSaleOrder);

        //6、构造订单缺品信息
        OrderLackInfo orderLackInfo = OrderLackInfo.builder()
                .lackAfterSaleOrder(lackAfterSaleOrder)
                .afterSaleItems(afterSaleItems)
                .afterSaleRefund(afterSaleRefund)
                .lackExtJson(lackExtJson)
                .orderId(order.getOrderId())
                .build();

        //7、通过缺品事务消息保存缺品数据
        TransactionMQProducer producer = defaultProducer.getProducer();
        producer.setTransactionListener(new TransactionListener() {

            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                try {
                    //保存缺品数据
                    orderLackProcessor.saveLackInfo(orderLackInfo);
                    return LocalTransactionState.COMMIT_MESSAGE;
                } catch (BaseBizException e) {
                    throw e;
                } catch (Exception e) {
                    log.error("system error", e);
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }

            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                // 检查缺品售后单是否已经创建
                Long afterSaleId = lackAfterSaleOrder.getAfterSaleId();
                AfterSaleInfoDO afterSaleInfoDO = afterSaleInfoDAO.getOneByAfterSaleId(afterSaleId);
                if (afterSaleInfoDO != null) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        });


        //8、发送缺品退款的消息
        ActualRefundMessage actualRefundMessage = new ActualRefundMessage();
        actualRefundMessage.setAfterSaleRefundId(afterSaleRefund.getId());
        actualRefundMessage.setOrderId(order.getOrderId());
        actualRefundMessage.setAfterSaleId(lackAfterSaleOrder.getAfterSaleId());
        String topic = ACTUAL_REFUND_TOPIC;
        byte[] body = JSON.toJSONString(actualRefundMessage).getBytes(StandardCharsets.UTF_8);
        Message mq = new Message(topic, null, order.getOrderId(), body);
        producer.sendMessageInTransaction(mq, order);

        return new LackDTO(order.getOrderId(), lackAfterSaleOrder.getAfterSaleId());
    }

    /**
     * 构造订单缺品扩展信息
     *
     * @return
     */
    private OrderExtJsonDTO buildOrderLackExtJson(LackRequest request, OrderInfoDO order
            , AfterSaleInfoDO lackAfterSaleOrder) {

        OrderExtJsonDTO orderExtJson = new OrderExtJsonDTO();
        orderExtJson.setLackFlag(true);

        OrderLackInfoDTO lackInfo = new OrderLackInfoDTO();
        lackInfo.setLackItems(request.getLackItems());
        lackInfo.setOrderId(order.getOrderId());
        lackInfo.setApplyRefundAmount(lackInfo.getApplyRefundAmount());
        lackInfo.setRealRefundAmount(lackInfo.getRealRefundAmount());
        orderExtJson.setLackInfo(lackInfo);

        return orderExtJson;
    }


    /**
     * 构造缺品售后单
     *
     * @param order
     * @return
     */
    private AfterSaleInfoDO buildLackAfterSaleInfo(OrderInfoDO order) {

        //构造售后单
        String userId = order.getUserId();
        AfterSaleInfoDO afterSaleInfoDO = new AfterSaleInfoDO();
        String afterSaleId = orderNoManager.genOrderId(OrderNoTypeEnum.AFTER_SALE.getCode(), userId);
        afterSaleInfoDO.setAfterSaleId(Long.valueOf(afterSaleId));
        afterSaleInfoDO.setOrderId(order.getOrderId());
        afterSaleInfoDO.setOrderSourceChannel(BusinessIdentifierEnum.SELF_MALL.getCode());
        afterSaleInfoDO.setUserId(userId);
        afterSaleInfoDO.setOrderType(OrderTypeEnum.NORMAL.getCode());
        afterSaleInfoDO.setAfterSaleType(AfterSaleTypeEnum.RETURN_MONEY.getCode());
        afterSaleInfoDO.setAfterSaleTypeDetail(AfterSaleTypeDetailEnum.LACK_REFUND.getCode());
        afterSaleInfoDO.setApplySource(AfterSaleApplySourceEnum.SYSTEM.getCode());
        afterSaleInfoDO.setAfterSaleStatus(AfterSaleStatusEnum.REVIEW_PASS.getCode());
        afterSaleInfoDO.setApplyTime(new Date());
        afterSaleInfoDO.setReviewTime(new Date());

        return afterSaleInfoDO;

    }

    /**
     * 构造缺品售后单item
     *
     * @param order
     * @return
     */
    private AfterSaleItemDO buildLackAfterSaleItem(OrderInfoDO order, AfterSaleInfoDO lackAfterSale
            , LackItemDTO lackItemDTO) {
        Integer lackNum = lackItemDTO.getLackNum();
        ProductSkuDTO productSku = lackItemDTO.getProductSku();
        OrderItemDO orderItem = lackItemDTO.getOrderItem();

        AfterSaleItemDO afterSaleItemDO = new AfterSaleItemDO();
        afterSaleItemDO.setAfterSaleId(lackAfterSale.getAfterSaleId());
        afterSaleItemDO.setOrderId(order.getOrderId());
        afterSaleItemDO.setProductName(productSku.getProductName());
        afterSaleItemDO.setSkuCode(productSku.getSkuCode());
        afterSaleItemDO.setReturnQuantity(lackNum);
        afterSaleItemDO.setProductImg(orderItem.getProductImg());
        afterSaleItemDO.setOriginAmount(orderItem.getOriginAmount());
        //计算sku缺品退款金额
        afterSaleItemDO.setApplyRefundAmount(orderItem.getSalePrice() * lackNum);
        afterSaleItemDO.setRealRefundAmount(afterSaleAmountService.calculateOrderItemLackRealRefundAmount(orderItem, lackNum));
        return afterSaleItemDO;
    }

    /**
     * 构造缺品售后支付单
     *
     * @param order
     * @return
     */
    private AfterSaleRefundDO buildLackAfterSaleRefundDO(OrderInfoDO order, AfterSaleInfoDO afterSaleInfo) {

        //构造售后单
        AfterSaleRefundDO AfterSaleRefundDO = new AfterSaleRefundDO();
        AfterSaleRefundDO.setAfterSaleId(String.valueOf(afterSaleInfo.getAfterSaleId()));
        AfterSaleRefundDO.setOrderId(order.getOrderId());
        AfterSaleRefundDO.setAccountType(AccountTypeEnum.THIRD.getCode());
        AfterSaleRefundDO.setPayType(order.getPayType());
        AfterSaleRefundDO.setRefundAmount(afterSaleInfo.getRealRefundAmount());
        AfterSaleRefundDO.setRefundStatus(RefundStatusEnum.UN_REFUND.getCode());

        return AfterSaleRefundDO;

    }

    /**
     * 校验缺品项
     *
     * @return
     */
    private LackItemDTO checkLackItem(OrderInfoDO order, List<OrderItemDO> orderItems, LackItemRequest request) {
        String skuCode = request.getSkuCode();
        Integer lackNum = request.getLackNum();

        //1、参数校验
        ParamCheckUtil.checkStringNonEmpty(skuCode, OrderErrorCodeEnum.SKU_CODE_IS_NULL);
        ParamCheckUtil.checkIntMin(lackNum, 1, OrderErrorCodeEnum.LACK_NUM_IS_LT_0);

        //2、查询商品sku
        String lockSkuCode = request.getSkuCode();

        ProductSkuDTO productSkuDTO = productRemote.getProductSku(skuCode, order.getSellerId());
        ParamCheckUtil.checkObjectNonNull(productSkuDTO, OrderErrorCodeEnum.PRODUCT_SKU_CODE_ERROR, lockSkuCode);

        //3、找到item中对应的缺品sku item
        OrderItemDO lackItemDO = orderItems.stream().filter(item -> item.getSkuCode().equals(lockSkuCode))
                .findFirst().orElse(null);
        ParamCheckUtil.checkObjectNonNull(lackItemDO, OrderErrorCodeEnum.LACK_ITEM_NOT_IN_ORDER, lockSkuCode);

        //4、缺品商品数量不能>=下单商品数量
        if (lackItemDO.getSaleQuantity() <= request.getLackNum()) {
            throw new OrderBizException(OrderErrorCodeEnum.LACK_NUM_IS_GE_SKU_ORDER_ITEM_SIZE);
        }

        //5、构造返参
        return new LackItemDTO(lackItemDO, lackNum, productSkuDTO);
    }

}
