package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单信息DTO
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class OrderInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 接入方业务线标识  1, "自营商城"
     */
    private Integer businessIdentifier;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 父订单编号
     */
    private String parentOrderId;

    /**
     * 接入方订单号
     */
    private String businessOrderId;

    /**
     * 订单类型 1:一般订单  255:其它
     */
    private Integer orderType;

    /**
     * 订单状态 10:已创建, 30:已履约, 40:出库, 50:配送中, 60:已签收, 70:已取消, 100:已拒收, 255:无效订单
     */
    private Integer orderStatus;

    /**
     * 订单取消类型
     */
    private String cancelType;

    /**
     * 订单取消时间
     */
    private Date cancelTime;

    /**
     * 卖家编号
     */
    private String sellerId;

    /**
     * 买家编号
     */
    private String userId;

    /**
     * 交易总金额（以分为单位存储）
     */
    private Integer totalAmount;

    /**
     * 交易支付金额
     */
    private Integer payAmount;

    /**
     * 交易支付方式
     */
    private Integer payType;

    /**
     * 使用的优惠券编号
     */
    private String couponId;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付订单截止时间
     */
    private Date expireTime;

    /**
     * 用户备注
     */
    private String userRemark;

    /**
     * 订单删除状态 0:未删除  1:已删除
     */
    private Integer deleteStatus;

    /**
     * 订单评论状态 0:未发表评论  1:已发表评论
     */
    private Integer commentStatus;

    /**
     * 缺品标识，0:未缺品，1:缺品
     */
    private Integer lackFlag;

    /**
     * 原订单状态
     */
    private Integer oldOrderStatus;
}