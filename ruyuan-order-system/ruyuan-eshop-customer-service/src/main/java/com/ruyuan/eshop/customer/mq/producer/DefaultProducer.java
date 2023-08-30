package com.ruyuan.eshop.customer.mq.producer;

import com.ruyuan.eshop.common.constants.RocketMqConstant;
import com.ruyuan.eshop.customer.exception.CustomerBizException;
import com.ruyuan.eshop.customer.exception.CustomerErrorCodeEnum;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 默认的普通的mq消息生产者（只能发普通消息，不能发事务消息）
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class DefaultProducer {

    private static Logger log = LoggerFactory.getLogger(DefaultProducer.class);

    private DefaultMQProducer producer;

    @Autowired
    public DefaultProducer(RocketMQProperties rocketMQProperties) {
        producer = new DefaultMQProducer(RocketMqConstant.ORDER_DEFAULT_PRODUCER_GROUP);
        producer.setNamesrvAddr(rocketMQProperties.getNameServer());
        start();
    }

    public DefaultMQProducer getProducer() {
        return this.producer;
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
    public void sendMessage(String topic, String message, String type) {
        sendMessage(topic, message, -1, type);
    }

    /**
     * 发送消息
     *
     * @param topic   topic
     * @param message 消息
     */
    public void sendMessage(String topic, String message, Integer delayTimeLevel, String type) {
        Message msg = new Message(topic, message.getBytes(StandardCharsets.UTF_8));
        try {
            if (delayTimeLevel > 0) {
                msg.setDelayTimeLevel(delayTimeLevel);
            }
            SendResult send = producer.send(msg);
            if (SendStatus.SEND_OK == send.getSendStatus()) {
                log.info("发送MQ消息成功, type:{}, message:{}", type, message);
            } else {
                throw new CustomerBizException(send.getSendStatus().toString());
            }
        } catch (Exception e) {
            log.error("发送MQ消息失败：", e);
            throw new CustomerBizException(CustomerErrorCodeEnum.SEND_MQ_FAILED);
        }
    }

}
