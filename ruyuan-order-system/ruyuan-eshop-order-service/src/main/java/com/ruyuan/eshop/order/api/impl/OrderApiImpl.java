package com.ruyuan.eshop.order.api.impl;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.common.utils.LoggerFormat;
import com.ruyuan.eshop.common.utils.ParamCheckUtil;
import com.ruyuan.eshop.order.api.OrderApi;
import com.ruyuan.eshop.order.constants.OrderConstants;
import com.ruyuan.eshop.order.domain.dto.*;
import com.ruyuan.eshop.order.domain.request.*;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
import com.ruyuan.eshop.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 订单中心接口
 *
 * @author zhonghuashishan
 */
@Slf4j
@DubboService(version = "1.0.0", interfaceClass = OrderApi.class, retries = 0)
public class OrderApiImpl implements OrderApi {

    @Autowired
    private OrderService orderService;

    /**
     * 生成订单号接口
     *
     * @param genOrderIdRequest 生成订单号入参
     * @return 订单号
     */
    @Override
    public JsonResult<GenOrderIdDTO> genOrderId(GenOrderIdRequest genOrderIdRequest) {
        try {
            String userId = genOrderIdRequest.getUserId();
            if (userId == null || "".equals(userId)) {
                return JsonResult.buildError(OrderErrorCodeEnum.USER_ID_IS_NULL);
            }
            GenOrderIdDTO genOrderIdDTO = orderService.genOrderId(genOrderIdRequest);
            return JsonResult.buildSuccess(genOrderIdDTO);
        } catch (OrderBizException e) {
            log.error("biz error", e);
            return JsonResult.buildError(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            log.error("system error", e);
            return JsonResult.buildError(e.getMessage());
        }
    }

    /**
     * 提交订单/生成订单接口
     *
     * @param createOrderRequest 提交订单请求入参
     * @return 订单号
     */
    @Override
    public JsonResult<CreateOrderDTO> createOrder(CreateOrderRequest createOrderRequest) {
        try {
            CreateOrderDTO createOrderDTO = orderService.createOrder(createOrderRequest);
            return JsonResult.buildSuccess(createOrderDTO);
        } catch (OrderBizException e) {
            log.error(LoggerFormat.build()
                    .remark("biz error")
                    .finish(), e);
            return JsonResult.buildError(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            log.error(LoggerFormat.build()
                    .remark("system error")
                    .finish(), e);
            return JsonResult.buildError(e.getMessage());
        }
    }

    /**
     * 预支付订单接口
     *
     * @param prePayOrderRequest 预支付订单请求入参
     * @return
     */
    @Override
    public JsonResult<PrePayOrderDTO> prePayOrder(PrePayOrderRequest prePayOrderRequest) {
        try {
            PrePayOrderDTO prePayOrderDTO = orderService.prePayOrder(prePayOrderRequest);
            return JsonResult.buildSuccess(prePayOrderDTO);
        } catch (OrderBizException e) {
            log.error("biz error", e);
            return JsonResult.buildError(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            log.error("system error", e);
            return JsonResult.buildError(e.getMessage());
        }

    }

    /**
     * 支付回调接口
     *
     * @param payCallbackRequest 支付系统回调入参
     * @return
     */
    @Override
    public JsonResult<Boolean> payCallback(PayCallbackRequest payCallbackRequest) {
        try {
            orderService.payCallback(payCallbackRequest);
            return JsonResult.buildSuccess(true);
        } catch (OrderBizException e) {
            log.error("biz error", e);
            return JsonResult.buildError(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            log.error("error", e);
            return JsonResult.buildError(e.getMessage());
        }
    }

    /**
     * 移除订单
     *
     * @param removeOrderRequest 移除订单请求入参
     * @return
     */
    @Override
    public JsonResult<RemoveOrderDTO> removeOrders(RemoveOrderRequest removeOrderRequest) {
        try {

            //1、参数校验
            Set<String> orderIdSet = removeOrderRequest.getOrderIds();
            ParamCheckUtil.checkCollectionNonEmpty(orderIdSet, OrderErrorCodeEnum.ORDER_ID_IS_NULL);
            ParamCheckUtil.checkSetMaxSize(orderIdSet, OrderConstants.REMOVE_ORDER_MAX_COUNT,
                    OrderErrorCodeEnum.COLLECTION_PARAM_CANNOT_BEYOND_MAX_SIZE, "orderIds"
                    , OrderConstants.REMOVE_ORDER_MAX_COUNT);

            //2、执行具体的移除订单逻辑
            List<String> orderIds = new ArrayList<>(orderIdSet);
            orderService.removeOrders(orderIds);

            return JsonResult.buildSuccess(new RemoveOrderDTO(true));
        } catch (OrderBizException e) {
            log.error("biz error", e);
            return JsonResult.buildError(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            log.error("error", e);
            return JsonResult.buildError(e.getMessage());
        }
    }

    /**
     * 修改地址
     *
     * @param adjustDeliveryAddressRequest 修改地址请求入参
     * @return
     */
    @Override
    public JsonResult<AdjustDeliveryAddressDTO> adjustDeliveryAddress(AdjustDeliveryAddressRequest adjustDeliveryAddressRequest) {
        try {

            //1、参数校验
            ParamCheckUtil.checkStringNonEmpty(adjustDeliveryAddressRequest.getOrderId(), OrderErrorCodeEnum.ORDER_ID_IS_NULL);

            //2、执行具体的调整订单配送地址逻辑
            orderService.adjustDeliveryAddress(adjustDeliveryAddressRequest);

            return JsonResult.buildSuccess(new AdjustDeliveryAddressDTO(true));
        } catch (OrderBizException e) {
            log.error("biz error", e);
            return JsonResult.buildError(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            log.error("error", e);
            return JsonResult.buildError(e.getMessage());
        }
    }
}
