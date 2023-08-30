package com.ruyuan.eshop.fulfill.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruyuan.eshop.common.constants.RocketMqConstant;
import com.ruyuan.eshop.common.enums.OrderStatusChangeEnum;
import com.ruyuan.eshop.common.message.OrderEvent;
import com.ruyuan.eshop.fulfill.domain.request.TriggerOrderWmsShipEventRequest;
import com.ruyuan.eshop.fulfill.mq.producer.DefaultProducer;
import com.ruyuan.eshop.fulfill.service.OrderWmsShipEventProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
public abstract class AbstractWmsShipEventProcessor implements OrderWmsShipEventProcessor {

    @Autowired
    private DefaultProducer defaultProducer;

    @Override
    public void execute(TriggerOrderWmsShipEventRequest request) {

        //1、执行业务流程
        doBizProcess(request);

        //2、构造消息体
        String body = buildMsgBody(request);

        //3、发送消息
        sendMessage(body, request.getOrderId());
    }

    /**
     * 执行业务流程
     */
    protected abstract void doBizProcess(TriggerOrderWmsShipEventRequest request);

    protected abstract String buildMsgBody(TriggerOrderWmsShipEventRequest request);

    private void sendMessage(String body, String orderId) {
        if (StringUtils.isNotBlank(body)) {
            Message message = new Message();
            message.setTopic(RocketMqConstant.ORDER_WMS_SHIP_RESULT_TOPIC);
            message.setBody(body.getBytes(StandardCharsets.UTF_8));
            try {
                DefaultMQProducer defaultMQProducer = defaultProducer.getProducer();
                SendResult sendResult = defaultMQProducer.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message message, Object arg) {
                        //根据订单id选择发送queue
                        String orderId = (String) arg;
                        long index = hash(orderId) % mqs.size();
                        return mqs.get((int) index);
                    }
                }, orderId);

                log.info("send order wms ship result message finished，SendResult status:{}, queueId:{}, body:{}", sendResult.getSendStatus(),
                        sendResult.getMessageQueue().getQueueId(), body);
            } catch (Exception e) {
                log.error("send order wms ship result message error,orderId={},err={}", orderId, e.getMessage(), e);
            }
        }
    }

    protected <T> OrderEvent buildOrderEvent(String orderId, OrderStatusChangeEnum orderStatusChange, T messaheContent, Class<T> clazz) {
        OrderEvent<T> orderEvent = new OrderEvent<>();

        orderEvent.setOrderId(orderId);
        orderEvent.setBusinessIdentifier(1);
        orderEvent.setOrderType(1);
        orderEvent.setOrderStatusChange(orderStatusChange);
        orderEvent.setMessageContent(messaheContent);

        return orderEvent;
    }

    /**
     * hash
     *
     * @param orderId
     * @return
     */
    private int hash(String orderId) {
        //解决取模可能为负数的情况
        return orderId.hashCode() & Integer.MAX_VALUE;
    }


}
