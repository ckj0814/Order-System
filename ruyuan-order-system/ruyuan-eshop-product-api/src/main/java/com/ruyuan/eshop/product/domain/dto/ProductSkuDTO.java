package com.ruyuan.eshop.product.domain.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * 商品sku信息
 *
 * @author zhonghuashishan
 */
@Data
public class ProductSkuDTO implements Serializable {

    private static final long serialVersionUID = 3085701132845075839L;

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
