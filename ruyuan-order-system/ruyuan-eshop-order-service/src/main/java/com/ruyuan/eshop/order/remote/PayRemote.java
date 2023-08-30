package com.ruyuan.eshop.order.remote;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
import com.ruyuan.eshop.pay.api.PayApi;
import com.ruyuan.eshop.pay.domain.dto.PayOrderDTO;
import com.ruyuan.eshop.pay.domain.request.PayOrderRequest;
import com.ruyuan.eshop.pay.domain.request.PayRefundRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * 支付服务远程接口
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class PayRemote {

    /**
     * 支付服务
     */
    @DubboReference(version = "1.0.0", retries = 0)
    private PayApi payApi;

    /**
     * 调用支付系统进行预支付下单
     * @param payOrderRequest
     */
    public PayOrderDTO payOrder(PayOrderRequest payOrderRequest) {
        JsonResult<PayOrderDTO> jsonResult = payApi.payOrder(payOrderRequest);
        if (!jsonResult.getSuccess()) {
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_PRE_PAY_ERROR);
        }
        return jsonResult.getData();
    }

    /**
     * 调用支付系统执行退款
     * @param payRefundRequest
     */
    public void executeRefund(PayRefundRequest payRefundRequest) {
        JsonResult<Boolean> jsonResult = payApi.executeRefund(payRefundRequest);
        if (!jsonResult.getSuccess()) {
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_REFUND_AMOUNT_FAILED);
        }
    }

}