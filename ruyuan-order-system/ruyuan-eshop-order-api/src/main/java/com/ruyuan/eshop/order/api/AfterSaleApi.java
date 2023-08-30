package com.ruyuan.eshop.order.api;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.customer.domain.request.CustomerReceiveAfterSaleRequest;
import com.ruyuan.eshop.customer.domain.request.CustomerReviewReturnGoodsRequest;
import com.ruyuan.eshop.order.domain.dto.LackDTO;
import com.ruyuan.eshop.order.domain.request.CancelOrderRequest;
import com.ruyuan.eshop.order.domain.request.LackRequest;
import com.ruyuan.eshop.order.domain.request.RefundCallbackRequest;
import com.ruyuan.eshop.order.domain.request.RevokeAfterSaleRequest;

/**
 * 订单中心-逆向售后业务接口
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface AfterSaleApi {

    /**
     * 取消订单/超时未支付取消
     */
    JsonResult<Boolean> cancelOrder(CancelOrderRequest cancelOrderRequest);

    /**
     * 缺品
     */
    JsonResult<LackDTO> lockItem(LackRequest request);

    /**
     * 取消订单支付退款回调
     */
    JsonResult<Boolean> refundCallback(RefundCallbackRequest payRefundCallbackRequest);

    /**
     * 接收客服的审核结果
     */
    JsonResult<Boolean> receiveCustomerAuditResult(CustomerReviewReturnGoodsRequest customerReviewReturnGoodsRequest);

    /**
     * 用户撤销售后申请
     */
    JsonResult<Boolean> revokeAfterSale(RevokeAfterSaleRequest request);

    /**
     * 提供给客服系统查询售后支付单信息
     */
    JsonResult<Long> customerFindAfterSaleRefundInfo(CustomerReceiveAfterSaleRequest customerReceiveAfterSaleRequest);

}