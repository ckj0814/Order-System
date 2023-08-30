package com.ruyuan.eshop.fulfill.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单履约条目
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("order_fulfill_item")
public class OrderFulfillItemDO implements Serializable {

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
     * 履约单ID
     */
    private String fulfillId;

    /**
     * 商品id
     */
    private String skuCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 销售单价
     */
    private Integer salePrice;

    /**
     * 销售数量
     */
    private Integer saleQuantity;

    /**
     * 商品单位
     */
    private String productUnit;

    /**
     * 付款金额
     */
    private Integer payAmount;

    /**
     * 当前商品支付原总价
     */
    private Integer originAmount;
}
