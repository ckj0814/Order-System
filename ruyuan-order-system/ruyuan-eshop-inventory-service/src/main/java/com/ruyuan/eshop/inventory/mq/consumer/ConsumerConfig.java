package com.ruyuan.eshop.inventory.mq.consumer;

import com.ruyuan.eshop.common.constants.RocketMqConstant;
import com.ruyuan.eshop.inventory.mq.consumer.listener.ReleaseInventoryListener;
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
     * 释放库存消息消费者
     *
     * @param releaseInventoryListener
     * @return
     */
    @Bean("releaseInventoryConsumer")
    public DefaultMQPushConsumer releaseInventoryConsumer(ReleaseInventoryListener releaseInventoryListener)
            throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMqConstant.RELEASE_INVENTORY_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMqConstant.CANCEL_RELEASE_INVENTORY_TOPIC, "*");
        consumer.registerMessageListener(releaseInventoryListener);
        consumer.start();
        return consumer;
    }
}