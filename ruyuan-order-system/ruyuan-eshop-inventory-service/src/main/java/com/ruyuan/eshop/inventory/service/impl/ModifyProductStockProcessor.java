package com.ruyuan.eshop.inventory.service.impl;

import com.ruyuan.eshop.common.redis.RedisCache;
import com.ruyuan.eshop.inventory.cache.CacheSupport;
import com.ruyuan.eshop.inventory.cache.LuaScript;
import com.ruyuan.eshop.inventory.dao.ProductStockDAO;
import com.ruyuan.eshop.inventory.domain.entity.ProductStockDO;
import com.ruyuan.eshop.inventory.exception.InventoryBizException;
import com.ruyuan.eshop.inventory.exception.InventoryErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * 调整商品库存处理器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class ModifyProductStockProcessor {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ProductStockDAO productStockDAO;

    @Autowired
    private AddProductStockProcessor addProductStockProcessor;

    /**
     * 调整商品库存
     */
    @Transactional(rollbackFor = Exception.class)
    public void doModify(ProductStockDO productStock, Long stockIncremental) {

        //1、更新mysql商品可销售库存数量
        String skuCode = productStock.getSkuCode();
        Long originSaleStockQuantity = productStock.getSaleStockQuantity();
        int num = productStockDAO.modifyProductStock(skuCode, originSaleStockQuantity, stockIncremental);
        if (num <= 0) {
            throw new InventoryBizException(InventoryErrorCodeEnum.MODIFY_PRODUCT_SKU_STOCK_ERROR);
        }

        //2、lua脚本更新redis商品可销售库存数量
        String luaScript = LuaScript.MODIFY_PRODUCT_STOCK;
        String productStockKey = CacheSupport.buildProductStockKey(skuCode);
        String saleStockKey = CacheSupport.SALE_STOCK;

        Long result = redisCache.execute(new DefaultRedisScript<>(luaScript, Long.class),
                Arrays.asList(productStockKey, saleStockKey), String.valueOf(originSaleStockQuantity), String.valueOf(stockIncremental));
        if (result < 0) {
            //redis更新异常，以mysql的数据为准
            redisCache.delete(saleStockKey);
            ProductStockDO productStockInDB = productStockDAO.getBySkuCode(skuCode);
            addProductStockProcessor.addStockToRedis(productStockInDB);
        }
    }


}
