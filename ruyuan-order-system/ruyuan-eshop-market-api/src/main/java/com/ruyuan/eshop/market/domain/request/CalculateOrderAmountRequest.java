package com.ruyuan.eshop.market.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class CalculateOrderAmountRequest implements Serializable {

    private static final long serialVersionUID = 7867481234204617257L;

    /**
     * 订单号
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

    /**
     * 区域ID
     */
    private String regionId;

    /**
     * 订单条目信息
     */
    private List<OrderItemRequest> orderItemRequestList;

    /**
     * 订单费用信息
     */
    private List<OrderAmountRequest> orderAmountRequestList;

    /**
     * 订单条目信息
     */
    @Data
    public static class OrderItemRequest implements Serializable {

        private static final long serialVersionUID = 752390422733973833L;

        /**
         * 商品编号
         */
        private String productId;

        /**
         * 商品类型 1:普通商品,2:预售商品
         */
        private Integer productType;

        /**
         * 商品sku编号
         */
        private String skuCode;

        /**
         * 商品销售价格
         */
        private Integer salePrice;

        /**
         * 销售数量
         */
        private Integer saleQuantity;

    }

    /**
     * 订单费用信息
     */
    @Data
    public static class OrderAmountRequest implements Serializable {

        private static final long serialVersionUID = 3724180842774045151L;

        /**
         * 费用类型
         */
        private Integer amountType;

        /**
         * 费用金额
         */
        private Integer amount;

    }

}