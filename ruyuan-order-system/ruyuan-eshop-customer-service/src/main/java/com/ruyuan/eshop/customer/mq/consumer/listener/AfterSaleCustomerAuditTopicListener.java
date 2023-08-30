package com.ruyuan.eshop.customer.mq.consumer.listener;

import com.alibaba.fastjson.JSON;
import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.customer.domain.request.CustomerReceiveAfterSaleRequest;
import com.ruyuan.eshop.customer.exception.CustomerBizException;
import com.ruyuan.eshop.customer.exception.CustomerErrorCodeEnum;
import com.ruyuan.eshop.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 接收订单系统售后审核申请
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class AfterSaleCustomerAuditTopicListener implements MessageListenerConcurrently {

    @Autowired
    private CustomerService customerService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                    ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt messageExt : list) {
                String message = new String(messageExt.getBody());
                log.info("AfterSaleCustomerAuditTopicListener message:{}", message);
                CustomerReceiveAfterSaleRequest customerReceiveAfterSaleRequest = JSON.parseObject(message, CustomerReceiveAfterSaleRequest.class);
                //  客服接收订单系统的售后申请
                JsonResult<Boolean> jsonResult = customerService.receiveAfterSale(customerReceiveAfterSaleRequest);
                if (!jsonResult.getSuccess()) {
                    throw new CustomerBizException(CustomerErrorCodeEnum.PROCESS_RECEIVE_AFTER_SALE);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            log.error("consumer error", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}