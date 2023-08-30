package com.ruyuan.eshop.inventory.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 锁定商品库存入参
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class DeductProductStockRequest implements Serializable {

    private static final long serialVersionUID = 8229493558996271243L;

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
     * 订单条目
     */
    private List<OrderItemRequest> orderItemRequestList;

    @Data
    public static class OrderItemRequest implements Serializable {

        private static final long serialVersionUID = 6870559288334853954L;

        /**
         * 商品sku编号
         */
        private String skuCode;

        /**
         * 销售数量
         */
        private Integer saleQuantity;

    }

}