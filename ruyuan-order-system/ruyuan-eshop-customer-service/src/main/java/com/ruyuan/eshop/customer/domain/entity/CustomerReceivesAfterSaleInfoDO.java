package com.ruyuan.eshop.customer.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 客服接收售后申请信息表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("customer_receives_after_sales_info")
public class CustomerReceivesAfterSaleInfoDO implements Serializable {
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
     * 用户id
     */
    private String userId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 售后id
     */
    private String afterSaleId;

    /**
     * 售后支付单id
     */
    private Long afterSaleRefundId;

    /**
     * 售后类型 1 退款  2 退货
     */
    private Integer afterSaleType;

    /**
     * 实际退款金额
     */
    private Integer returnGoodAmount;

    /**
     * 申请退款金额
     */
    private Integer applyRefundAmount;
}
