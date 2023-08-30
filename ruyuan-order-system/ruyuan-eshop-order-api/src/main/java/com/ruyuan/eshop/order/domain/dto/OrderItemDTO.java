package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 订单条目DTO
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class OrderItemDTO implements Serializable {

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 订单明细编号
     */
    private String orderItemId;

    /**
     * 商品类型 1:普通商品,2:预售商品
     */
    private Integer productType;

    /**
     * 商品编号
     */
    private String productId;

    /**
     * 商品图片
     */
    private String productImg;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * 销售数量
     */
    private Integer saleQuantity;

    /**
     * 销售单价
     */
    private Integer salePrice;

    /**
     * 当前商品支付原总价
     */
    private Integer originAmount;

    /**
     * 交易支付金额
     */
    private Integer payAmount;

    /**
     * 商品单位
     */
    private String productUnit;

    /**
     * 采购成本价
     */
    private Integer purchasePrice;

    /**
     * 卖家编号
     */
    private String sellerId;

}
