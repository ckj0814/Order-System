package com.ruyuan.eshop.inventory.api.impl;

import com.google.common.collect.Lists;
import com.ruyuan.eshop.common.constants.RedisLockKeyConstants;
import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.common.redis.RedisLock;
import com.ruyuan.eshop.inventory.api.InventoryApi;
import com.ruyuan.eshop.inventory.domain.request.DeductProductStockRequest;
import com.ruyuan.eshop.inventory.domain.request.ReleaseProductStockRequest;
import com.ruyuan.eshop.inventory.exception.InventoryBizException;
import com.ruyuan.eshop.inventory.exception.InventoryErrorCodeEnum;
import com.ruyuan.eshop.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@DubboService(version = "1.0.0", interfaceClass = InventoryApi.class, retries = 0)
@Slf4j
public class InventoryApiImpl implements InventoryApi {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private RedisLock redisLock;

    /**
     * 扣减商品库存
     *
     * @param deductProductStockRequest
     * @return
     */
    @Override
    public JsonResult<Boolean> deductProductStock(DeductProductStockRequest deductProductStockRequest) {
        try {
            Boolean result = inventoryService.deductProductStock(deductProductStockRequest);
            return JsonResult.buildSuccess(result);
        } catch (InventoryBizException e) {
            log.error("biz error", e);
            return JsonResult.buildError(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            log.error("system error", e);
            return JsonResult.buildError(e.getMessage());
        }
    }

    /**
     * 回滚库存
     */
    @Override
    public JsonResult<Boolean> cancelOrderReleaseProductStock(ReleaseProductStockRequest releaseProductStockRequest) {
        log.info("开始执行回滚库存,orderId:{}", releaseProductStockRequest.getOrderId());
        List<String> redisKeyList = Lists.newArrayList();
        //  分布式锁
        for (ReleaseProductStockRequest.OrderItemRequest orderItemRequest : releaseProductStockRequest.getOrderItemRequestList()) {
            //  lockKey: #MODIFY_PRODUCT_STOCK_KEY: skuCode
            String lockKey = RedisLockKeyConstants.MODIFY_PRODUCT_STOCK_KEY + orderItemRequest.getSkuCode();
            redisKeyList.add(lockKey);
        }
        boolean lock = redisLock.multiLock(redisKeyList);
        if (!lock) {
            throw new InventoryBizException(InventoryErrorCodeEnum.RELEASE_PRODUCT_SKU_STOCK_ERROR);
        }

        try {
            //  执行释放库存
            Boolean result = inventoryService.releaseProductStock(releaseProductStockRequest);
            return JsonResult.buildSuccess(result);
        } catch (InventoryBizException e) {
            log.error("biz error", e);
            return JsonResult.buildError(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            log.error("system error", e);
            return JsonResult.buildError(e.getMessage());
        } finally {
            // 释放分布式锁
            redisLock.unMultiLock(redisKeyList);
        }
    }
}