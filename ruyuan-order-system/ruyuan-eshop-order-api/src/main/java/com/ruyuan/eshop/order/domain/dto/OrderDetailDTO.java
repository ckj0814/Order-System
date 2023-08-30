package com.ruyuan.eshop.order.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单详情DTO
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@Builder
public class OrderDetailDTO {

    /**
     * 订单
     */
    private OrderInfoDTO orderInfo;
    /**
     * 订单条目
     */
    private List<OrderItemDTO> orderItems;
    /**
     * 订单费用明细
     */
    private List<OrderAmountDetailDTO> orderAmountDetails;
    /**
     * 订单配送信息表
     */
    private OrderDeliveryDetailDTO orderDeliveryDetail;
    /**
     * 订单支付明细
     */
    private List<OrderPaymentDetailDTO> orderPaymentDetails;

    /**
     * 费用类型
     */
    private Map<Integer, Integer> orderAmounts;

    /**
     * 订单操作日志
     */
    private List<OrderOperateLogDTO> orderOperateLogs;

    /**
     * 订单快照信息
     */
    private List<OrderSnapshotDTO> orderSnapshots;

    /**
     * 订单缺品信息
     */
    private List<OrderLackItemDTO> lackItems;
}
