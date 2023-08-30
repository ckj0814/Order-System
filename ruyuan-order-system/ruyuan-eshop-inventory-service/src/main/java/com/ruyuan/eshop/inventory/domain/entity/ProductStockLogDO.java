package com.ruyuan.eshop.inventory.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 库存扣减日志表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("product_stock_log")
public class ProductStockLogDO implements Serializable {

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
     * 订单ID
     */
    private String orderId;

    /**
     * 商品sku编号
     */
    private String skuCode;

    /**
     * 原始销售库存
     */
    private Long originSaleStockQuantity;

    /**
     * 原始已销售库存
     */
    private Long originSaledStockQuantity;

    /**
     * 扣减后的销售库存
     */
    private Long deductedSaleStockQuantity;

    /**
     * 增加的已销售库存
     */
    private Long increasedSaledStockQuantity;

    /**
     * 状态：1-已扣减；2-已释放
     */
    private Integer status;
}
