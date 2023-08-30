package com.ruyuan.eshop.order.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付系统退款回调入参
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class RefundCallbackRequest implements Serializable {
    private static final long serialVersionUID = -5258136513569055820L;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 退款批次号
     */
    private String batchNo;
    /**
     * 支付接口返回来的退款结果状态 10 未退款  20 退款成功 30 退款失败
     */
    private Integer refundStatus;
    /**
     * 退款费用
     */
    private Integer refundFee;
    /**
     * 退款总额
     */
    private Integer totalFee;
    /**
     * 支付退款签名
     */
    private String sign;
    /**
     * 交易流水号
     */
    private String tradeNo;
    /**
     * 支付退款回调时间
     */
    private Date refundTime;
    /**
     * 订单售后id
     */
    private String afterSaleId;

}
