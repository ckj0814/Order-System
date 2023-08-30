package com.ruyuan.eshop.inventory;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.redis.RedisCache;
import com.ruyuan.eshop.inventory.cache.CacheSupport;
import com.ruyuan.eshop.inventory.domain.request.*;
import com.ruyuan.eshop.inventory.service.InventoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = InventoryApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private RedisCache redisCache;

    @Test
    public void deleteProductStock() {
        String skuCode = "test001";
        String productStockKey = CacheSupport.buildProductStockKey(skuCode);
        redisCache.delete(productStockKey);
    }

    @Test
    public void addProductStock() throws Exception {

        String skuCode = "test002";
        Long saleStockQuantity = 10000L;
        String productStockKey = CacheSupport.buildProductStockKey(skuCode);

        Map<String, String> productStockValue = redisCache.hGetAll(productStockKey);

        System.out.println("before productStockValue=" + JSONObject.toJSONString(productStockValue));

        AddProductStockRequest request = new AddProductStockRequest();
        request.setSkuCode(skuCode);
        request.setSaleStockQuantity(saleStockQuantity);

        inventoryService.addProductStock(request);

        productStockValue = redisCache.hGetAll(productStockKey);
        System.out.println("after productStockValue=" + JSONObject.toJSONString(productStockValue));
    }

    @Test
    public void modifyProductStock() throws Exception {
        String skuCode = "test001";
        Long stockIncremental = 100L;
        String productStockKey = CacheSupport.buildProductStockKey(skuCode);

        Map<String, String> productStockValue = redisCache.hGetAll(productStockKey);

        System.out.println("before productStockValue=" + JSONObject.toJSONString(productStockValue));
        ModifyProductStockRequest request = new ModifyProductStockRequest();
        request.setSkuCode(skuCode);
        request.setStockIncremental(stockIncremental);
        inventoryService.modifyProductStock(request);

        productStockValue = redisCache.hGetAll(productStockKey);
        System.out.println("after productStockValue=" + JSONObject.toJSONString(productStockValue));
    }

    @Test
    public void deductProductStock() throws Exception {
        String skuCode1 = "test001";
        Integer saleQuantity1 = 10;
        String productStockKey1 = CacheSupport.buildProductStockKey(skuCode1);

        String skuCode2 = "test002";
        Integer saleQuantity2 = 1;
        String productStockKey2 = CacheSupport.buildProductStockKey(skuCode2);

        Map<String, String> productStockValue1 = redisCache.hGetAll(productStockKey1);
        Map<String, String> productStockValue2 = redisCache.hGetAll(productStockKey2);
        System.out.println("before productStockValue1=" + JSONObject.toJSONString(productStockValue1));
        System.out.println("before productStockValue2=" + JSONObject.toJSONString(productStockValue2));

        DeductProductStockRequest request = new DeductProductStockRequest();
        request.setBusinessIdentifier(1);
        request.setOrderId("1");
        List<DeductProductStockRequest.OrderItemRequest> orderItemRequests = new ArrayList<>();
        request.setOrderItemRequestList(orderItemRequests);

        DeductProductStockRequest.OrderItemRequest itemRequest1 = new DeductProductStockRequest.OrderItemRequest();
        itemRequest1.setSkuCode(skuCode1);
        itemRequest1.setSaleQuantity(saleQuantity1);
        orderItemRequests.add(itemRequest1);

        DeductProductStockRequest.OrderItemRequest itemRequest2 = new DeductProductStockRequest.OrderItemRequest();
        itemRequest2.setSkuCode(skuCode2);
        itemRequest2.setSaleQuantity(saleQuantity2);
        orderItemRequests.add(itemRequest2);

        try {
            inventoryService.deductProductStock(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        productStockValue1 = redisCache.hGetAll(productStockKey1);
        productStockValue2 = redisCache.hGetAll(productStockKey2);
        System.out.println("after productStockValue1=" + JSONObject.toJSONString(productStockValue1));
        System.out.println("after productStockValue2=" + JSONObject.toJSONString(productStockValue2));
    }

    @Test
    public void releaseProductStock() throws Exception {

        String skuCode1 = "test001";
        Integer saleQuantity1 = 10;
        String productStockKey1 = CacheSupport.buildProductStockKey(skuCode1);

        String skuCode2 = "test002";
        Integer saleQuantity2 = 1;
        String productStockKey2 = CacheSupport.buildProductStockKey(skuCode2);

        Map<String, String> productStockValue1 = redisCache.hGetAll(productStockKey1);
        Map<String, String> productStockValue2 = redisCache.hGetAll(productStockKey2);
        System.out.println("before productStockValue1=" + JSONObject.toJSONString(productStockValue1));
        System.out.println("before productStockValue2=" + JSONObject.toJSONString(productStockValue2));

        ReleaseProductStockRequest request = new ReleaseProductStockRequest();
        request.setOrderId("1");
        List<ReleaseProductStockRequest.OrderItemRequest> orderItemRequests = new ArrayList<>();
        request.setOrderItemRequestList(orderItemRequests);

        ReleaseProductStockRequest.OrderItemRequest itemRequest1 = new ReleaseProductStockRequest.OrderItemRequest();
        itemRequest1.setSkuCode(skuCode1);
        itemRequest1.setSaleQuantity(saleQuantity1);
        orderItemRequests.add(itemRequest1);

        ReleaseProductStockRequest.OrderItemRequest itemRequest2 = new ReleaseProductStockRequest.OrderItemRequest();
        itemRequest2.setSkuCode(skuCode2);
        itemRequest2.setSaleQuantity(saleQuantity2);
        orderItemRequests.add(itemRequest2);

        try {
            inventoryService.releaseProductStock(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        productStockValue1 = redisCache.hGetAll(productStockKey1);
        productStockValue2 = redisCache.hGetAll(productStockKey2);
        System.out.println("after productStockValue1=" + JSONObject.toJSONString(productStockValue1));
        System.out.println("after productStockValue2=" + JSONObject.toJSONString(productStockValue2));
    }

    @Test
    public void syncStockToCache() throws Exception {

        String skuCode1 = "test001";
        String productStockKey1 = CacheSupport.buildProductStockKey(skuCode1);

        String skuCode2 = "test002";
        String productStockKey2 = CacheSupport.buildProductStockKey(skuCode2);

        SyncStockToCacheRequest request = new SyncStockToCacheRequest();
        request.setSkuCode(skuCode1);
        inventoryService.syncStockToCache(request);

        request.setSkuCode(skuCode2);
        inventoryService.syncStockToCache(request);


        Map<String, String> productStockValue1 = redisCache.hGetAll(productStockKey1);
        Map<String, String> productStockValue2 = redisCache.hGetAll(productStockKey2);

        System.out.println("after productStockValue1=" + JSONObject.toJSONString(productStockValue1));
        System.out.println("after productStockValue2=" + JSONObject.toJSONString(productStockValue2));
    }


}
