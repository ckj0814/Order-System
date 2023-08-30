package com.ruyuan.eshop.order.remote;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.fulfill.api.FulfillApi;
import com.ruyuan.eshop.fulfill.domain.request.CancelFulfillRequest;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * 履约服务远程接口
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class FulfillRemote {

    @DubboReference(version = "1.0.0")
    private FulfillApi fulfillApi;

    /**
     * 取消订单履约
     * @param cancelFulfillRequest
     */
    public void cancelFulfill(CancelFulfillRequest cancelFulfillRequest) {
        JsonResult<Boolean> jsonResult = fulfillApi.cancelFulfill(cancelFulfillRequest);
        if (!jsonResult.getSuccess()) {
            throw new OrderBizException(OrderErrorCodeEnum.CANCEL_ORDER_FULFILL_ERROR);
        }
    }
}