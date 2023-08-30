package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 订单DTO
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class OrderInfoDTO {

    /**
     * 接入方业务线标识  1, "自营商城"
     */
    private Integer businessIdentifier;

    /**
     * 主键id
     */
    private Long id;

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
     * 订单类型
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
     * 支付方式
     */
    private Integer payType;

    /**
     * 交易总金额（以分为单位存储）
     */
    private Integer totalAmount;

    /**
     * 交易支付金额
     */
    private Integer payAmount;

    /**
     * 使用的优惠券编号
     */
    private String couponId;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 订单取消截止时间
     */
    private Date cancelDeadlineTime;

    /**
     * 商家备注
     */
    private String sellerRemark;

    /**
     * 用户备注
     */
    private String userRemark;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 订单删除状态 0:未删除  1:已删除
     */
    private Integer deleteStatus;

    /**
     * 订单评论状态 0:未发表评论  1:已发表评论
     */
    private Integer commentStatus;
}
