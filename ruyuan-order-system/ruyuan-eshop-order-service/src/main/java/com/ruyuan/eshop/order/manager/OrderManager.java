package com.ruyuan.eshop.order.manager;

import com.ruyuan.eshop.market.domain.dto.CalculateOrderAmountDTO;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import com.ruyuan.eshop.order.domain.entity.OrderPaymentDetailDO;
import com.ruyuan.eshop.order.domain.request.CreateOrderRequest;
import com.ruyuan.eshop.order.domain.request.PayCallbackRequest;
import com.ruyuan.eshop.product.domain.dto.ProductSkuDTO;

import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public interface OrderManager {

    /**
     * 支付回调更新订单状态
     *
     * @param payCallbackRequest
     * @param orderInfoDO
     * @param orderPaymentDetailDO
     */
    void updateOrderStatusPaid(PayCallbackRequest payCallbackRequest,
                               OrderInfoDO orderInfoDO,
                               OrderPaymentDetailDO orderPaymentDetailDO);


    /**
     * 生成订单
     *
     * @param createOrderRequest
     * @param productSkuList
     * @param calculateOrderAmountDTO
     */
    void createOrder(CreateOrderRequest createOrderRequest, List<ProductSkuDTO> productSkuList, CalculateOrderAmountDTO calculateOrderAmountDTO);

}