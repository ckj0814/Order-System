package com.ruyuan.eshop.product.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class ProductSkuVO implements Serializable {

    /**
     * 商品编号
     */
    private Integer productId;

    /**
     * 商品类型 1:普通商品,2:预售商品
     */
    private Integer productType;

    /**
     * 商品sku编号
     */
    private String skuCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片
     */
    private String productImg;

    /**
     * 商品单位
     */
    private String productUnit;

    /**
     * 商品销售价格
     */
    private Integer salePrice;

    /**
     * 商品采购价格
     */
    private Integer purchasePrice;

}