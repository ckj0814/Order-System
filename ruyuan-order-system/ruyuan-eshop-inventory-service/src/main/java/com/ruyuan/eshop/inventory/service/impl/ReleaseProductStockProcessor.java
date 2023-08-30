package com.ruyuan.eshop.inventory.service.impl;

import com.ruyuan.eshop.common.redis.RedisCache;
import com.ruyuan.eshop.inventory.cache.CacheSupport;
import com.ruyuan.eshop.inventory.cache.LuaScript;
import com.ruyuan.eshop.inventory.dao.ProductStockDAO;
import com.ruyuan.eshop.inventory.dao.ProductStockLogDAO;
import com.ruyuan.eshop.inventory.domain.entity.ProductStockLogDO;
import com.ruyuan.eshop.inventory.enums.StockLogStatusEnum;
import com.ruyuan.eshop.inventory.exception.InventoryBizException;
import com.ruyuan.eshop.inventory.exception.InventoryErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * 释放商品库存处理器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class ReleaseProductStockProcessor {

    @Autowired
    private ProductStockDAO productStockDAO;

    @Autowired
    private ProductStockLogDAO productStockLogDAO;

    @Autowired
    private RedisCache redisCache;

    /**
     * 执行释放商品库存逻辑
     */
    @Transactional(rollbackFor = Exception.class)
    public void doRelease(String orderId, String skuCode, Integer saleQuantity, ProductStockLogDO productStockLog) {

        //1、执行mysql释放商品库存逻辑
        int nums = productStockDAO.releaseProductStock(skuCode, saleQuantity);
        if (nums <= 0) {
            throw new InventoryBizException(InventoryErrorCodeEnum.RELEASE_PRODUCT_SKU_STOCK_ERROR);
        }

        //2、更新库存日志的状态为"已释放"
        if (null != productStockLog) {
            productStockLogDAO.updateStatus(productStockLog.getId(), StockLogStatusEnum.RELEASED);
        }

        //3、执行redis库存释放逻辑
        String luaScript = LuaScript.RELEASE_PRODUCT_STOCK;
        String saleStockKey = CacheSupport.SALE_STOCK;
        String saledStockKey = CacheSupport.SALED_STOCK;
        String productStockKey = CacheSupport.buildProductStockKey(skuCode);

        Long result = redisCache.execute(new DefaultRedisScript<>(luaScript, Long.class),
                Arrays.asList(productStockKey, saleStockKey, saledStockKey), String.valueOf(saleQuantity));
        if (result < 0) {
            throw new InventoryBizException(InventoryErrorCodeEnum.MODIFY_PRODUCT_SKU_STOCK_ERROR);
        }

    }
}
