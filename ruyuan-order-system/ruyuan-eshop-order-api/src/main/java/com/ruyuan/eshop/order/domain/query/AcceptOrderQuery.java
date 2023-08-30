package com.ruyuan.eshop.order.domain.query;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 接收 订单列表查询请求的入参
 * <p>
 * 与OrderQuery的主要区别:
 * 为OrderQuery提供创建时间 、支付时间、支付金额 三个区间的组合参数
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class AcceptOrderQuery implements Serializable {
    public static final Integer MAX_PAGE_SIZE = 100;

    /**
     * 业务线
     */
    private Integer businessIdentifier;
    /**
     * 订单类型
     */
    private Set<Integer> orderTypes;
    /**
     * 订单号
     */
    private Set<String> orderIds;
    /**
     * 卖家ID
     */
    private Set<String> sellerIds;
    /**
     * 父订单号
     */
    private Set<String> parentOrderIds;
    /**
     * 用户ID
     */
    private Set<String> userIds;
    /**
     * 订单状态
     */
    private Set<Integer> orderStatus;
    /**
     * 收货人手机号
     */
    private Set<String> receiverPhones;
    /**
     * 收货人姓名
     */
    private Set<String> receiverNames;
    /**
     * 交易流水号
     */
    private Set<String> tradeNos;

    /**
     * sku code
     */
    private Set<String> skuCodes;
    /**
     * sku商品名称
     */
    private Set<String> productNames;

    /**
     * 查询创建时间的区间范围
     */
    private Date queryStartCreatedTime;

    private Date queryEndCreatedTime;

    /**
     * 查询支付时间的区间范围
     */
    private Date queryStartPayTime;

    private Date queryEndPayTime;

    /**
     * 查询支付金额的区间范围
     */
    private Integer queryStartPayAmount;

    private Integer queryEndPayAmount;

    /**
     * 页码
     */
    private Integer pageNo = 1;
    /**
     * 每页条数
     */
    private Integer pageSize = 20;
}
