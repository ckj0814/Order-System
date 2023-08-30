package com.ruyuan.eshop.order.service.impl;

import com.ruyuan.eshop.order.builder.FullOrderData;
import com.ruyuan.eshop.order.domain.entity.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 新订单信息数据（包含主订单与子订单）
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class NewOrderDataHolder {

    /**
     * 订单信息
     */
    private List<OrderInfoDO> orderInfoDOList = new ArrayList<>();

    /**
     * 订单条目
     */
    private List<OrderItemDO> orderItemDOList = new ArrayList<>();

    /**
     * 订单配送信息
     */
    private List<OrderDeliveryDetailDO> orderDeliveryDetailDOList = new ArrayList<>();

    /**
     * 订单支付信息
     */
    private List<OrderPaymentDetailDO> orderPaymentDetailDOList = new ArrayList<>();

    /**
     * 订单费用信息
     */
    private List<OrderAmountDO> orderAmountDOList = new ArrayList<>();

    /**
     * 订单费用明细
     */
    private List<OrderAmountDetailDO> orderAmountDetailDOList = new ArrayList<>();

    /**
     * 订单状态变更日志信息
     */
    private List<OrderOperateLogDO> orderOperateLogDOList = new ArrayList<>();

    /**
     * 订单快照数据
     */
    private List<OrderSnapshotDO> orderSnapshotDOList = new ArrayList<>();


    /**
     * 追加订单数据
     */
    public void appendOrderData(FullOrderData fullOrderData) {
        this.getOrderInfoDOList().add(fullOrderData.getOrderInfoDO());
        this.getOrderItemDOList().addAll(fullOrderData.getOrderItemDOList());
        this.getOrderDeliveryDetailDOList().add(fullOrderData.getOrderDeliveryDetailDO());
        this.getOrderPaymentDetailDOList().addAll(fullOrderData.getOrderPaymentDetailDOList());
        this.getOrderAmountDOList().addAll(fullOrderData.getOrderAmountDOList());
        this.getOrderAmountDetailDOList().addAll(fullOrderData.getOrderAmountDetailDOList());
        this.getOrderOperateLogDOList().add(fullOrderData.getOrderOperateLogDO());
        this.getOrderSnapshotDOList().addAll(fullOrderData.getOrderSnapshotDOList());
    }

}