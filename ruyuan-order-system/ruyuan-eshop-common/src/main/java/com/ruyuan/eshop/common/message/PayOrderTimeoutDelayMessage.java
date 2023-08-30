package com.ruyuan.eshop.common.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单支付超时自定取消订单延迟消息
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class PayOrderTimeoutDelayMessage implements Serializable {

    private static final long serialVersionUID = 2575864833116171389L;

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

}