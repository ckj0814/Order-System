package com.ruyuan.eshop.inventory.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.inventory.api.InventoryApi;
import com.ruyuan.eshop.inventory.domain.request.ReleaseProductStockRequest;
import com.ruyuan.eshop.inventory.exception.InventoryBizException;
import com.ruyuan.eshop.inventory.exception.InventoryErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 监听释放库存消息
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class ReleaseInventoryListener implements MessageListenerConcurrently {

    @DubboReference(version = "1.0.0")
    private InventoryApi inventoryApi;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt msg : list) {
                String content = new String(msg.getBody(), StandardCharsets.UTF_8);
                log.info("ReleaseInventoryConsumer message:{}", content);
                ReleaseProductStockRequest releaseProductStockRequest = JSONObject.parseObject(content, ReleaseProductStockRequest.class);
                //  释放库存
                JsonResult<Boolean> jsonResult = inventoryApi.cancelOrderReleaseProductStock(releaseProductStockRequest);
                if (!jsonResult.getSuccess()) {
                    throw new InventoryBizException(InventoryErrorCodeEnum.CONSUME_MQ_FAILED);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            log.error("consumer error", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}