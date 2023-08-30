package com.ruyuan.eshop.order.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.order.domain.request.CancelOrderAssembleRequest;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
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
public class CancelRefundListener implements MessageListenerConcurrently {

    @Autowired
    private OrderAfterSaleService orderAfterSaleService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt messageExt : list) {
                String message = new String(messageExt.getBody());
                CancelOrderAssembleRequest cancelOrderAssembleRequest = JSONObject.parseObject(message, CancelOrderAssembleRequest.class);
                log.info("CancelRefundConsumer message:{}", message);

                //  执行 取消订单/超时未支付取消 前的操作
                JsonResult<Boolean> jsonResult = orderAfterSaleService.processCancelOrder(cancelOrderAssembleRequest);

                if (!jsonResult.getSuccess()) {
                    throw new OrderBizException(OrderErrorCodeEnum.CONSUME_MQ_FAILED);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            log.error("consumer error", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}