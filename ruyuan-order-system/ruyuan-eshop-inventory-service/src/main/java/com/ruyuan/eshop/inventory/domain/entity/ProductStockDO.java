package com.ruyuan.eshop.inventory.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 库存中心的商品库存表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("inventory_product_stock")
public class ProductStockDO implements Serializable {

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
     * 商品sku编号
     */
    private String skuCode;

    /**
     * 销售库存
     */
    private Long saleStockQuantity;

    /**
     * 已销售库存
     */
    private Long saledStockQuantity;
}
