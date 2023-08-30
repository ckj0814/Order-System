package com.ruyuan.eshop.inventory.service.impl;


import com.ruyuan.eshop.common.redis.RedisCache;
import com.ruyuan.eshop.common.utils.ParamCheckUtil;
import com.ruyuan.eshop.inventory.cache.CacheSupport;
import com.ruyuan.eshop.inventory.dao.ProductStockDAO;
import com.ruyuan.eshop.inventory.domain.entity.ProductStockDO;
import com.ruyuan.eshop.inventory.exception.InventoryErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 同步商品sku库存到缓存处理器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class SyncStockToCacheProcessor {

    @Autowired
    private ProductStockDAO productStockDAO;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AddProductStockProcessor addProductStockProcessor;

    /**
     * 执行扣减商品库存逻辑
     */
    public void doSync(String skuCode) {
        //1、查询商品库存
        ProductStockDO productStock = productStockDAO.getBySkuCode(skuCode);
        if(null != productStock) {
            //2、删除缓存数据
            redisCache.delete(CacheSupport.buildProductStockKey(productStock.getSkuCode()));

            //3、保存商品库存到redis
            addProductStockProcessor.addStockToRedis(productStock);
        }
    }
}
