package com.ruyuan.eshop.fulfill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.enums.OrderStatusChangeEnum;
import com.ruyuan.eshop.common.message.OrderEvent;
import com.ruyuan.eshop.fulfill.dao.OrderFulfillDAO;
import com.ruyuan.eshop.fulfill.domain.event.OrderDeliveredWmsEvent;
import com.ruyuan.eshop.fulfill.domain.request.TriggerOrderWmsShipEventRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单已配送事件处理器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
@Slf4j
public class OrderDeliveredWmsEventProcessor extends AbstractWmsShipEventProcessor {

    @Autowired
    private OrderFulfillDAO orderFulfillDAO;

    @Override
    protected void doBizProcess(TriggerOrderWmsShipEventRequest request) {
        OrderDeliveredWmsEvent deliveredWmsEvent = (OrderDeliveredWmsEvent) request.getWmsEvent();
        String fulfillId = request.getFulfillId();
        //更新配送员信息
        orderFulfillDAO.updateDeliverer(fulfillId, deliveredWmsEvent.getDelivererNo(),
                deliveredWmsEvent.getDelivererName(), deliveredWmsEvent.getDelivererPhone());
    }

    @Override
    protected String buildMsgBody(TriggerOrderWmsShipEventRequest request) {
        String orderId = request.getOrderId();
        //订单已配送事件
        OrderDeliveredWmsEvent deliveredWmsEvent = (OrderDeliveredWmsEvent) request.getWmsEvent();
        deliveredWmsEvent.setOrderId(orderId);

        //构建订单已配送消息体
        OrderEvent<OrderDeliveredWmsEvent> orderEvent = buildOrderEvent(orderId, OrderStatusChangeEnum.ORDER_DELIVERED,
                deliveredWmsEvent, OrderDeliveredWmsEvent.class);
        return JSONObject.toJSONString(orderEvent);
    }
}
