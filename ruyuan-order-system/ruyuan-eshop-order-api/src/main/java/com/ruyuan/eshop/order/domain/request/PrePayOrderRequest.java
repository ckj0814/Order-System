package com.ruyuan.eshop.order.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class PrePayOrderRequest implements Serializable {

    private static final long serialVersionUID = -634137320435888212L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 业务方标识
     */
    private String businessIdentifier;

    /**
     * 支付类型
     */
    private Integer payType;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 订单支付金额
     */
    private Integer payAmount;

    /**
     * 支付成功后跳转地址
     */
    private String callbackUrl;

    /**
     * 支付失败跳转地址
     */
    private String callbackFailUrl;

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 订单摘要
     */
    private String subject;

    /**
     * 商品明细 json
     */
    private String itemInfo;

}