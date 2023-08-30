package com.ruyuan.eshop.order;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.order.api.AfterSaleApi;
import com.ruyuan.eshop.order.domain.dto.LackDTO;
import com.ruyuan.eshop.order.domain.request.LackItemRequest;
import com.ruyuan.eshop.order.domain.request.LackRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest(classes = OrderApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderLackServiceTest {

    @DubboReference(version = "1.0.0")
    private AfterSaleApi api;

    @Test
    public void lockItem() throws Exception {
        LackRequest lackRequest = new LackRequest();
        lackRequest.setOrderId("1011250000000010000");
        lackRequest.setUserId("user_id_001");

        Set<LackItemRequest> itemRequests = new HashSet<>();
        itemRequests.add(new LackItemRequest("10101010", 1));
        lackRequest.setLackItems(itemRequests);

        JsonResult<LackDTO> jsonResult = api.lockItem(lackRequest);
        System.out.println("jsonResult=" + JSONObject.toJSONString(jsonResult));
    }


}
