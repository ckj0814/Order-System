package com.ruyuan.eshop.inventory.api;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.inventory.domain.request.DeductProductStockRequest;
import com.ruyuan.eshop.inventory.domain.request.ReleaseProductStockRequest;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public interface InventoryApi {

    /**
     * 扣减商品库存
     *
     * @param deductProductStockRequest
     * @return
     */
    JsonResult<Boolean> deductProductStock(DeductProductStockRequest deductProductStockRequest);

    /**
     * 取消订单 释放商品库存
     */
    JsonResult<Boolean> cancelOrderReleaseProductStock(ReleaseProductStockRequest releaseProductStockRequest);
}