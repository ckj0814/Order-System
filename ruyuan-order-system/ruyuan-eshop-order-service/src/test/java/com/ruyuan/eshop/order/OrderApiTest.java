package com.ruyuan.eshop.order;

import com.ruyuan.eshop.order.api.OrderApi;
import com.ruyuan.eshop.order.domain.request.AdjustDeliveryAddressRequest;
import com.ruyuan.eshop.order.domain.request.RemoveOrderRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest(classes = OrderApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderApiTest {

    @DubboReference(version = "1.0.0")
    private OrderApi orderApi;


    @Test
    public void removeOrders() throws Exception {
        RemoveOrderRequest removeOrderRequest = new RemoveOrderRequest();
        Set<String> orderIds = new HashSet<>();
        orderIds.add("1011260000000020000");
        removeOrderRequest.setOrderIds(orderIds);
        orderApi.removeOrders(removeOrderRequest);
    }

    @Test
    public void adjustDeliveryAddress() throws Exception {
        AdjustDeliveryAddressRequest adjustDeliveryAddressRequest = new AdjustDeliveryAddressRequest();
        adjustDeliveryAddressRequest.setOrderId("1011250000000010000");
        adjustDeliveryAddressRequest.setProvince("北京");
        orderApi.adjustDeliveryAddress(adjustDeliveryAddressRequest);
    }
}
