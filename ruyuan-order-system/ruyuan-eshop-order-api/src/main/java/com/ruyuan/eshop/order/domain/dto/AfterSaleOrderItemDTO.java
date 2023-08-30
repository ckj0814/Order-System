package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class AfterSaleOrderItemDTO implements Serializable {

    /**
     * 售后id
     */
    private Long afterSaleId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * sku code
     */
    private String skuCode;

    /**
     * 商品图片地址
     */
    private String productImg;

    /**
     * 商品退货数量
     */
    private Integer returnQuantity;

    /**
     * 商品总金额
     */
    private Integer originAmount;

    /**
     * 申请退款金额
     */
    private Integer applyRefundAmount;

    /**
     * 实际退款金额
     */
    private Integer realRefundAmount;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 商品类型
     */
    private Integer itemType;

    /**
     * 商品退货数量
     */
    private Integer itemNum;

    /**
     * 商品图片地址
     */
    private String itemImg;
}
