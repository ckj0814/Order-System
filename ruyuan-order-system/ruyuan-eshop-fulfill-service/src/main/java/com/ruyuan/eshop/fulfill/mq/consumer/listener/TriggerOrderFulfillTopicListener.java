package com.ruyuan.eshop.fulfill.mq.consumer.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.fulfill.domain.request.ReceiveFulfillRequest;
import com.ruyuan.eshop.fulfill.service.FulfillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 接受订单履约消息
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class TriggerOrderFulfillTopicListener implements MessageListenerConcurrently {

    /**
     * 履约服务
     */
    @Autowired
    private FulfillService fulfillService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                    ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt messageExt : list) {
                String message = new String(messageExt.getBody());
                ReceiveFulfillRequest request =
                        JSON.parseObject(message, ReceiveFulfillRequest.class);

                log.info("接受订单履约成功，request={}", JSONObject.toJSONString(request));

                fulfillService.receiveOrderFulFill(request);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            log.error("consumer error", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }

}