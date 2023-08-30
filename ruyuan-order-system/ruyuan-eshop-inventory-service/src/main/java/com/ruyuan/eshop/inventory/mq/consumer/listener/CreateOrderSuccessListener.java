package com.ruyuan.eshop.inventory.mq.consumer.listener;

import com.alibaba.fastjson.JSON;
import com.ruyuan.eshop.inventory.domain.request.DeductProductStockRequest;
import com.ruyuan.eshop.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 监听订单创建成功后的消息
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class CreateOrderSuccessListener implements MessageListenerConcurrently {

    /**
     * 库存服务
     */
    @Autowired
    private InventoryService inventoryService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {

            for (MessageExt messageExt : list) {
                String message = new String(messageExt.getBody());
                DeductProductStockRequest deductProductStockRequest = JSON.parseObject(message, DeductProductStockRequest.class);
                // 触发扣减商品库存
                inventoryService.deductProductStock(deductProductStockRequest);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            log.error("consumer error", e);
            //本地业务逻辑执行失败，触发消息重新消费
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }

}