package com.ruyuan.eshop.order.service.impl;

import com.ruyuan.eshop.common.bean.SpringApplicationContext;
import com.ruyuan.eshop.common.enums.AmountTypeEnum;
import com.ruyuan.eshop.common.enums.OrderStatusChangeEnum;
import com.ruyuan.eshop.common.enums.OrderStatusEnum;
import com.ruyuan.eshop.fulfill.domain.request.ReceiveFulfillRequest;
import com.ruyuan.eshop.fulfill.domain.request.ReceiveOrderItemRequest;
import com.ruyuan.eshop.order.dao.*;
import com.ruyuan.eshop.order.domain.dto.WmsShipDTO;
import com.ruyuan.eshop.order.domain.entity.OrderAmountDO;
import com.ruyuan.eshop.order.domain.entity.OrderDeliveryDetailDO;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import com.ruyuan.eshop.order.domain.entity.OrderItemDO;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.service.OrderFulFillService;
import com.ruyuan.eshop.order.wms.OrderDeliveredProcessor;
import com.ruyuan.eshop.order.wms.OrderOutStockedProcessor;
import com.ruyuan.eshop.order.wms.OrderSignedProcessor;
import com.ruyuan.eshop.order.wms.OrderWmsShipResultProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class OrderFulFillServiceImpl implements OrderFulFillService {

    @Autowired
    private OrderInfoDAO orderInfoDAO;

    @Autowired
    private OrderItemDAO orderItemDAO;

    @Autowired
    private OrderAmountDAO orderAmountDAO;

    @Autowired
    private OrderDeliveryDetailDAO orderDeliveryDetailDAO;

    @Autowired
    private OrderOperateLogFactory orderOperateLogFactory;

    @Autowired
    private OrderOperateLogDAO orderOperateLogDAO;

    @Autowired
    private SpringApplicationContext springApplicationContext;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void triggerOrderFulFill(String orderId) throws OrderBizException {
        //1、查询订单
        OrderInfoDO order = orderInfoDAO.getByOrderId(orderId);
        if (Objects.isNull(order)) {
            return;
        }

        //2、校验订单是否已支付
        OrderStatusEnum orderStatus = OrderStatusEnum.getByCode(order.getOrderStatus());
        if (!OrderStatusEnum.PAID.equals(orderStatus)) {
            log.info("order has not been paid，cannot fulfill, orderId={}", order.getOrderId());
            return;
        }

        //3、更新订单状态为：“已履约”
        orderInfoDAO.updateOrderStatus(orderId, OrderStatusEnum.PAID.getCode(), OrderStatusEnum.FULFILL.getCode());

        //4、并插入一条订单变更记录
        orderOperateLogDAO.save(orderOperateLogFactory.get(order, OrderStatusChangeEnum.ORDER_FULFILLED));

    }

    @Override
    public void informOrderWmsShipResult(WmsShipDTO wmsShipDTO) throws OrderBizException {
        //1、获取对应的订单物流结果处理器
        OrderWmsShipResultProcessor processor = getProcessor(wmsShipDTO.getStatusChange());

        //2、执行
        if (null != processor) {
            processor.execute(wmsShipDTO);
        }
    }

    /**
     * 构建接受订单履约请求
     *
     * @param orderInfo
     * @return
     */
    @Override
    public ReceiveFulfillRequest buildReceiveFulFillRequest(OrderInfoDO orderInfo) {

        OrderDeliveryDetailDO orderDeliveryDetail = orderDeliveryDetailDAO.getByOrderId(orderInfo.getOrderId());
        List<OrderItemDO> orderItems = orderItemDAO.listByOrderId(orderInfo.getOrderId());

        OrderAmountDO deliveryAmount = orderAmountDAO.getOne(orderInfo.getOrderId()
                , AmountTypeEnum.SHIPPING_AMOUNT.getCode());

        //构造请求
        ReceiveFulfillRequest request = ReceiveFulfillRequest.builder()
                .businessIdentifier(orderInfo.getBusinessIdentifier())
                .orderId(orderInfo.getOrderId())
                .sellerId(orderInfo.getSellerId())
                .userId(orderInfo.getUserId())
                .deliveryType(orderDeliveryDetail.getDeliveryType())
                .receiverName(orderDeliveryDetail.getReceiverName())
                .receiverPhone(orderDeliveryDetail.getReceiverPhone())
                .receiverProvince(orderDeliveryDetail.getProvince())
                .receiverCity(orderDeliveryDetail.getCity())
                .receiverArea(orderDeliveryDetail.getArea())
                .receiverStreet(orderDeliveryDetail.getStreet())
                .receiverDetailAddress(orderDeliveryDetail.getDetailAddress())
                .receiverLat(orderDeliveryDetail.getLat())
                .receiverLon(orderDeliveryDetail.getLon())
                .payType(orderInfo.getPayType())
                .payAmount(orderInfo.getPayAmount())
                .totalAmount(orderInfo.getTotalAmount())
                .receiveOrderItems(buildReceiveOrderItemRequest(orderInfo, orderItems))
                .build();

        //运费
        if (null != deliveryAmount) {
            request.setDeliveryAmount(deliveryAmount.getAmount());
        }
        return request;
    }


    private List<ReceiveOrderItemRequest> buildReceiveOrderItemRequest(OrderInfoDO orderInfo, List<OrderItemDO> items) {

        List<ReceiveOrderItemRequest> itemRequests = new ArrayList<>();

        items.forEach(item -> {
            ReceiveOrderItemRequest request = ReceiveOrderItemRequest.builder()
                    .skuCode(item.getSkuCode())
                    .productName(item.getProductName())
                    .salePrice(item.getSalePrice())
                    .saleQuantity(item.getSaleQuantity())
                    .productUnit(item.getProductUnit())
                    .payAmount(item.getPayAmount())
                    .originAmount(item.getOriginAmount())
                    .build();
            itemRequests.add(request);
        });

        return itemRequests;
    }

    /**
     * 获取对应的订单物流结果处理器
     *
     * @param orderStatusChange
     * @return
     */
    private OrderWmsShipResultProcessor getProcessor(OrderStatusChangeEnum orderStatusChange) {

        if (OrderStatusChangeEnum.ORDER_OUT_STOCKED.equals(orderStatusChange)) {
            return springApplicationContext.getBean(OrderOutStockedProcessor.class);
        } else if (OrderStatusChangeEnum.ORDER_DELIVERED.equals(orderStatusChange)) {
            return springApplicationContext.getBean(OrderDeliveredProcessor.class);
        } else if (OrderStatusChangeEnum.ORDER_SIGNED.equals(orderStatusChange)) {
            return springApplicationContext.getBean(OrderSignedProcessor.class);
        }

        return null;
    }
}
