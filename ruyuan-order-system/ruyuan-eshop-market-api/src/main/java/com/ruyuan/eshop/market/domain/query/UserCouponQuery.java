package com.ruyuan.eshop.market.domain.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class UserCouponQuery implements Serializable {


    private static final long serialVersionUID = -7687082779183203277L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 优惠券ID
     */
    private String couponId;

}