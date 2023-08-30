package com.ruyuan.eshop.order.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.common.message.ActualRefundMessage;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.service.OrderAfterSaleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class ActualRefundListener implements MessageListenerConcurrently {

    @Autowired
    private OrderAfterSaleService orderAfterSaleService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt messageExt : list) {
                String message = new String(messageExt.getBody());
                ActualRefundMessage actualRefundMessage = JSONObject.parseObject(message, ActualRefundMessage.class);
                log.info("ActualRefundConsumer message:{}", message);

                JsonResult<Boolean> jsonResult = orderAfterSaleService.refundMoney(actualRefundMessage);
                if (!jsonResult.getSuccess()) {
                    throw new OrderBizException(jsonResult.getErrorCode(), jsonResult.getErrorMessage());
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            log.error("consumer error", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}