package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 售后单列表DTO
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class AfterSaleOrderListDTO implements Serializable {

    /**
     * 接入方业务标识
     */
    private Integer businessIdentifier;
    /**
     * 售后单号
     */
    private String afterSaleId;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 买家编号
     */
    private String userId;
    /**
     * 订单实付金额
     */
    private Integer orderAmount;
    /**
     * 售后单状态
     */
    private Integer afterSaleStatus;
    /**
     * 申请售后来源
     */
    private Integer applySource;
    /**
     * 申请售后时间
     */
    private Date applyTime;
    /**
     * 申请理由编码
     */
    private Integer applyReasonCode;
    /**
     * 申请理由描述
     */
    private String applyReason;

    /**
     * 客服审核时间
     */
    private Date reviewTime;
    /**
     * 客服审核结果编码
     */
    private Long reviewReasonCode;
    /**
     * 客服审核结果
     */
    private String reviewReason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 售后类型
     */
    private Integer afterSaleType;

    /**
     * 售后类型详情
     */
    private Integer afterSaleTypeDetail;

    // after_sale_detail

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
