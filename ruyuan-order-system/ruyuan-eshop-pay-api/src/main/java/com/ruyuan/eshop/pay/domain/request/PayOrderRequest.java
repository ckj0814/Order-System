package com.ruyuan.eshop.pay.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class PayOrderRequest implements Serializable {

    private static final long serialVersionUID = 3125438300418569860L;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 订单摘要
     */
    private String subject;

    /**
     * 商品明细 json
     */
    private String itemInfo;

}