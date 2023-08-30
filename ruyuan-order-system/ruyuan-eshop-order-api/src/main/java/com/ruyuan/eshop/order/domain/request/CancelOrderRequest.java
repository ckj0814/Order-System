package com.ruyuan.eshop.order.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 取消订单入参
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class CancelOrderRequest implements Serializable {

    private static final long serialVersionUID = -4035579903997700971L;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 订单渠道来源
     */
    private Integer businessIdentifier;

    /**
     * 订单取消类型 0-手动取消 1-超时未支付
     */
    private Integer cancelType;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 原订单状态
     */
    private Integer oldOrderStatus;

}
