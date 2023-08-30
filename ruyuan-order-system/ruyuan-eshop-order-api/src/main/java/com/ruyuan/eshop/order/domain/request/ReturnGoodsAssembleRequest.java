package com.ruyuan.eshop.order.domain.request;

import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderItemDTO;
import com.ruyuan.eshop.order.domain.dto.OrderInfoDTO;
import com.ruyuan.eshop.order.domain.dto.OrderItemDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class ReturnGoodsAssembleRequest implements Serializable {
    /**
     * 实际退款金额
     */
    private Integer returnGoodAmount;
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
     * 订单售后条目列表
     */
    private List<AfterSaleOrderItemDTO> afterSaleOrderItemDTOList;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 当前订单是否是退最后一笔
     */
    private boolean lastReturnGoods = false;

    /**
     * 订单id
     */
    private String orderId;
    /**
     * 订单信息
     */
    private OrderInfoDTO orderInfoDTO;
    /**
     * 订单条目列表
     */
    private List<OrderItemDTO> orderItemDTOList;
    /**
     * 售后类型 1 退款  2 退货
     */
    private Integer afterSaleType;
    /**
     * 售后id
     */
    private String afterSaleId;
    /**
     * 售后支付单id
     */
    private Long afterSaleRefundId;

    /**
     * 该笔订单的售后状态
     */
    private Integer afterSaleStatus;

    /**
     * 执行售后退货时，本次售后退货的条目
     */
    private List<OrderItemDTO> refundOrderItemDTO;

}
