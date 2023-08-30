package com.ruyuan.eshop.order;

import com.alibaba.fastjson.JSON;
import com.ruyuan.eshop.common.constants.RocketMqConstant;
import com.ruyuan.eshop.common.message.PaidOrderSuccessMessage;
import com.ruyuan.eshop.order.mq.producer.DefaultProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = OrderApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MqProducerTest {

    @Autowired
    private DefaultProducer defaultProducer;

    @Test
    public void test() {
        String orderId = "1111111";
        PaidOrderSuccessMessage message = new PaidOrderSuccessMessage();
        message.setOrderId(orderId);
        String msgJson = JSON.toJSONString(message);
        defaultProducer.sendMessage(RocketMqConstant.PAID_ORDER_SUCCESS_TOPIC, msgJson, "订单已完成支付", null, orderId);
    }
}
