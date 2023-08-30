package com.ruyuan.eshop.fulfill.domain.request;

import com.ruyuan.eshop.common.enums.OrderStatusChangeEnum;
import com.ruyuan.eshop.fulfill.domain.event.BaseWmsShipEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 触发订单物流配送结果事件请求
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TriggerOrderWmsShipEventRequest implements Serializable {


    private static final long serialVersionUID = 1710983153062037444L;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 履约单ID
     */
    private String fulfillId;

    /**
     * 订单状态变更
     */
    private OrderStatusChangeEnum orderStatusChange;

    /**
     * 物流配送结果事件消息体
     */
    private BaseWmsShipEvent wmsEvent;
}
