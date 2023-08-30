package com.ruyuan.eshop.pay.api;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.pay.domain.dto.PayOrderDTO;
import com.ruyuan.eshop.pay.domain.request.PayOrderRequest;
import com.ruyuan.eshop.pay.domain.request.PayRefundRequest;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public interface PayApi {

    /**
     * 支付订单
     *
     * @param payOrderRequest
     * @return
     */
    JsonResult<PayOrderDTO> payOrder(PayOrderRequest payOrderRequest);

    /**
     * 调用支付接口执行退款
     */
    JsonResult<Boolean> executeRefund(PayRefundRequest payRefundRequest);

}