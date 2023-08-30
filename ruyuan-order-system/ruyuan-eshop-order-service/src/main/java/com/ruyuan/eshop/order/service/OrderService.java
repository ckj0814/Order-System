package com.ruyuan.eshop.order.service;

import com.ruyuan.eshop.order.domain.dto.CreateOrderDTO;
import com.ruyuan.eshop.order.domain.dto.GenOrderIdDTO;
import com.ruyuan.eshop.order.domain.dto.PrePayOrderDTO;
import com.ruyuan.eshop.order.domain.request.*;

import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public interface OrderService {

    /**
     * 生成订单号
     *
     * @param genOrderIdRequest 生成订单号入参
     * @return 订单号
     */
    GenOrderIdDTO genOrderId(GenOrderIdRequest genOrderIdRequest);

    /**
     * 提交订单/生成订单
     *
     * @param createOrderRequest 提交订单请求入参
     * @return 订单号
     */
    CreateOrderDTO createOrder(CreateOrderRequest createOrderRequest);

    /**
     * 预支付订单
     *
     * @param prePayOrderRequest
     * @return
     */
    PrePayOrderDTO prePayOrder(PrePayOrderRequest prePayOrderRequest);

    /**
     * 支付回调
     *
     * @param payCallbackRequest
     * @return
     */
    void payCallback(PayCallbackRequest payCallbackRequest);

    /**
     * 移除订单
     *
     * @param orderIds
     * @return
     */
    boolean removeOrders(List<String> orderIds);

    /**
     * 调整订单配送地址
     *
     * @param request
     */
    boolean adjustDeliveryAddress(AdjustDeliveryAddressRequest request);
}