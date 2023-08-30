package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 取消订单 退款金额 DTO
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class CancelOrderRefundAmountDTO implements Serializable {
    private static final long serialVersionUID = 2078305514048894973L;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 交易总金额
     */
    private Integer totalAmount;
    /**
     * 实际退款金额
     */
    private Integer returnGoodAmount;


    /**
     * 申请退款金额
     */
    private Integer applyRefundAmount;

    /**
     * sku编号
     */
    private String skuCode;
    /**
     * 退货数量
     */
    private Integer returnNum;


}
