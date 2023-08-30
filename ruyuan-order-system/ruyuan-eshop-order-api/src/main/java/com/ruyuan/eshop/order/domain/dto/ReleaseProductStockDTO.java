package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class ReleaseProductStockDTO implements Serializable {
    private static final long serialVersionUID = 4202385798844939307L;

    /**
     * 订单ID
     */
    private String orderId;

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