package com.ruyuan.eshop.order.mq.consumer.listener;

import com.alibaba.fastjson.JSON;
import com.ruyuan.eshop.common.constants.RedisLockKeyConstants;
import com.ruyuan.eshop.common.enums.OrderStatusEnum;
import com.ruyuan.eshop.common.exception.BaseBizException;
import com.ruyuan.eshop.common.message.PaidOrderSuccessMessage;
import com.ruyuan.eshop.common.redis.RedisLock;
import com.ruyuan.eshop.fulfill.domain.request.ReceiveFulfillRequest;
import com.ruyuan.eshop.order.dao.OrderInfoDAO;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
import com.ruyuan.eshop.order.mq.producer.DefaultProducer;
import com.ruyuan.eshop.order.service.OrderFulFillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static com.ruyuan.eshop.common.constants.RocketMqConstant.TRIGGER_ORDER_FULFILL_TOPIC;

/**
 * 监听订单支付成功后的消息
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class PaidOrderSuccessListener implements MessageListenerConcurrently {

    @Autowired
    private OrderInfoDAO orderInfoDAO;

    @Autowired
    private OrderFulFillService orderFulFillService;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private DefaultProducer defaultProducer;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {

            for (MessageExt messageExt : list) {
                String message = new String(messageExt.getBody());
                PaidOrderSuccessMessage paidOrderSuccessMessage = JSON.parseObject(message, PaidOrderSuccessMessage.class);
                String orderId = paidOrderSuccessMessage.getOrderId();
                log.info("触发订单履约，orderId:{}", orderId);

                OrderInfoDO order = orderInfoDAO.getByOrderId(orderId);
                if (Objects.isNull(order)) {
                    throw new OrderBizException(OrderErrorCodeEnum.ORDER_INFO_IS_NULL);
                }

                //1、加分布式锁+里面的履约前置状态校验防止消息重复消费
                String key = RedisLockKeyConstants.ORDER_FULFILL_KEY + orderId;
                if (!redisLock.tryLock(key)) {
                    log.error("order has not acquired lock，cannot fulfill, orderId={}", orderId);
                    throw new BaseBizException(OrderErrorCodeEnum.ORDER_FULFILL_ERROR);
                }

                try {
                    //2、进行订单履约逻辑
                    TransactionMQProducer producer = defaultProducer.getProducer();
                    producer.setTransactionListener(new TransactionListener() {

                        @Override
                        public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                            try {
                                orderFulFillService.triggerOrderFulFill(orderId);
                                return LocalTransactionState.COMMIT_MESSAGE;
                            } catch (BaseBizException e) {
                                throw e;
                            } catch (Exception e) {
                                log.error("system error", e);
                                return LocalTransactionState.ROLLBACK_MESSAGE;
                            }

                        }

                        @Override
                        public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                            // 检查订单是否"已履约"状态
                            OrderInfoDO orderInfoDO = orderInfoDAO.getByOrderId(orderId);
                            if (orderInfoDO != null
                                    && OrderStatusEnum.FULFILL.getCode().equals(orderInfoDO.getOrderStatus())) {
                                return LocalTransactionState.COMMIT_MESSAGE;
                            }
                            return LocalTransactionState.ROLLBACK_MESSAGE;
                        }
                    });

                    ReceiveFulfillRequest receiveFulfillRequest = orderFulFillService.buildReceiveFulFillRequest(order);

                    String topic = TRIGGER_ORDER_FULFILL_TOPIC;
                    byte[] body = JSON.toJSONString(receiveFulfillRequest).getBytes(StandardCharsets.UTF_8);
                    Message mq = new Message(topic, null, orderId, body);
                    producer.sendMessageInTransaction(mq, order);

                } finally {
                    redisLock.unlock(key);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            log.error("consumer error", e);
            //本地业务逻辑执行失败，触发消息重新消费
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

    }

}