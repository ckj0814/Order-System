package com.ruyuan.eshop.order.mq.consumer.listener;

import com.alibaba.fastjson.JSON;
import com.ruyuan.eshop.common.enums.OrderStatusEnum;
import com.ruyuan.eshop.common.message.PayOrderTimeoutDelayMessage;
import com.ruyuan.eshop.order.dao.OrderInfoDAO;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import com.ruyuan.eshop.order.domain.request.CancelOrderRequest;
import com.ruyuan.eshop.order.service.OrderAfterSaleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 监听 支付订单超时延迟消息
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class PayOrderTimeoutListener implements MessageListenerConcurrently {

    @Autowired
    private OrderAfterSaleService orderAfterSaleService;

    @Autowired
    private OrderInfoDAO orderInfoDAO;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt messageExt : list) {
                String message = new String(messageExt.getBody());
                PayOrderTimeoutDelayMessage payOrderTimeoutDelayMessage = JSON.parseObject(message, PayOrderTimeoutDelayMessage.class);
                // 消费延迟消息，执行关单逻辑
                CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
                cancelOrderRequest.setOrderId(payOrderTimeoutDelayMessage.getOrderId());
                cancelOrderRequest.setBusinessIdentifier(payOrderTimeoutDelayMessage.getBusinessIdentifier());
                cancelOrderRequest.setCancelType(payOrderTimeoutDelayMessage.getOrderType());
                cancelOrderRequest.setUserId(payOrderTimeoutDelayMessage.getUserId());
                cancelOrderRequest.setOrderType(payOrderTimeoutDelayMessage.getOrderType());
                cancelOrderRequest.setOrderStatus(payOrderTimeoutDelayMessage.getOrderStatus());

                //  查询当前数据库的订单实时状态
                OrderInfoDO orderInfoDO = orderInfoDAO.getByOrderId(payOrderTimeoutDelayMessage.getOrderId());
                if (orderInfoDO == null) {
                    log.warn("订单不存在，orderId:{}", cancelOrderRequest.getOrderId());
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                Integer orderStatusDatabase = orderInfoDO.getOrderStatus();
                if (!OrderStatusEnum.CREATED.getCode().equals(orderStatusDatabase)) {
                    //  订单实时状态不等于已创建
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                //  当前时间 小于 订单实际支付截止时间
                if (new Date().before(orderInfoDO.getExpireTime())) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }

                orderAfterSaleService.cancelOrder(cancelOrderRequest);
                log.info("关闭订单，orderId:{}", cancelOrderRequest.getOrderId());
            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            log.error("consumer error", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }

}