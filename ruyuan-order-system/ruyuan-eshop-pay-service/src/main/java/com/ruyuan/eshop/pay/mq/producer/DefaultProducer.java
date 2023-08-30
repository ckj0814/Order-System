package com.ruyuan.eshop.pay.mq.producer;

import com.ruyuan.eshop.common.constants.RocketMqConstant;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}
