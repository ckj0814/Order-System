package com.ruyuan.eshop.inventory.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.inventory.dao.ProductStockDAO;
import com.ruyuan.eshop.inventory.dao.ProductStockLogDAO;
import com.ruyuan.eshop.inventory.domain.entity.ProductStockDO;
import com.ruyuan.eshop.inventory.domain.request.AddProductStockRequest;
import com.ruyuan.eshop.inventory.domain.request.InitMeasureDataRequest;
import com.ruyuan.eshop.inventory.domain.request.ModifyProductStockRequest;
import com.ruyuan.eshop.inventory.domain.request.SyncStockToCacheRequest;
import com.ruyuan.eshop.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 正向下单流程接口冒烟测试
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductStockDAO productStockDAO;

    @Autowired
    private ProductStockLogDAO productStockLogDAO;

    /**
     * 新增商品库存
     *
     * @return
     */
    @PostMapping("/addProductStock")
    public JsonResult<Boolean> addProductStock(@RequestBody AddProductStockRequest request) {
        inventoryService.addProductStock(request);
        return JsonResult.buildSuccess(true);
    }

    /**
     * 调整商品库存
     *
     * @return
     */
    @PostMapping("/modifyProductStock")
    public JsonResult<Boolean> modifyProductStock(@RequestBody ModifyProductStockRequest request) {
        inventoryService.modifyProductStock(request);
        return JsonResult.buildSuccess(true);
    }

    /**
     * 同步商品sku库存数据到缓存
     *
     * @return
     */
    @PostMapping("/syncStockToCache")
    public JsonResult<Boolean> syncStockToCache(@RequestBody SyncStockToCacheRequest request) {
        inventoryService.syncStockToCache(request);
        return JsonResult.buildSuccess(true);
    }


    /**
     * 初始化压测数据
     * @return
     */
    @PostMapping("/initMeasureData")
    public JsonResult<Boolean> initMeasureData(@RequestBody InitMeasureDataRequest request) {
        List<String> skuCodes = request.getSkuCodes();

        if(CollectionUtils.isNotEmpty(skuCodes)) {

            // 初始化压测库存数据数据
            productStockDAO.initMeasureInventoryData(skuCodes);

            for(String skuCode : skuCodes) {
                SyncStockToCacheRequest syncStockToCacheRequest = new SyncStockToCacheRequest();
                syncStockToCacheRequest.setSkuCode(skuCode);
                // 同步缓存
                inventoryService.syncStockToCache(syncStockToCacheRequest);
            }
            productStockLogDAO.remove(new LambdaUpdateWrapper<>());
        }

        return JsonResult.buildSuccess(true);
    }


}
