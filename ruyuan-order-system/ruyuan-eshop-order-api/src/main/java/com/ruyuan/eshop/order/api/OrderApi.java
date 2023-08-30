package com.ruyuan.eshop.order.api;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.order.domain.dto.*;
import com.ruyuan.eshop.order.domain.request.*;

/**
 * 订单中心-正向下单业务接口
 *
 * @author zhonghuashishan
 */
public interface OrderApi {

    /**
     * 生成订单号接口
     *
     * @param genOrderIdRequest 生成订单号入参
     * @return 订单号
     */
    JsonResult<GenOrderIdDTO> genOrderId(GenOrderIdRequest genOrderIdRequest);


    /**
     * 提交订单接口
     *
     * @param createOrderRequest 提交订单请求入参
     * @return 订单号
     */
    JsonResult<CreateOrderDTO> createOrder(CreateOrderRequest createOrderRequest);


    /**
     * 预支付订单接口
     *
     * @param prePayOrderRequest 预支付订单请求入参
     * @return
     */
    JsonResult<PrePayOrderDTO> prePayOrder(PrePayOrderRequest prePayOrderRequest);

    /**
     * 支付回调接口
     *
     * @param payCallbackRequest 支付系统回调入参
     * @return
     */
    JsonResult<Boolean> payCallback(PayCallbackRequest payCallbackRequest);


    /**
     * 移除订单
     *
     * @param removeOrderRequest 移除订单请求入参
     * @return
     */
    JsonResult<RemoveOrderDTO> removeOrders(RemoveOrderRequest removeOrderRequest);


    /**
     * 修改地址
     *
     * @param adjustDeliveryAddressRequest 修改地址请求入参
     * @return
     */
    JsonResult<AdjustDeliveryAddressDTO> adjustDeliveryAddress(AdjustDeliveryAddressRequest adjustDeliveryAddressRequest);

}
