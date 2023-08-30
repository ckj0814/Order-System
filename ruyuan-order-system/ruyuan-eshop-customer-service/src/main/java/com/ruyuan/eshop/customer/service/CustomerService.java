package com.ruyuan.eshop.customer.service;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.customer.domain.request.CustomerReceiveAfterSaleRequest;
import com.ruyuan.eshop.customer.domain.request.CustomerReviewReturnGoodsRequest;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public interface CustomerService {
    /**
     * 客服接收订单系统的售后申请
     */
    JsonResult<Boolean> receiveAfterSale(CustomerReceiveAfterSaleRequest customerReceiveAfterSaleRequest);

    /**
     * 客服审核
     */
    JsonResult<Boolean> customerAudit(CustomerReviewReturnGoodsRequest customerReviewReturnGoodsRequest);

}
