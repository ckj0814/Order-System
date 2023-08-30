package com.ruyuan.eshop.inventory.service;

import com.ruyuan.eshop.inventory.domain.request.*;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public interface InventoryService {

    /**
     * 扣减商品库存
     *
     * @param deductProductStockRequest
     * @return
     */
    Boolean deductProductStock(DeductProductStockRequest deductProductStockRequest);

    /**
     * 释放商品库存
     */
    Boolean releaseProductStock(ReleaseProductStockRequest releaseProductStockRequest);

    /**
     * 新增商品库存
     *
     * @param request
     * @return
     */
    Boolean addProductStock(AddProductStockRequest request);

    /**
     * 调整商品库存
     *
     * @param request
     * @return
     */
    Boolean modifyProductStock(ModifyProductStockRequest request);

    /**
     * 同步商品sku库存数据到缓存
     *
     * @param request
     * @return
     */
    Boolean syncStockToCache(SyncStockToCacheRequest request);
}
