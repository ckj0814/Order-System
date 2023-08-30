package com.ruyuan.eshop.order.service;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.common.message.ActualRefundMessage;
import com.ruyuan.eshop.order.domain.request.*;


/**
 * 订单逆向售后业务接口
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface OrderAfterSaleService {

    /**
     * 取消订单/超时未支付取消 入口
     * <p>
     * 有3个调用的地方：
     * 1、用户手动取消，订单出库状态之前都可以取消
     * 2、消费正向生单之后的MQ取消，要先判断支付状态，未支付才取消。
     * 3、定时任务定时扫描，超过30分钟，未支付才取消
     */
    JsonResult<Boolean> cancelOrder(CancelOrderRequest cancelOrderRequest);

    /**
     * 取消订单逻辑
     */
    JsonResult<Boolean> executeCancelOrder(CancelOrderRequest cancelOrderRequest, String orderId);

    /**
     * 取消订单/超时未支付取消 执行 退款前计算金额、记录售后信息等准备工作
     */
    JsonResult<Boolean> processCancelOrder(CancelOrderAssembleRequest cancelOrderAssembleRequest);

    /**
     * 执行退款
     */
    JsonResult<Boolean> refundMoney(ActualRefundMessage actualRefundMessage);

    /**
     * 支付退款回调 入口
     */
    JsonResult<Boolean> receivePaymentRefundCallback(RefundCallbackRequest payRefundCallbackRequest);

    /**
     * 处理售后申请 入口
     */
    JsonResult<Boolean> processApplyAfterSale(ReturnGoodsOrderRequest returnGoodsOrderRequest);

    /**
     * 发送退款短信
     */
    JsonResult<Boolean> sendRefundMobileMessage(String orderId);

    /**
     * 发送APP通知
     */
    JsonResult<Boolean> sendRefundAppMessage(String orderId);

    /**
     * 撤销售后申请
     */
    void revokeAfterSale(RevokeAfterSaleRequest request);

    /**
     * 接收客服审核拒绝结果 入口
     */
    void receiveCustomerAuditReject(CustomerAuditAssembleRequest customerAuditAssembleResult);

    /**
     * 接收客服审核通过结果 入口
     */
    void receiveCustomerAuditAccept(CustomerAuditAssembleRequest customerAuditAssembleResult);

    /**
     * 查询售后信息的客服审核状态
     */
    Integer findCustomerAuditAfterSaleStatus(Long afterSaleId);

}
