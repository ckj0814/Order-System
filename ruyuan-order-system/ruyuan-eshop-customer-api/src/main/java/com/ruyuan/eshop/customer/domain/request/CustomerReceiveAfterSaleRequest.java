package com.ruyuan.eshop.customer.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 客服接收订单系统的售后申请入参
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class CustomerReceiveAfterSaleRequest implements Serializable {

    private static final long serialVersionUID = 5541950604615013941L;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 售后id
     */
    private String afterSaleId;

    /**
     * 售后支付单id
     */
    private Long afterSaleRefundId;

    /**
     * 售后类型 1 退款  2 退货
     */
    private Integer afterSaleType;

    /**
     * 实际退款金额
     */
    private Integer returnGoodAmount;

    /**
     * 申请退款金额
     */
    private Integer applyRefundAmount;

}