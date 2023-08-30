package com.ruyuan.eshop.pay.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 支付服务的退款入参
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class PayRefundRequest implements Serializable {
    private static final long serialVersionUID = -8167173288717203089L;

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
    private Long afterSaleId;

    /**
     * 交易流水号
     */
    private String outTradeNo;

}