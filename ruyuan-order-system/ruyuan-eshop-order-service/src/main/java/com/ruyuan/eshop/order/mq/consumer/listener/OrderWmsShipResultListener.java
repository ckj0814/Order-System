package com.ruyuan.eshop.order.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.constants.RedisLockKeyConstants;
import com.ruyuan.eshop.common.enums.OrderStatusChangeEnum;
import com.ruyuan.eshop.common.exception.BaseBizException;
import com.ruyuan.eshop.common.message.OrderEvent;
import com.ruyuan.eshop.common.redis.RedisLock;
import com.ruyuan.eshop.fulfill.domain.event.OrderDeliveredWmsEvent;
import com.ruyuan.eshop.fulfill.domain.event.OrderOutStockWmsEvent;
import com.ruyuan.eshop.fulfill.domain.event.OrderSignedWmsEvent;
import com.ruyuan.eshop.order.converter.WmsShipDtoConverter;
import com.ruyuan.eshop.order.domain.dto.WmsShipDTO;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
import com.ruyuan.eshop.order.service.OrderFulFillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 监听 订单物流配送结果消息
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class OrderWmsShipResultListener implements MessageListenerOrderly {

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private OrderFulFillService orderFulFillService;

    @Autowired
    private WmsShipDtoConverter wmsShipDtoConverter;

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
        OrderEvent orderEvent;
        try {

            for (MessageExt messageExt : list) {
                String message = new String(messageExt.getBody());

                log.info("received orderWmsShopResult  message:{}", message);
                orderEvent = JSONObject.parseObject(message, OrderEvent.class);

                //1、解析消息
                WmsShipDTO wmsShipDTO = buildWmsShip(orderEvent);

                //2、加分布式锁+里面的前置状态校验防止消息重复消费
                String key = RedisLockKeyConstants.ORDER_WMS_RESULT_KEY + wmsShipDTO.getOrderId();
                boolean lock = redisLock.tryLock(key);
                if (!lock) {
                    log.error("order has not acquired lock，cannot inform order wms result, orderId={}", wmsShipDTO.getOrderId());
                    throw new BaseBizException(OrderErrorCodeEnum.ORDER_NOT_ALLOW_INFORM_WMS_RESULT);
                }

                //3、通知订单物流结果
                //  注意这里分布式锁加锁放在了本地事务外面
                try {
                    orderFulFillService.informOrderWmsShipResult(wmsShipDTO);
                } finally {
                    if (lock) {
                        redisLock.unlock(key);
                    }
                }
            }

            return ConsumeOrderlyStatus.SUCCESS;
        } catch (Exception e) {
            // 处理业务逻辑失败！ Suspend current queue a moment
            return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }
    }

    private WmsShipDTO buildWmsShip(OrderEvent orderEvent) {
        String messageContent = JSONObject.toJSONString(orderEvent.getMessageContent());
        WmsShipDTO wmsShipDTO = null;
        if (OrderStatusChangeEnum.ORDER_OUT_STOCKED.equals(orderEvent.getOrderStatusChange())) {
            //订单已出库消息
            OrderOutStockWmsEvent outStockWmsEvent = JSONObject.parseObject(messageContent, OrderOutStockWmsEvent.class);
            wmsShipDTO = wmsShipDtoConverter.convert(outStockWmsEvent);
        } else if (OrderStatusChangeEnum.ORDER_DELIVERED.equals(orderEvent.getOrderStatusChange())) {
            //订单已配送消息
            OrderDeliveredWmsEvent deliveredWmsEvent = JSONObject.parseObject(messageContent, OrderDeliveredWmsEvent.class);
            wmsShipDTO = wmsShipDtoConverter.convert(deliveredWmsEvent);
        } else if (OrderStatusChangeEnum.ORDER_SIGNED.equals(orderEvent.getOrderStatusChange())) {
            //订单已签收消息
            OrderSignedWmsEvent signedWmsEvent = JSONObject.parseObject(messageContent, OrderSignedWmsEvent.class);
            wmsShipDTO = wmsShipDtoConverter.convert(signedWmsEvent);
        }
        if (wmsShipDTO != null) {
            wmsShipDTO.setStatusChange(orderEvent.getOrderStatusChange());
        }
        return wmsShipDTO;
    }

}