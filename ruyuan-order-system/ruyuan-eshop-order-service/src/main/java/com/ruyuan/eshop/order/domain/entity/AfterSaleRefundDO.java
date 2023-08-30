package com.ruyuan.eshop.order.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 售后退款单表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("after_sale_refund")
public class AfterSaleRefundDO implements Serializable {

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
     * 售后单号
     */
    private String afterSaleId;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 售后批次号
     */
    private String afterSaleBatchNo;

    /**
     * 账户类型
     */
    private Integer accountType;

    /**
     * 支付类型
     */
    private Integer payType;

    /**
     * 退款状态
     */
    private Integer refundStatus;

    /**
     * 退款金额
     */
    private Integer refundAmount;

    /**
     * 退款支付时间
     */
    private Date refundPayTime;

    /**
     * 交易单号
     */
    private String outTradeNo;

    /**
     * 备注
     */
    private String remark;

}
