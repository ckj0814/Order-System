package com.ruyuan.eshop.order.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 实际退款入参
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class RefundRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 订单号
     */
    public String orderId;

    /**
     * 实际退款金额
     */
    public Integer refundAmount;

    /**
     * 售后单号
     */
    private String afterSaleId;

    /**
     * 申请退款金额
     */
    private Integer applyRefundAmount;

}
