package com.ruyuan.eshop.order;

import com.ruyuan.eshop.order.service.OrderFulFillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = OrderApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderFulFillServiceTest {

    @Autowired
    private OrderFulFillService orderFulFillService;


    @Test
    public void triggerOrderFulFill() throws Exception {

        String orderId = "1011250000000010000";
        orderFulFillService.triggerOrderFulFill(orderId);

    }

}
