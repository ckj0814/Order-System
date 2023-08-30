package com.ruyuan.eshop.product.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商品sku记录表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("product_sku")
public class ProductSkuDO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
    /**
     * 商品编号
     */
    private String productId;

    /**
     * 商品类型 1:普通商品,2:预售商品
     */
    private Integer productType;

    /**
     * 商品SKU编码
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
