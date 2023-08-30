package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单售后DTO
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class AfterSaleInfoDTO implements Serializable {

    /**
     * 售后id
     */
    private Long afterSaleId;

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
     * 订单实付金额
     */
    private Integer orderAmount;

    /**
     * 订单状态
     */
    private Integer orderStatus;

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
     * 客服审核时间
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
     * 售后类型：1、退货 2、退款
     */
    private Integer afterSaleType;

    /**
     * 售后类型详情枚举
     */
    private Integer afterSaleTypeDetail;

    /**
     * 备注
     */
    private String remark;

}
