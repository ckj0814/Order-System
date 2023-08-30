package com.ruyuan.eshop.order.manager.impl;

import com.ruyuan.eshop.common.enums.AfterSaleTypeDetailEnum;
import com.ruyuan.eshop.common.enums.AfterSaleTypeEnum;
import com.ruyuan.eshop.common.enums.OrderOperateTypeEnum;
import com.ruyuan.eshop.common.enums.OrderStatusEnum;
import com.ruyuan.eshop.common.utils.RandomUtil;
import com.ruyuan.eshop.fulfill.domain.request.CancelFulfillRequest;
import com.ruyuan.eshop.order.converter.OrderConverter;
import com.ruyuan.eshop.order.dao.*;
import com.ruyuan.eshop.order.domain.dto.OrderInfoDTO;
import com.ruyuan.eshop.order.domain.dto.OrderItemDTO;
import com.ruyuan.eshop.order.domain.entity.*;
import com.ruyuan.eshop.order.domain.request.CancelOrderAssembleRequest;
import com.ruyuan.eshop.order.enums.*;
import com.ruyuan.eshop.order.remote.FulfillRemote;
import com.ruyuan.eshop.order.manager.AfterSaleManager;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
@Slf4j
public class AfterSaleManagerImpl implements AfterSaleManager {

    @Autowired
    private FulfillRemote fulfillRemote;

    @Autowired
    private OrderInfoDAO orderInfoDAO;

    @Autowired
    private OrderOperateLogDAO orderOperateLogDAO;

    @Autowired
    private AfterSaleInfoDAO afterSaleInfoDAO;

    @Autowired
    private AfterSaleItemDAO afterSaleItemDAO;

    @Autowired
    private AfterSaleLogDAO afterSaleLogDAO;

    @Autowired
    private OrderPaymentDetailDAO orderPaymentDetailDAO;

    @Autowired
    private AfterSaleRefundDAO afterSaleRefundDAO;

    @Autowired
    private OrderConverter orderConverter;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void cancelOrderFulfillmentAndUpdateOrderStatus(CancelOrderAssembleRequest cancelOrderAssembleRequest) {
        // 履约取消
        cancelFulfill(cancelOrderAssembleRequest);
        // 更新订单状态和记录订单操作日志
        updateOrderStatusAndSaveOperationLog(cancelOrderAssembleRequest);
    }

    /**
     * 调用履约拦截订单
     */
    private void cancelFulfill(CancelOrderAssembleRequest cancelOrderAssembleRequest) {
        OrderInfoDTO orderInfoDTO = cancelOrderAssembleRequest.getOrderInfoDTO();
        if (OrderStatusEnum.CREATED.getCode().equals(orderInfoDTO.getOrderStatus())) {
            return;
        }
        CancelFulfillRequest cancelFulfillRequest = orderConverter.convertCancelFulfillRequest(orderInfoDTO);
        fulfillRemote.cancelFulfill(cancelFulfillRequest);
    }

    /**
     * 更新订单状态和记录订单操作日志
     */
    private void updateOrderStatusAndSaveOperationLog(CancelOrderAssembleRequest cancelOrderAssembleRequest) {
        //  更新订单表
        OrderInfoDO orderInfoDO = orderConverter.orderInfoDTO2DO(cancelOrderAssembleRequest.getOrderInfoDTO());
        orderInfoDO.setCancelType(cancelOrderAssembleRequest.getCancelType().toString());
        orderInfoDO.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        orderInfoDO.setCancelTime(new Date());
        orderInfoDAO.updateOrderInfo(orderInfoDO);
        log.info("更新订单信息OrderInfo状态: orderId:{},status:{}", orderInfoDO.getOrderId(), orderInfoDO.getOrderStatus());

        //  新增订单操作操作日志表
        Integer cancelType = Integer.valueOf(orderInfoDO.getCancelType());
        String orderId = orderInfoDO.getOrderId();
        OrderOperateLogDO orderOperateLogDO = new OrderOperateLogDO();
        orderOperateLogDO.setOrderId(orderId);
        orderOperateLogDO.setPreStatus(cancelOrderAssembleRequest.getOrderInfoDTO().getOrderStatus());
        orderOperateLogDO.setCurrentStatus(OrderStatusEnum.CANCELED.getCode());
        orderOperateLogDO.setOperateType(OrderOperateTypeEnum.AUTO_CANCEL_ORDER.getCode());

        if (OrderCancelTypeEnum.USER_CANCELED.getCode().equals(cancelType)) {
            orderOperateLogDO.setOperateType(OrderOperateTypeEnum.MANUAL_CANCEL_ORDER.getCode());
            orderOperateLogDO.setRemark(OrderOperateTypeEnum.MANUAL_CANCEL_ORDER.getMsg()
                    + orderOperateLogDO.getPreStatus() + "-" + orderOperateLogDO.getCurrentStatus());
        }
        if (OrderCancelTypeEnum.TIMEOUT_CANCELED.getCode().equals(cancelType)) {
            orderOperateLogDO.setOperateType(OrderOperateTypeEnum.AUTO_CANCEL_ORDER.getCode());
            orderOperateLogDO.setRemark(OrderOperateTypeEnum.AUTO_CANCEL_ORDER.getMsg()
                    + orderOperateLogDO.getPreStatus() + "-" + orderOperateLogDO.getCurrentStatus());
        }
        orderOperateLogDAO.save(orderOperateLogDO);
        log.info("新增订单操作日志OrderOperateLog状态,orderId:{}, PreStatus:{},CurrentStatus:{}", orderInfoDO.getOrderId(),
                orderOperateLogDO.getPreStatus(), orderOperateLogDO.getCurrentStatus());
    }

    /**
     * 取消订单操作 记录售后信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertCancelOrderAfterSale(CancelOrderAssembleRequest cancelOrderAssembleRequest, Integer afterSaleStatus,
                                           OrderInfoDO orderInfoDO, String afterSaleId) {
        OrderInfoDTO orderInfoDTO = cancelOrderAssembleRequest.getOrderInfoDTO();
        //  取消订单过程中的 申请退款金额 和 实际退款金额 都是实付退款金额 金额相同
        AfterSaleInfoDO afterSaleInfoDO = new AfterSaleInfoDO();
        afterSaleInfoDO.setApplyRefundAmount(orderInfoDO.getPayAmount());
        afterSaleInfoDO.setRealRefundAmount(orderInfoDO.getPayAmount());

        //  1、新增售后订单表
        Integer cancelOrderAfterSaleStatus = AfterSaleStatusEnum.REVIEW_PASS.getCode();
        insertCancelOrderAfterSaleInfoTable(orderInfoDO, cancelOrderAfterSaleStatus, afterSaleInfoDO, afterSaleId);
        cancelOrderAssembleRequest.setAfterSaleId(afterSaleId);

        //  2、新增售后条目表
        String orderId = cancelOrderAssembleRequest.getOrderId();
        List<OrderItemDTO> orderItemDTOList = cancelOrderAssembleRequest.getOrderItemDTOList();
        insertAfterSaleItemTable(orderId, orderItemDTOList, afterSaleId);

        //  3、新增售后变更表
        insertCancelOrderAfterSaleLogTable(afterSaleId, orderInfoDTO, AfterSaleStatusEnum.UN_CREATED.getCode(), afterSaleStatus);

        //  4、新增售后支付表
        AfterSaleRefundDO afterSaleRefundDO = insertAfterSaleRefundTable(orderInfoDTO, afterSaleId, afterSaleInfoDO);
        cancelOrderAssembleRequest.setAfterSaleRefundId(afterSaleRefundDO.getId());
    }

    private void insertCancelOrderAfterSaleInfoTable(OrderInfoDO orderInfoDO, Integer cancelOrderAfterSaleStatus,
                                                     AfterSaleInfoDO afterSaleInfoDO, String afterSaleId) {
        afterSaleInfoDO.setAfterSaleId(Long.valueOf(afterSaleId));
        afterSaleInfoDO.setBusinessIdentifier(BusinessIdentifierEnum.SELF_MALL.getCode());
        afterSaleInfoDO.setOrderId(orderInfoDO.getOrderId());
        afterSaleInfoDO.setOrderSourceChannel(BusinessIdentifierEnum.SELF_MALL.getCode());
        afterSaleInfoDO.setUserId(orderInfoDO.getUserId());
        afterSaleInfoDO.setOrderType(OrderTypeEnum.NORMAL.getCode());
        afterSaleInfoDO.setApplyTime(new Date());
        afterSaleInfoDO.setAfterSaleStatus(cancelOrderAfterSaleStatus);

        afterSaleInfoDO.setRealRefundAmount(afterSaleInfoDO.getRealRefundAmount());
        afterSaleInfoDO.setApplyRefundAmount(afterSaleInfoDO.getApplyRefundAmount());
        //  取消订单 整笔退款
        afterSaleInfoDO.setAfterSaleType(AfterSaleTypeEnum.RETURN_MONEY.getCode());

        Integer cancelType = Integer.valueOf(orderInfoDO.getCancelType());
        if (OrderCancelTypeEnum.TIMEOUT_CANCELED.getCode().equals(cancelType)) {
            afterSaleInfoDO.setAfterSaleTypeDetail(AfterSaleTypeDetailEnum.TIMEOUT_NO_PAY.getCode());
            afterSaleInfoDO.setRemark("超时未支付自动取消");
        }
        if (OrderCancelTypeEnum.USER_CANCELED.getCode().equals(cancelType)) {
            afterSaleInfoDO.setAfterSaleTypeDetail(AfterSaleTypeDetailEnum.USER_CANCEL.getCode());
            afterSaleInfoDO.setRemark("用户手动取消");
        }
        afterSaleInfoDO.setApplyReasonCode(AfterSaleReasonEnum.CANCEL.getCode());
        afterSaleInfoDO.setApplyReason(AfterSaleReasonEnum.CANCEL.getMsg());
        afterSaleInfoDO.setApplySource(AfterSaleApplySourceEnum.SYSTEM.getCode());
        afterSaleInfoDO.setReviewTime(new Date());

        afterSaleInfoDAO.save(afterSaleInfoDO);

        log.info("新增订单售后记录,订单号:{},售后单号:{},订单售后状态:{}", afterSaleInfoDO.getOrderId(),
                afterSaleInfoDO.getAfterSaleId(), afterSaleInfoDO.getAfterSaleStatus());
    }

    private void insertAfterSaleItemTable(String orderId, List<OrderItemDTO> orderItemDTOList, String afterSaleId) {
        List<AfterSaleItemDO> itemDOList = new ArrayList<>();
        orderItemDTOList.forEach(orderItem -> {
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
            itemDOList.add(afterSaleItemDO);
        });
        afterSaleItemDAO.saveBatch(itemDOList);
    }

    private void insertCancelOrderAfterSaleLogTable(String afterSaleId, OrderInfoDTO orderInfoDTO,
                                                    Integer preAfterSaleStatus, Integer currentAfterSaleStatus) {

        AfterSaleLogDO afterSaleLogDO = new AfterSaleLogDO();
        afterSaleLogDO.setAfterSaleId(afterSaleId);
        afterSaleLogDO.setPreStatus(preAfterSaleStatus);
        afterSaleLogDO.setCurrentStatus(currentAfterSaleStatus);

        //  取消订单的业务值
        Integer cancelType = Integer.valueOf(orderInfoDTO.getCancelType());
        if (OrderCancelTypeEnum.USER_CANCELED.getCode().equals(cancelType)) {
            afterSaleLogDO.setRemark(OrderCancelTypeEnum.USER_CANCELED.getMsg());
        }
        if (OrderCancelTypeEnum.TIMEOUT_CANCELED.getCode().equals(cancelType)) {
            afterSaleLogDO.setRemark(OrderCancelTypeEnum.TIMEOUT_CANCELED.getMsg());
        }

        afterSaleLogDAO.save(afterSaleLogDO);
        log.info("新增售后单变更信息, 订单号:{},售后单号:{},状态:PreStatus{},CurrentStatus:{}", orderInfoDTO.getOrderId(),
                afterSaleId, afterSaleLogDO.getPreStatus(), afterSaleLogDO.getCurrentStatus());
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
}
