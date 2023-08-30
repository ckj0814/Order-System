package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class ReturnGoodsAmountDTO implements Serializable {

    private static final long serialVersionUID = 972031755582273564L;
    /**
     * 实际退款金额
     */
    private Integer returnGoodAmount;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * sku编号
     */
    private String skuCode;
    /**
     * 退货数量
     */
    private Integer returnNum;
    /**
     * 申请退款金额
     */
    private Integer applyRefundAmount;

    /**
     * 订单条目DTO
     */
    private List<OrderItemDTO> orderItemDTOList;

    /**
     * 订单售后条目DTO
     */
    private List<AfterSaleItemDTO> afterSaleItemDTOList;

    /**
     * 订单DTO
     */
    private OrderInfoDTO orderInfoDTO;

    /**
     * 售后类型
     * 1 退款
     * 2 退货
     */
    private Integer afterSaleType;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 当前退货笔数是否是最后一笔标记，最后一笔退优惠券，0:最后一笔
     */
    private Integer endFlag;

}
