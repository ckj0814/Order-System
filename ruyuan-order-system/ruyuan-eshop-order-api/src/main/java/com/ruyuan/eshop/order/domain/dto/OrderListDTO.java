package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单列表DTO
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class OrderListDTO implements Serializable {

    /**
     * 接入方业务线标识
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
     * 订单类型
     */
    private Integer orderType;

    /**
     * 订单状态
     */
    private Integer orderStatus;

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
     * 使用的优惠券编号
     */
    private String couponId;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 用户备注
     */
    private String userRemark;

    /**
     * 订单评论状态 0:未发表评论  1:已发表评论
     */
    private Integer commentStatus;


    // order_item

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
     * 交易支付金额
     */
    private Integer payAmount;
}
