package com.ruyuan.eshop.inventory.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 扣减库存DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeductStockDTO {
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 商品skuCode
     */
    private String skuCode;

    /**
     * 销售数量
     */
    private Integer saleQuantity;

    /**
     * 原始销售库存
     */
    private Integer originSaleStock;

    /**
     * 原始已销售库存
     */
    private Integer originSaledStock;
}
