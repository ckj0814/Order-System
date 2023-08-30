package com.ruyuan.eshop.order;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.page.PagingInfo;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderDetailDTO;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderListDTO;
import com.ruyuan.eshop.order.domain.query.AfterSaleQuery;
import com.ruyuan.eshop.order.service.AfterSaleQueryService;
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
public class AfterSaleQueryServiceTest {

    @Autowired
    private AfterSaleQueryService afterSaleQueryService;

    @Test
    public void test() {
        AfterSaleQuery query = new AfterSaleQuery();
        Set<Long> afterSaleIds = new HashSet<>();
        afterSaleIds.add(2021112837104128002L);
        afterSaleIds.add(2021112837103888002L);
        query.setAfterSaleIds(afterSaleIds);
        PagingInfo<AfterSaleOrderListDTO> result = afterSaleQueryService.executeListQuery(query);

        System.out.println(JSONObject.toJSONString(result));
        System.out.println(result.getList().size());
    }

    @Test
    public void test1() {

        AfterSaleQuery query = new AfterSaleQuery();

        Set<String> orderIds = new HashSet<>();
        orderIds.add("11");
        query.setOrderIds(orderIds);

        Set<Integer> orderTypes = new HashSet<>();
        orderTypes.add(1);
        query.setOrderTypes(orderTypes);

        Set<Integer> afterSaleStatus = new HashSet<>();
        afterSaleStatus.add(1);
        query.setAfterSaleStatus(afterSaleStatus);

        Set<Integer> applySources = new HashSet<>();
        applySources.add(1);
        query.setApplySources(applySources);

        Set<Integer> afterSaleTypes = new HashSet<>();
        afterSaleTypes.add(1);
        query.setAfterSaleTypes(afterSaleTypes);


        Set<Long> afterSaleIds = new HashSet<>();
        afterSaleIds.add(1L);
        query.setAfterSaleIds(afterSaleIds);

        Set<String> userIds = new HashSet<>();
        userIds.add("1");
        query.setUserIds(userIds);

        Set<String> skuCodes = new HashSet<>();
        skuCodes.add("1");
        query.setSkuCodes(skuCodes);

        query.setCreatedTimeInterval(Pair.of(new Date(), new Date()));
        query.setApplyTimeInterval(Pair.of(new Date(), new Date()));
        query.setReviewTimeInterval(Pair.of(new Date(), new Date()));
        query.setRefundPayTimeInterval(Pair.of(new Date(), new Date()));
        query.setRefundAmountInterval(Pair.of(1, 1));


        PagingInfo<AfterSaleOrderListDTO> result = afterSaleQueryService.executeListQuery(query);

        System.out.println(JSONObject.toJSONString(result));
        System.out.println(result.getList().size());
    }

    @Test
    public void afterSaleDetail() throws Exception {
        Long afterSaleId = 2021112837103888002L;
        AfterSaleOrderDetailDTO afterSaleOrderDetailDTO = afterSaleQueryService.afterSaleDetail(afterSaleId);

        System.out.println(JSONObject.toJSONString(afterSaleOrderDetailDTO));
    }

}
