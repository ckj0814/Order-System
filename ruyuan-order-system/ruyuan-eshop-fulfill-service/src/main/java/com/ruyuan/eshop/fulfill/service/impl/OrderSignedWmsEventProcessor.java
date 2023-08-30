package com.ruyuan.eshop.fulfill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.enums.OrderStatusChangeEnum;
import com.ruyuan.eshop.common.message.OrderEvent;
import com.ruyuan.eshop.fulfill.domain.event.OrderSignedWmsEvent;
import com.ruyuan.eshop.fulfill.domain.request.TriggerOrderWmsShipEventRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 订单已签收事件处理器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
@Slf4j
public class OrderSignedWmsEventProcessor extends AbstractWmsShipEventProcessor {

    @Override
    protected void doBizProcess(TriggerOrderWmsShipEventRequest request) {
        log.info("准备发送【订单已签收事件】");
    }

    @Override
    protected String buildMsgBody(TriggerOrderWmsShipEventRequest request) {
        String orderId = request.getOrderId();
        //订单已签收事件
        OrderSignedWmsEvent signedWmsEvent = (OrderSignedWmsEvent) request.getWmsEvent();
        signedWmsEvent.setOrderId(orderId);

        //构建订单已签收消息体
        OrderEvent<OrderSignedWmsEvent> orderEvent = buildOrderEvent(orderId, OrderStatusChangeEnum.ORDER_SIGNED,
                signedWmsEvent, OrderSignedWmsEvent.class);

        return JSONObject.toJSONString(orderEvent);
    }
}
