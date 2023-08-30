package com.ruyuan.eshop.market.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 释放优惠券入参
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class ReleaseUserCouponRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 优惠券ID
     */
    private String couponId;

    /**
     * 订单ID
     */
    private String orderId;

}