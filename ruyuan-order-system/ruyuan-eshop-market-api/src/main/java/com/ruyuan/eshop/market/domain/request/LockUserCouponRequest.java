package com.ruyuan.eshop.market.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 锁定使用优惠券入参
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class LockUserCouponRequest implements Serializable {

    private static final long serialVersionUID = 8135282200466056333L;

    /**
     * 业务线标识
     */
    private Integer businessIdentifier;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 卖家ID
     */
    private String sellerId;

    /**
     * 优惠券ID
     */
    private String couponId;

}