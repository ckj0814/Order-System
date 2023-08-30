package com.ruyuan.eshop.order.mq.producer;

import com.alibaba.fastjson.JSON;
import com.ruyuan.eshop.common.constants.RocketMqConstant;
import com.ruyuan.eshop.common.message.TraceableMessage;
import com.ruyuan.eshop.common.utils.MdcUtil;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class DefaultProducer {

    private final TransactionMQProducer producer;

    @Autowired
    public DefaultProducer(RocketMQProperties rocketMQProperties) {
        producer = new TransactionMQProducer(RocketMqConstant.ORDER_DEFAULT_PRODUCER_GROUP);
        producer.setNamesrvAddr(rocketMQProperties.getNameServer());
        start();
    }

    /**
     * 对象在使用之前必须要调用一次，只能初始化一次
     */
    public void start() {
        try {
            this.producer.start();
        } catch (MQClientException e) {
            log.error("producer start error", e);
        }
    }

    /**
     * 一般在应用上下文，使用上下文监听器，进行关闭
     */
    public void shutdown() {
        this.producer.shutdown();
    }

    /**
     * 发送消息
     *
     * @param topic   topic
     * @param message 消息
     */
    public void sendMessage(String topic, String message, String type, String tags, String keys) {
        sendMessage(topic, message, -1, type, tags, keys);
    }

    /**
     * 发送消息
     *
     * @param topic   topic
     * @param message 消息
     */
    public void sendMessage(String topic, String message, Integer delayTimeLevel, String type, String tags, String keys) {
        Message msg = new Message(topic, tags, keys, message.getBytes(StandardCharsets.UTF_8));
        try {
            if (delayTimeLevel > 0) {
                msg.setDelayTimeLevel(delayTimeLevel);
            }
            SendResult send = producer.send(msg);
            if (SendStatus.SEND_OK == send.getSendStatus()) {
                log.info("发送MQ消息成功, type:{}, message:{}", type, message);
            } else {
                throw new OrderBizException(send.getSendStatus().toString());
            }
        } catch (Exception e) {
            log.error("发送MQ消息失败：", e);
            throw new OrderBizException(OrderErrorCodeEnum.SEND_MQ_FAILED);
        }
    }

    public TransactionSendResult sendMessageInTransaction(String topic, Object messageContent)
            throws MQClientException {
        String traceId = MdcUtil.getTraceId();
        TraceableMessage m = new TraceableMessage(traceId, messageContent.getClass(), messageContent);
        byte[] body = JSON.toJSONString(m).getBytes(StandardCharsets.UTF_8);
        Message message = new Message(topic, body);
        return producer.sendMessageInTransaction(message, messageContent);
    }

    public TransactionMQProducer getProducer() {
        return producer;
    }
}
