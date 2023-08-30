package com.ruyuan.eshop.fulfill.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.enums.OrderStatusChangeEnum;
import com.ruyuan.eshop.common.message.OrderEvent;
import com.ruyuan.eshop.fulfill.domain.event.OrderOutStockWmsEvent;
import com.ruyuan.eshop.fulfill.domain.request.TriggerOrderWmsShipEventRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 订单已出库事件处理器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
@Slf4j
public class OrderOutStockWmsEventProcessor extends AbstractWmsShipEventProcessor {

    @Override
    protected void doBizProcess(TriggerOrderWmsShipEventRequest request) {
        log.info("准备发送【订单已出库事件】");
    }

    @Override
    protected String buildMsgBody(TriggerOrderWmsShipEventRequest request) {
        String orderId = request.getOrderId();
        //订单已出库事件
        OrderOutStockWmsEvent outStockEvent = (OrderOutStockWmsEvent) request.getWmsEvent();
        outStockEvent.setOrderId(orderId);

        //构建订单已出库消息体
        OrderEvent<OrderOutStockWmsEvent> orderEvent = buildOrderEvent(orderId, OrderStatusChangeEnum.ORDER_OUT_STOCKED,
                outStockEvent, OrderOutStockWmsEvent.class);

        return JSONObject.toJSONString(orderEvent);
    }
}
