package com.ruyuan.eshop.market.message;

import lombok.Data;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class MarketMessage {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 优惠券id
     */
    private String couponId;
}
