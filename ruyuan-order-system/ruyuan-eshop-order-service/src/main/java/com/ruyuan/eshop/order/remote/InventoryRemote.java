package com.ruyuan.eshop.order.remote;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.inventory.api.InventoryApi;
import com.ruyuan.eshop.inventory.domain.request.DeductProductStockRequest;
import com.ruyuan.eshop.order.exception.OrderBizException;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * 库存服务远程接口
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class InventoryRemote {

    /**
     * 库存服务
     */
    @DubboReference(version = "1.0.0", retries = 0)
    private InventoryApi inventoryApi;

    /**
     * 扣减订单条目库存
     * @param lockProductStockRequest
     */
    public void deductProductStock(DeductProductStockRequest lockProductStockRequest) {
        JsonResult<Boolean> jsonResult = inventoryApi.deductProductStock(lockProductStockRequest);
        // 检查锁定商品库存结果
        if (!jsonResult.getSuccess()) {
            throw new OrderBizException(jsonResult.getErrorCode(), jsonResult.getErrorMessage());
        }
    }
}