package com.ruyuan.eshop.order;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.page.PagingInfo;
import com.ruyuan.eshop.order.dao.OrderInfoDAO;
import com.ruyuan.eshop.order.domain.dto.OrderDetailDTO;
import com.ruyuan.eshop.order.domain.dto.OrderListDTO;
import com.ruyuan.eshop.order.domain.query.OrderQuery;
import com.ruyuan.eshop.order.service.OrderQueryService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest(classes = OrderApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderQueryServiceTest {

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private OrderInfoDAO orderInfoDAO;

    @Test
    public void test() {
        OrderQuery query = new OrderQuery();
        Set<String> orderIds = new HashSet<>();
        orderIds.add("1011250000000010000");
        orderIds.add("1011260000000020000");
        query.setOrderIds(orderIds);
        PagingInfo<OrderListDTO> result = orderQueryService.executeListQuery(query);

        System.out.println(JSONObject.toJSONString(result));
        System.out.println(result.getList().size());
    }

    @Test
    public void test1() {

        OrderQuery query = new OrderQuery();

        query.setBusinessIdentifier(1);
        Set<String> orderIds = new HashSet<>();
        orderIds.add("11");
        query.setOrderIds(orderIds);

        Set<Integer> orderTypes = new HashSet<>();
        orderTypes.add(1);
        query.setOrderTypes(orderTypes);

        Set<String> sellerId = new HashSet<>();
        sellerId.add("1");
        query.setSellerIds(sellerId);

        Set<String> parentOrderIds = new HashSet<>();
        parentOrderIds.add("1");
        query.setParentOrderIds(parentOrderIds);

        Set<String> userIds = new HashSet<>();
        userIds.add("1");
        query.setUserIds(userIds);

        Set<Integer> orderStatus = new HashSet<>();
        orderStatus.add(1);
        query.setOrderStatus(orderStatus);

        Set<String> receiverPhones = new HashSet<>();
        receiverPhones.add("1");
        query.setReceiverPhones(receiverPhones);

        Set<String> receiverNames = new HashSet<>();
        receiverNames.add("1");
        query.setReceiverNames(receiverNames);

        Set<String> tradeNos = new HashSet<>();
        tradeNos.add("1");
        query.setTradeNos(tradeNos);

        Set<String> skuCodes = new HashSet<>();
        skuCodes.add("1");
        query.setSkuCodes(skuCodes);

        Set<String> productNames = new HashSet<>();
        productNames.add("1");
        query.setProductNames(productNames);

        Pair<Date, Date> createdTimeInterval = Pair.of(new Date(), new Date());
        query.setCreatedTimeInterval(createdTimeInterval);
        Pair<Date, Date> payTimeInterval = Pair.of(new Date(), new Date());
        query.setPayTimeInterval(payTimeInterval);

        Pair<Integer, Integer> payAmountInterval = Pair.of(1, 1);
        query.setPayAmountInterval(payAmountInterval);

        query.setPageNo(2);
        query.setPageSize(100);

        PagingInfo<OrderListDTO> result = orderQueryService.executeListQuery(query);

        System.out.println(JSONObject.toJSONString(result));
    }

    @Test
    public void orderDetail() throws Exception {
        String orderId = "1011250000000010000";
        OrderDetailDTO orderDetailDTO = orderQueryService.orderDetail(orderId);
        System.out.println(JSONObject.toJSONString(orderDetailDTO));
    }
}
