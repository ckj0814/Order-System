package com.ruyuan.eshop.customer.remote;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.customer.domain.request.CustomerReceiveAfterSaleRequest;
import com.ruyuan.eshop.customer.domain.request.CustomerReviewReturnGoodsRequest;
import com.ruyuan.eshop.customer.exception.CustomerBizException;
import com.ruyuan.eshop.order.api.AfterSaleApi;
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
     * 接收客服的审核结果
     */
    public JsonResult<Boolean> receiveCustomerAuditResult(CustomerReviewReturnGoodsRequest customerReviewReturnGoodsRequest) {
        JsonResult<Boolean> jsonResult = afterSaleApi.receiveCustomerAuditResult(customerReviewReturnGoodsRequest);
        if (!jsonResult.getSuccess()) {
            throw new CustomerBizException(jsonResult.getErrorCode(), jsonResult.getErrorMessage());
        }
        return jsonResult;
    }

    /**
     * 客服系统查询售后支付单信息
     */
    public JsonResult<Long> customerFindAfterSaleRefundInfo(CustomerReceiveAfterSaleRequest customerReceiveAfterSaleRequest) {
        JsonResult<Long> jsonResult = afterSaleApi.customerFindAfterSaleRefundInfo(customerReceiveAfterSaleRequest);
        if (!jsonResult.getSuccess()) {
            throw new CustomerBizException(jsonResult.getErrorCode(), jsonResult.getErrorMessage());
        }
        return jsonResult;
    }
}
