package com.ruyuan.eshop.inventory.service.impl;

import com.ruyuan.eshop.common.redis.RedisCache;
import com.ruyuan.eshop.inventory.cache.CacheSupport;
import com.ruyuan.eshop.inventory.dao.ProductStockDAO;
import com.ruyuan.eshop.inventory.domain.entity.ProductStockDO;
import com.ruyuan.eshop.inventory.domain.request.AddProductStockRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 添加商品库存处理器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class AddProductStockProcessor {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ProductStockDAO productStockDAO;

    /**
     * 执行添加商品库存逻辑
     *
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    public void doAdd(AddProductStockRequest request) {
        //1、构造商品库存
        ProductStockDO productStock = buildProductStock(request);

        //2、保存商品库存到mysql
        productStockDAO.save(productStock);

        //3、保存商品库存到redis
        addStockToRedis(productStock);
    }

    /**
     * 保存商品库存到redis
     *
     * @param productStock
     */
    public void addStockToRedis(ProductStockDO productStock) {
        String productStockKey = CacheSupport.buildProductStockKey(productStock.getSkuCode());
        Map<String, String> productStockValue = CacheSupport
                .buildProductStockValue(productStock.getSaleStockQuantity(), productStock.getSaledStockQuantity());
        redisCache.hPutAll(productStockKey, productStockValue);
    }

    private ProductStockDO buildProductStock(AddProductStockRequest request) {
        ProductStockDO productStock = new ProductStockDO();
        productStock.setSkuCode(request.getSkuCode());
        productStock.setSaleStockQuantity(request.getSaleStockQuantity());
        productStock.setSaledStockQuantity(0L);
        return productStock;
    }
}
