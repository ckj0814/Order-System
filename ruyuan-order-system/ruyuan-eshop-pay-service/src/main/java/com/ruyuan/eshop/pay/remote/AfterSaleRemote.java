package com.ruyuan.eshop.pay.remote;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.order.api.AfterSaleApi;
import com.ruyuan.eshop.order.domain.request.RefundCallbackRequest;
import com.ruyuan.eshop.pay.exception.PayBizException;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * 订单售后远程接口
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class AfterSaleRemote {

    @DubboReference(version = "1.0.0")
    private AfterSaleApi afterSaleApi;

    /**
     * 取消订单支付退款回调
     */
    public JsonResult<Boolean> refundCallback(RefundCallbackRequest payRefundCallbackRequest) {
        JsonResult<Boolean> jsonResult = afterSaleApi.refundCallback(payRefundCallbackRequest);
        if (!jsonResult.getSuccess()) {
            throw new PayBizException(jsonResult.getErrorCode(), jsonResult.getErrorMessage());
        }
        return jsonResult;
    }

}
