package com.ruyuan.eshop.order.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单条目表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("order_item")
public class OrderItemDO implements Serializable {

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
     * 卖家ID
     */
    private String sellerId;
}
