package com.ruyuan.eshop.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.constants.RocketMqConstant;
import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.common.enums.OrderStatusChangeEnum;
import com.ruyuan.eshop.common.message.PaidOrderSuccessMessage;
import com.ruyuan.eshop.common.page.PagingInfo;
import com.ruyuan.eshop.fulfill.api.FulfillApi;
import com.ruyuan.eshop.fulfill.domain.event.OrderDeliveredWmsEvent;
import com.ruyuan.eshop.fulfill.domain.event.OrderOutStockWmsEvent;
import com.ruyuan.eshop.fulfill.domain.event.OrderSignedWmsEvent;
import com.ruyuan.eshop.fulfill.domain.request.ReceiveFulfillRequest;
import com.ruyuan.eshop.fulfill.domain.request.TriggerOrderWmsShipEventRequest;
import com.ruyuan.eshop.order.api.OrderApi;
import com.ruyuan.eshop.order.api.OrderQueryApi;
import com.ruyuan.eshop.order.dao.OrderInfoDAO;
import com.ruyuan.eshop.order.domain.dto.*;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import com.ruyuan.eshop.order.domain.query.AcceptOrderQuery;
import com.ruyuan.eshop.order.domain.request.*;
import com.ruyuan.eshop.order.mq.producer.DefaultProducer;
import com.ruyuan.eshop.order.service.impl.OrderFulFillServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 正向下单流程接口冒烟测试
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/order/test")
public class OrderTestController {

    /**
     * 订单服务
     */
    @DubboReference(version = "1.0.0", retries = 0)
    private OrderApi orderApi;

    @DubboReference(version = "1.0.0")
    private OrderQueryApi queryApi;

    @DubboReference(version = "1.0.0", retries = 0)
    private FulfillApi fulfillApi;

    @Autowired
    private DefaultProducer defaultProducer;

    @Autowired
    private OrderFulFillServiceImpl orderFulFillService;

    @Autowired
    private OrderInfoDAO orderInfoDAO;

    /**
     * 测试生成新的订单号
     *
     * @param genOrderIdRequest
     * @return
     */
    @PostMapping("/genOrderId")
    public JsonResult<GenOrderIdDTO> genOrderId(@RequestBody GenOrderIdRequest genOrderIdRequest) {
        JsonResult<GenOrderIdDTO> genOrderIdDTO = orderApi.genOrderId(genOrderIdRequest);
        return genOrderIdDTO;
    }

    /**
     * 测试提交订单
     *
     * @param createOrderRequest
     * @return
     */
    @PostMapping("/createOrder")
    public JsonResult<CreateOrderDTO> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        JsonResult<CreateOrderDTO> createOrderDTO = orderApi.createOrder(createOrderRequest);
        return createOrderDTO;
    }

    /**
     * 测试预支付订单
     *
     * @param prePayOrderRequest
     * @return
     */
    @PostMapping("/prePayOrder")
    public JsonResult<PrePayOrderDTO> prePayOrder(@RequestBody PrePayOrderRequest prePayOrderRequest) {
        JsonResult<PrePayOrderDTO> prePayOrderDTO = orderApi.prePayOrder(prePayOrderRequest);
        return prePayOrderDTO;
    }

    /**
     * 测试支付回调
     *
     * @param payCallbackRequest
     * @return
     */
    @PostMapping("/payCallback")
    public JsonResult<Boolean> payCallback(@RequestBody PayCallbackRequest payCallbackRequest) {
        JsonResult<Boolean> result = orderApi.payCallback(payCallbackRequest);
        return result;
    }

    /**
     * 移除订单
     *
     * @param request
     * @return
     */
    @PostMapping("/removeOrders")
    public JsonResult<RemoveOrderDTO> removeOrders(@RequestBody RemoveOrderRequest request) {
        JsonResult<RemoveOrderDTO> result = orderApi.removeOrders(request);
        return result;
    }

    /**
     * 调整订单配置地址
     *
     * @param request
     * @return
     */
    @PostMapping("/adjustDeliveryAddress")
    public JsonResult<AdjustDeliveryAddressDTO> adjustDeliveryAddress(@RequestBody AdjustDeliveryAddressRequest request) {
        JsonResult<AdjustDeliveryAddressDTO> result = orderApi.adjustDeliveryAddress(request);
        return result;
    }

    /**
     * 订单列表查询
     *
     * @param acceptOrderQuery
     * @return
     */
    @PostMapping("/listOrders")
    public JsonResult<PagingInfo<OrderListDTO>> listOrders(@RequestBody AcceptOrderQuery acceptOrderQuery) {
        JsonResult<PagingInfo<OrderListDTO>> result = queryApi.listOrders(acceptOrderQuery);
        return result;
    }

    /**
     * 订单详情
     *
     * @param orderId
     * @return
     */
    @GetMapping("/orderDetail")
    public JsonResult<OrderDetailDTO> orderDetail(String orderId) {
        JsonResult<OrderDetailDTO> result = queryApi.orderDetail(orderId);
        return result;
    }

    /**
     * 触发订单发货出库事件
     *
     * @param event
     * @return
     */
    @PostMapping("/triggerOutStockEvent")
    public JsonResult<Boolean> triggerOrderOutStockWmsEvent(@RequestParam("orderId") String orderId
            , @RequestParam("fulfillId") String fulfillId, @RequestBody OrderOutStockWmsEvent event) {
        log.info("orderId={},fulfillId={},event={}", orderId, fulfillId, JSONObject.toJSONString(event));

        TriggerOrderWmsShipEventRequest request = new TriggerOrderWmsShipEventRequest(orderId
                , fulfillId, OrderStatusChangeEnum.ORDER_OUT_STOCKED, event);

        JsonResult<Boolean> result = fulfillApi.triggerOrderWmsShipEvent(request);
        return result;
    }

    /**
     * 触发订单配送事件
     *
     * @param event
     * @return
     */
    @PostMapping("/triggerDeliveredWmsEvent")
    public JsonResult<Boolean> triggerOrderDeliveredWmsEvent(@RequestParam("orderId") String orderId
            , @RequestParam("fulfillId") String fulfillId, @RequestBody OrderDeliveredWmsEvent event) {
        log.info("orderId={},fulfillId={},event={}", orderId, fulfillId, JSONObject.toJSONString(event));

        TriggerOrderWmsShipEventRequest request = new TriggerOrderWmsShipEventRequest(orderId
                , fulfillId, OrderStatusChangeEnum.ORDER_DELIVERED, event);

        JsonResult<Boolean> result = fulfillApi.triggerOrderWmsShipEvent(request);
        return result;
    }

    /**
     * 触发订单签收事件
     *
     * @param event
     * @return
     */
    @PostMapping("/triggerSignedWmsEvent")
    public JsonResult<Boolean> triggerOrderSignedWmsEvent(@RequestParam("orderId") String orderId
            , @RequestParam("fulfillId") String fulfillId, @RequestBody OrderSignedWmsEvent event) {
        log.info("orderId={},fulfillId={},event={}", orderId, fulfillId, JSONObject.toJSONString(event));

        TriggerOrderWmsShipEventRequest request = new TriggerOrderWmsShipEventRequest(orderId
                , fulfillId, OrderStatusChangeEnum.ORDER_SIGNED, event);

        JsonResult<Boolean> result = fulfillApi.triggerOrderWmsShipEvent(request);
        return result;
    }


    /**
     * 触发订单已支付事件
     *
     * @param orderId
     * @return
     */
    @GetMapping("/triggerPaidEvent")
    public JsonResult<Boolean> triggerOrderPaidEvent(@RequestParam("orderId") String orderId) {
        log.info("orderId={}", orderId);
        PaidOrderSuccessMessage message = new PaidOrderSuccessMessage();
        message.setOrderId(orderId);
        String msgJson = JSON.toJSONString(message);
        defaultProducer.sendMessage(RocketMqConstant.PAID_ORDER_SUCCESS_TOPIC, msgJson, "订单已完成支付", null, orderId);
        return JsonResult.buildSuccess(true);
    }


    /**
     * 触发接收订单履约
     *
     * @param orderId
     * @return
     */
    @GetMapping("/triggerReceiveOrderFulFill")
    public JsonResult<Boolean> triggerReceiveOrderFulFill(@RequestParam("orderId") String orderId,
                                                          @RequestParam("fulfillException") String fulfillException,
                                                          @RequestParam("wmsException") String wmsException,
                                                          @RequestParam("tmsException") String tmsException) {
        log.info("orderId={}", orderId);

        OrderInfoDO order = orderInfoDAO.getByOrderId(orderId);
        ReceiveFulfillRequest request = orderFulFillService.buildReceiveFulFillRequest(order);
        request.setFulfillException(fulfillException);
        request.setWmsException(wmsException);
        request.setTmsException(tmsException);

        return fulfillApi.receiveOrderFulFill(request);
    }
}