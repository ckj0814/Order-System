package com.ruyuan.eshop.order.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单支付明细表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("order_payment_detail")
public class OrderPaymentDetailDO implements Serializable {

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
     * 账户类型
     */
    private Integer accountType;

    /**
     * 支付类型  10:微信支付, 20:支付宝支付
     */
    private Integer payType;

    /**
     * 支付状态 10:未支付,20:已支付
     */
    private Integer payStatus;

    /**
     * 支付金额
     */
    private Integer payAmount;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付流水号
     */
    private String outTradeNo;

    /**
     * 支付备注信息
     */
    private String payRemark;
}
