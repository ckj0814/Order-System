package com.ruyuan.eshop.order.builder;

import com.ruyuan.eshop.order.domain.entity.*;
import lombok.Data;

import java.util.List;

/**
 * 全量的订单基础数据
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class FullOrderData {

    /**
     * 订单信息
     */
    private OrderInfoDO orderInfoDO;

    /**
     * 订单条目
     */
    private List<OrderItemDO> orderItemDOList;

    /**
     * 订单配送信息
     */
    private OrderDeliveryDetailDO orderDeliveryDetailDO;

    /**
     * 订单支付信息
     */
    private List<OrderPaymentDetailDO> orderPaymentDetailDOList;

    /**
     * 订单费用信息
     */
    private List<OrderAmountDO> orderAmountDOList;

    /**
     * 订单费用明细
     */
    private List<OrderAmountDetailDO> orderAmountDetailDOList;

    /**
     * 订单状态变更日志信息
     */
    private OrderOperateLogDO orderOperateLogDO;

    /**
     * 订单快照数据
     */
    private List<OrderSnapshotDO> orderSnapshotDOList;

}