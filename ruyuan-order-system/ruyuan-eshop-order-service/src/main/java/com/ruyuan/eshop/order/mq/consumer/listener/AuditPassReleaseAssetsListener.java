package com.ruyuan.eshop.order.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.constants.RocketMqConstant;
import com.ruyuan.eshop.common.message.ActualRefundMessage;
import com.ruyuan.eshop.inventory.domain.request.ReleaseProductStockRequest;
import com.ruyuan.eshop.order.converter.OrderConverter;
import com.ruyuan.eshop.order.domain.dto.ReleaseProductStockDTO;
import com.ruyuan.eshop.order.domain.request.AuditPassReleaseAssetsRequest;
import com.ruyuan.eshop.order.mq.producer.DefaultProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 接收客服审核通过后的 监听 释放资产消息
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class AuditPassReleaseAssetsListener implements MessageListenerConcurrently {

    @Autowired
    private DefaultProducer defaultProducer;

    @Autowired
    private OrderConverter orderConverter;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt messageExt : list) {
                // 1、消费到释放资产message
                String message = new String(messageExt.getBody());
                log.info("AuditPassReleaseAssetsListener message:{}", message);
                AuditPassReleaseAssetsRequest auditPassReleaseAssetsRequest = JSONObject.parseObject(message, AuditPassReleaseAssetsRequest.class);

                // 2、发送释放库存MQ
                ReleaseProductStockDTO releaseProductStockDTO = auditPassReleaseAssetsRequest.getReleaseProductStockDTO();
                ReleaseProductStockRequest releaseProductStockRequest = buildReleaseProductStock(releaseProductStockDTO);
                defaultProducer.sendMessage(RocketMqConstant.CANCEL_RELEASE_INVENTORY_TOPIC,
                        JSONObject.toJSONString(releaseProductStockRequest), "客服审核通过释放库存", null, null);

                // 3、发送实际退款
                ActualRefundMessage actualRefundMessage = auditPassReleaseAssetsRequest.getActualRefundMessage();
                defaultProducer.sendMessage(RocketMqConstant.ACTUAL_REFUND_TOPIC,
                        JSONObject.toJSONString(actualRefundMessage), "客服审核通过实际退款", null, null);

            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            log.error("consumer error", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }


    /**
     * 组装释放库存数据
     */
    private ReleaseProductStockRequest buildReleaseProductStock(ReleaseProductStockDTO releaseProductStockDTO) {
        List<ReleaseProductStockRequest.OrderItemRequest> orderItemRequestList = new ArrayList<>();

        //  补充订单条目
        for (ReleaseProductStockDTO.OrderItemRequest releaseProductOrderItemRequest : releaseProductStockDTO.getOrderItemRequestList()) {
            orderItemRequestList.add(orderConverter.convertOrderItemRequest(releaseProductOrderItemRequest));
        }

        ReleaseProductStockRequest releaseProductStockRequest = new ReleaseProductStockRequest();
        releaseProductStockRequest.setOrderId(releaseProductStockDTO.getOrderId());
        releaseProductStockRequest.setOrderItemRequestList(orderItemRequestList);

        return releaseProductStockRequest;
    }

}