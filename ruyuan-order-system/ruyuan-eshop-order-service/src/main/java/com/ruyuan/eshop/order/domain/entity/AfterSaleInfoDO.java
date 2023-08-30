package com.ruyuan.eshop.order.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单售后表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("after_sale_info")
public class AfterSaleInfoDO implements Serializable {

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
     * 售后id
     */
    private Long afterSaleId;

    /**
     * 接入方业务标识
     */
    private Integer businessIdentifier;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 订单来源渠道
     */
    private Integer orderSourceChannel;

    /**
     * 购买用户id
     */
    private String userId;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 申请售后来源
     */
    private Integer applySource;

    /**
     * 申请售后时间
     */
    private Date applyTime;

    /**
     * 申请原因编码
     */
    private Integer applyReasonCode;

    /**
     * 申请原因
     */
    private String applyReason;

    /**
     * 审核时间
     */
    private Date reviewTime;

    /**
     * 客服审核来源
     */
    private Integer reviewSource;

    /**
     * 客服审核结果编码
     */
    private Integer reviewReasonCode;

    /**
     * 客服审核结果
     */
    private String reviewReason;

    /**
     * 售后类型
     */
    private Integer afterSaleType;

    /**
     * 售后类型详情枚举
     */
    private Integer afterSaleTypeDetail;

    /**
     * 售后单状态
     */
    private Integer afterSaleStatus;

    /**
     * 申请退款金额
     */
    private Integer applyRefundAmount;

    /**
     * 实际退款金额
     */
    private Integer realRefundAmount;

    /**
     * 备注
     */
    private String remark;


}
