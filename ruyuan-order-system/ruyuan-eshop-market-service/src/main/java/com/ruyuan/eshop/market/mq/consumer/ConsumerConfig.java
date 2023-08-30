package com.ruyuan.eshop.market.mq.consumer;

import com.ruyuan.eshop.common.constants.RocketMqConstant;
import com.ruyuan.eshop.market.mq.consumer.listener.ReleasePropertyListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Configuration
public class ConsumerConfig {

    @Autowired
    private RocketMQProperties rocketMQProperties;

    /**
     * 释放资产权益消息消费者
     *
     * @param releasePropertyListener
     * @return
     */
    @Bean("releaseInventoryConsumer")
    public DefaultMQPushConsumer releaseInventoryConsumer(ReleasePropertyListener releasePropertyListener)
            throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMqConstant.RELEASE_PROPERTY_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMqConstant.CANCEL_RELEASE_PROPERTY_TOPIC, "*");
        consumer.registerMessageListener(releasePropertyListener);
        consumer.start();
        return consumer;
    }

}