package com.ruyuan.eshop.order.domain.request;

import com.ruyuan.eshop.order.domain.dto.CancelOrderRefundAmountDTO;
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
public class CancelOrderAssembleRequest implements Serializable {

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
     * 取消订单 退款金额 DTO
     */
    private CancelOrderRefundAmountDTO cancelOrderRefundAmountDTO;
    /**
     * 售后id
     */
    private String afterSaleId;
    /**
     * 订单id
     */
    private String orderId;

    /**
     * 订单取消类型 0-手动取消 1-超时未支付
     */
    private Integer cancelType;
    /**
     * 售后支付单id
     */
    private Long afterSaleRefundId;
    /**
     * 当前订单是否是退最后一笔
     */
    private boolean lastReturnGoods = false;
}
