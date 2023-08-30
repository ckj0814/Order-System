package com.ruyuan.eshop.order.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单售后详情表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("after_sale_item")
public class AfterSaleItemDO implements Serializable {
    private static final long serialVersionUID = -908961610293643135L;

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
     * 商品名
     */
    private String productName;

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

}
