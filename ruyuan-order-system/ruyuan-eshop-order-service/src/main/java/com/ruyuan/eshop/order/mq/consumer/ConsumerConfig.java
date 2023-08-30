package com.ruyuan.eshop.order.mq.consumer;

import com.ruyuan.eshop.common.constants.RocketMqConstant;
import com.ruyuan.eshop.order.mq.consumer.listener.*;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ruyuan.eshop.common.constants.RocketMqConstant.*;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Configuration
public class ConsumerConfig {

    @Autowired
    private RocketMQProperties rocketMQProperties;

    /**
     * 订单完成支付消息消费者
     *
     * @param paidOrderSuccessListener
     * @return
     * @throws MQClientException
     */
    @Bean("paidOrderSuccessConsumer")
    public DefaultMQPushConsumer paidOrderSuccessConsumer(PaidOrderSuccessListener paidOrderSuccessListener)
            throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(PAID_ORDER_SUCCESS_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(PAID_ORDER_SUCCESS_TOPIC, "*");
        consumer.registerMessageListener(paidOrderSuccessListener);
        consumer.start();
        return consumer;
    }

    @Bean("actualRefundConsumer")
    public DefaultMQPushConsumer actualRefundConsumer(ActualRefundListener actualRefundListener)
            throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(ACTUAL_REFUND_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(ACTUAL_REFUND_TOPIC, "*");
        consumer.registerMessageListener(actualRefundListener);
        consumer.start();
        return consumer;
    }

    /**
     * 消费退款请求消息 消费者
     *
     * @param cancelRefundListener
     * @return
     * @throws MQClientException
     */
    @Bean("cancelRefundConsumer")
    public DefaultMQPushConsumer cancelRefundConsumer(CancelRefundListener cancelRefundListener)
            throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMqConstant.REQUEST_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());

        consumer.subscribe(RocketMqConstant.CANCEL_REFUND_REQUEST_TOPIC, "*");
        consumer.registerMessageListener(cancelRefundListener);
        consumer.start();
        return consumer;
    }

    /**
     * 订单物流配送结果rocketmq消息消费者
     *
     * @param orderWmsShipResultListener
     * @return
     * @throws MQClientException
     */
    @Bean("orderWmsShipResultConsumer")
    public DefaultMQPushConsumer orderWmsShipResultConsumer(OrderWmsShipResultListener orderWmsShipResultListener)
            throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMqConstant.ORDER_WMS_SHIP_RESULT_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMqConstant.ORDER_WMS_SHIP_RESULT_TOPIC, "*");
        consumer.registerMessageListener(orderWmsShipResultListener);
        consumer.start();
        return consumer;
    }

    /**
     * 支付订单超时延迟消息消费者
     *
     * @param payOrderTimeoutListener
     * @return
     * @throws MQClientException
     */
    @Bean("payOrderTimeoutConsumer")
    public DefaultMQPushConsumer payOrderTimeoutConsumer(PayOrderTimeoutListener payOrderTimeoutListener)
            throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(PAY_ORDER_TIMEOUT_DELAY_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(PAY_ORDER_TIMEOUT_DELAY_TOPIC, "*");
        consumer.registerMessageListener(payOrderTimeoutListener);
        consumer.start();
        return consumer;

    }

    /**
     * 释放资产消息消费者
     *
     * @param releaseAssetsListener
     * @return
     * @throws MQClientException
     */
    @Bean("releaseAssetsConsumer")
    public DefaultMQPushConsumer releaseAssetsConsumer(ReleaseAssetsListener releaseAssetsListener)
            throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RELEASE_ASSETS_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RELEASE_ASSETS_TOPIC, "*");
        consumer.registerMessageListener(releaseAssetsListener);
        consumer.start();
        return consumer;
    }

    /**
     * 释放资产消息消费者
     */
    @Bean("auditPassReleaseAssetsConsumer")
    public DefaultMQPushConsumer auditPassReleaseAssetsConsumer(AuditPassReleaseAssetsListener auditPassReleaseAssetsListener)
            throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CUSTOMER_AUDIT_PASS_RELEASE_ASSETS_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(CUSTOMER_AUDIT_PASS_RELEASE_ASSETS_TOPIC, "*");
        consumer.registerMessageListener(auditPassReleaseAssetsListener);
        consumer.start();
        return consumer;
    }

}