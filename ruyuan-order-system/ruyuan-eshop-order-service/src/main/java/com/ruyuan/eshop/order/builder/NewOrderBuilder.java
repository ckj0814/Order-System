package com.ruyuan.eshop.order.builder;

import com.ruyuan.eshop.common.enums.AmountTypeEnum;
import com.ruyuan.eshop.common.enums.DeleteStatusEnum;
import com.ruyuan.eshop.common.enums.OrderOperateTypeEnum;
import com.ruyuan.eshop.common.enums.OrderStatusEnum;
import com.ruyuan.eshop.market.domain.dto.CalculateOrderAmountDTO;
import com.ruyuan.eshop.order.config.OrderProperties;
import com.ruyuan.eshop.order.converter.OrderConverter;
import com.ruyuan.eshop.order.domain.dto.OrderAmountDTO;
import com.ruyuan.eshop.order.domain.dto.OrderAmountDetailDTO;
import com.ruyuan.eshop.order.domain.entity.*;
import com.ruyuan.eshop.order.domain.request.CreateOrderRequest;
import com.ruyuan.eshop.order.enums.CommentStatusEnum;
import com.ruyuan.eshop.order.enums.OrderTypeEnum;
import com.ruyuan.eshop.order.enums.PayStatusEnum;
import com.ruyuan.eshop.order.enums.SnapshotTypeEnum;
import com.ruyuan.eshop.product.domain.dto.ProductSkuDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 创建新订单的建造器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public class NewOrderBuilder {

    private OrderConverter orderConverter;
    private CreateOrderRequest createOrderRequest;

    private List<ProductSkuDTO> productSkuList;

    private CalculateOrderAmountDTO calculateOrderAmountDTO;

    private OrderProperties orderProperties;

    private FullOrderData fullOrderData;

    public NewOrderBuilder(CreateOrderRequest createOrderRequest,
                           List<ProductSkuDTO> productSkuList,
                           CalculateOrderAmountDTO calculateOrderAmountDTO,
                           OrderProperties orderProperties,
                           OrderConverter orderConverter) {
        this.createOrderRequest = createOrderRequest;
        this.productSkuList = productSkuList;
        this.calculateOrderAmountDTO = calculateOrderAmountDTO;
        this.fullOrderData = new FullOrderData();
        this.orderProperties = orderProperties;
        this.orderConverter = orderConverter;
    }

    /**
     * 构建OrderInfoDO对象
     *
     * @return
     */
    public NewOrderBuilder buildOrder() {
        OrderInfoDO orderInfoDO = new OrderInfoDO();
        orderInfoDO.setBusinessIdentifier(createOrderRequest.getBusinessIdentifier());
        orderInfoDO.setOrderId(createOrderRequest.getOrderId());
        orderInfoDO.setParentOrderId(null);
        orderInfoDO.setBusinessOrderId(null);
        orderInfoDO.setOrderType(OrderTypeEnum.NORMAL.getCode());
        orderInfoDO.setOrderStatus(OrderStatusEnum.CREATED.getCode());
        orderInfoDO.setCancelType(null);
        orderInfoDO.setCancelTime(null);
        orderInfoDO.setSellerId(createOrderRequest.getSellerId());
        orderInfoDO.setUserId(createOrderRequest.getUserId());
        List<CreateOrderRequest.OrderAmountRequest> orderAmountRequestList = createOrderRequest.getOrderAmountRequestList();
        Map<Integer, Integer> orderAmountMap = orderAmountRequestList.stream()
                .collect(Collectors.toMap(CreateOrderRequest.OrderAmountRequest::getAmountType,
                        CreateOrderRequest.OrderAmountRequest::getAmount));
        orderInfoDO.setTotalAmount(orderAmountMap.get(AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode()));

        orderInfoDO.setPayAmount(orderAmountMap.get(AmountTypeEnum.REAL_PAY_AMOUNT.getCode()));
        List<CreateOrderRequest.PaymentRequest> paymentRequestList = createOrderRequest.getPaymentRequestList();
        if (paymentRequestList != null && !paymentRequestList.isEmpty()) {
            orderInfoDO.setPayType(paymentRequestList.get(0).getPayType());
        }
        orderInfoDO.setCouponId(createOrderRequest.getCouponId());
        orderInfoDO.setPayTime(null);
        long currentTimeMillis = System.currentTimeMillis();
        Integer expireTime = orderProperties.getExpireTime();
        orderInfoDO.setExpireTime(new Date(currentTimeMillis + expireTime));
        orderInfoDO.setUserRemark(createOrderRequest.getUserRemark());
        orderInfoDO.setDeleteStatus(DeleteStatusEnum.NO.getCode());
        orderInfoDO.setCommentStatus(CommentStatusEnum.NO.getCode());
        fullOrderData.setOrderInfoDO(orderInfoDO);
        return this;
    }

    /**
     * 构建OrderItemDO对象
     *
     * @return
     */
    public NewOrderBuilder buildOrderItems() {
        String orderId = createOrderRequest.getOrderId();
        String sellerId = createOrderRequest.getSellerId();
        List<CreateOrderRequest.OrderItemRequest> orderItemRequestList = createOrderRequest.getOrderItemRequestList();
        List<OrderItemDO> orderItemDOList = new ArrayList<>();
        int num = 0;
        for (ProductSkuDTO productSkuDTO : productSkuList) {
            OrderItemDO orderItemDO = new OrderItemDO();

            orderItemDO.setOrderId(orderId);
            orderItemDO.setOrderItemId(genOrderItemId(orderId, ++num));
            orderItemDO.setProductType(productSkuDTO.getProductType());
            orderItemDO.setProductId(productSkuDTO.getProductId());
            orderItemDO.setProductImg(productSkuDTO.getProductImg());
            orderItemDO.setProductName(productSkuDTO.getProductName());
            orderItemDO.setSkuCode(productSkuDTO.getSkuCode());
            for (CreateOrderRequest.OrderItemRequest orderItemRequest : orderItemRequestList) {
                if (orderItemRequest.getSkuCode().equals(productSkuDTO.getSkuCode())) {
                    orderItemDO.setSaleQuantity(orderItemRequest.getSaleQuantity());
                    break;
                }
            }
            orderItemDO.setSalePrice(productSkuDTO.getSalePrice());
            orderItemDO.setOriginAmount(orderItemDO.getSaleQuantity() * orderItemDO.getSalePrice());

            // 商品项目实际支付金额，默认是originAmount，但是有优惠抵扣的时候需要分摊
            int realPayAmount = 0;
            List<OrderAmountDetailDTO> orderItemAmountList = orderConverter.convertOrderAmountDetail(calculateOrderAmountDTO.getOrderAmountDetail());

            // 判断是否存在优惠抵扣费用
            orderItemAmountList = orderItemAmountList.stream().filter(item -> item.getSkuCode().equals(productSkuDTO.getSkuCode()))
                    .collect(Collectors.toList());
            if (!orderItemAmountList.isEmpty()) {
                Map<Integer, OrderAmountDetailDTO> orderAmountDetailMap = orderItemAmountList.stream()
                        .collect(Collectors.toMap(OrderAmountDetailDTO::getAmountType, Function.identity()));
                if (orderAmountDetailMap.get(AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode()) != null) {
                    realPayAmount = orderItemDO.getOriginAmount() - orderAmountDetailMap.get(
                            AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode()).getAmount();
                }
            }
            if (realPayAmount > 0) {
                orderItemDO.setPayAmount(realPayAmount);
            } else {
                orderItemDO.setPayAmount(orderItemDO.getOriginAmount());
            }
            orderItemDO.setProductUnit(productSkuDTO.getProductUnit());
            orderItemDO.setPurchasePrice(productSkuDTO.getPurchasePrice());
            orderItemDO.setSellerId(sellerId);
            orderItemDOList.add(orderItemDO);
        }
        fullOrderData.setOrderItemDOList(orderItemDOList);
        return this;
    }

    /**
     * 获取orderItemId后缀值
     *
     * @param orderId
     * @param num
     * @return
     */
    private String genOrderItemId(String orderId, Integer num) {
        if (num < 10) {
            return orderId + "_00" + num;
        }
        if (num < 100) {
            return orderId + "_0" + num;
        }
        return "_" + num;
    }

    /**
     * 构建OrderDeliveryDetailDO对象
     *
     * @return
     */
    public NewOrderBuilder buildOrderDeliveryDetail() {
        OrderDeliveryDetailDO orderDeliveryDetailDO = new OrderDeliveryDetailDO();
        orderDeliveryDetailDO.setOrderId(createOrderRequest.getOrderId());
        orderDeliveryDetailDO.setDeliveryType(createOrderRequest.getDeliveryType());
        orderDeliveryDetailDO.setProvince(createOrderRequest.getProvince());
        orderDeliveryDetailDO.setCity(createOrderRequest.getCity());
        orderDeliveryDetailDO.setArea(createOrderRequest.getArea());
        orderDeliveryDetailDO.setStreet(createOrderRequest.getStreet());
        orderDeliveryDetailDO.setDetailAddress(createOrderRequest.getDetailAddress());
        orderDeliveryDetailDO.setLon(createOrderRequest.getLon());
        orderDeliveryDetailDO.setLat(createOrderRequest.getLat());
        orderDeliveryDetailDO.setReceiverName(createOrderRequest.getReceiverName());
        orderDeliveryDetailDO.setReceiverPhone(createOrderRequest.getReceiverPhone());
        orderDeliveryDetailDO.setModifyAddressCount(0);
        fullOrderData.setOrderDeliveryDetailDO(orderDeliveryDetailDO);
        return this;
    }

    /**
     * 构建OrderPaymentDetailDO对象
     *
     * @return
     */
    public NewOrderBuilder buildOrderPaymentDetail() {
        List<CreateOrderRequest.PaymentRequest> paymentRequestList = createOrderRequest.getPaymentRequestList();
        List<OrderPaymentDetailDO> orderPaymentDetailDOList = new ArrayList<>();
        for (CreateOrderRequest.PaymentRequest paymentRequest : paymentRequestList) {
            OrderPaymentDetailDO orderPaymentDetailDO = new OrderPaymentDetailDO();
            orderPaymentDetailDO.setOrderId(createOrderRequest.getOrderId());
            orderPaymentDetailDO.setAccountType(paymentRequest.getAccountType());
            orderPaymentDetailDO.setPayType(paymentRequest.getPayType());
            orderPaymentDetailDO.setPayStatus(PayStatusEnum.UNPAID.getCode());
            List<CreateOrderRequest.OrderAmountRequest> orderAmountRequestList = createOrderRequest.getOrderAmountRequestList();
            Map<Integer, Integer> orderAmountMap = orderAmountRequestList.stream()
                    .collect(Collectors.toMap(CreateOrderRequest.OrderAmountRequest::getAmountType,
                            CreateOrderRequest.OrderAmountRequest::getAmount));
            orderPaymentDetailDO.setPayAmount(orderAmountMap.get(AmountTypeEnum.REAL_PAY_AMOUNT.getCode()));
            orderPaymentDetailDO.setPayTime(null);
            orderPaymentDetailDO.setOutTradeNo(null);
            orderPaymentDetailDO.setPayRemark(null);
            orderPaymentDetailDOList.add(orderPaymentDetailDO);
        }
        fullOrderData.setOrderPaymentDetailDOList(orderPaymentDetailDOList);
        return this;
    }

    /**
     * 构建OrderAmountDO对象
     *
     * @return
     */
    public NewOrderBuilder buildOrderAmount() {
        List<OrderAmountDTO> orderAmountDTOList = orderConverter.convertOrderAmountDTO(calculateOrderAmountDTO.getOrderAmountList());
        List<OrderAmountDO> orderAmountDOList = new ArrayList<>();
        for (OrderAmountDTO orderAmountDTO : orderAmountDTOList) {
            OrderAmountDO orderAmountDO = new OrderAmountDO();
            orderAmountDO.setOrderId(createOrderRequest.getOrderId());
            orderAmountDO.setAmountType(orderAmountDTO.getAmountType());
            orderAmountDO.setAmount(orderAmountDTO.getAmount());
            orderAmountDOList.add(orderAmountDO);
        }
        fullOrderData.setOrderAmountDOList(orderAmountDOList);
        return this;
    }

    /**
     * 构建OrderAmountDetailDO对象
     *
     * @return
     */
    public NewOrderBuilder buildOrderAmountDetail() {
        List<OrderAmountDetailDTO> orderItemAmountList = orderConverter.convertOrderAmountDetail(calculateOrderAmountDTO.getOrderAmountDetail());
        List<OrderAmountDetailDO> orderAmountDetailDOList = new ArrayList<>();
        for (OrderAmountDetailDTO orderAmountDetailDTO : orderItemAmountList) {
            OrderAmountDetailDO orderAmountDetailDO = new OrderAmountDetailDO();
            orderAmountDetailDO.setOrderId(createOrderRequest.getOrderId());
            orderAmountDetailDO.setProductType(orderAmountDetailDTO.getProductType());
            for (OrderItemDO orderItemDO : fullOrderData.getOrderItemDOList()) {
                if (orderItemDO.getSkuCode().equals(orderAmountDetailDTO.getSkuCode())) {
                    orderAmountDetailDO.setOrderItemId(orderItemDO.getOrderItemId());
                    orderAmountDetailDO.setProductId(orderItemDO.getProductId());
                }
            }

            orderAmountDetailDO.setSkuCode(orderAmountDetailDTO.getSkuCode());
            orderAmountDetailDO.setSaleQuantity(orderAmountDetailDTO.getSaleQuantity());
            orderAmountDetailDO.setSalePrice(orderAmountDetailDTO.getSalePrice());
            orderAmountDetailDO.setAmountType(orderAmountDetailDTO.getAmountType());
            orderAmountDetailDO.setAmount(orderAmountDetailDTO.getAmount());
            orderAmountDetailDOList.add(orderAmountDetailDO);
        }
        fullOrderData.setOrderAmountDetailDOList(orderAmountDetailDOList);
        return this;
    }

    /**
     * 构建OrderOperateLogDO对象
     *
     * @return
     */
    public NewOrderBuilder buildOperateLog() {
        OrderOperateLogDO orderOperateLogDO = new OrderOperateLogDO();
        orderOperateLogDO.setOrderId(createOrderRequest.getOrderId());
        orderOperateLogDO.setOperateType(OrderOperateTypeEnum.NEW_ORDER.getCode());
        orderOperateLogDO.setPreStatus(OrderStatusEnum.NULL.getCode());
        orderOperateLogDO.setCurrentStatus(OrderStatusEnum.CREATED.getCode());
        orderOperateLogDO.setRemark(null);
        fullOrderData.setOrderOperateLogDO(orderOperateLogDO);
        return this;
    }

    /**
     * 构建OrderSnapshot对象
     *
     * @return
     */
    public NewOrderBuilder buildOrderSnapshot() {
        String orderId = createOrderRequest.getOrderId();
        String couponId = createOrderRequest.getCouponId();
        List<OrderSnapshotDO> orderOperateLogDOList = new ArrayList<>();
        if (StringUtils.isNotEmpty(couponId)) {
            // 优惠券信息
            OrderSnapshotDO orderCouponSnapshotDO = new OrderSnapshotDO();
            orderCouponSnapshotDO.setOrderId(orderId);
            orderCouponSnapshotDO.setSnapshotType(SnapshotTypeEnum.ORDER_COUPON.getCode());
            orderCouponSnapshotDO.setSnapshotJson(null);
            orderOperateLogDOList.add(orderCouponSnapshotDO);
        }

        // 费用信息
        OrderSnapshotDO orderAmountSnapshotDO = new OrderSnapshotDO();
        orderAmountSnapshotDO.setOrderId(orderId);
        orderAmountSnapshotDO.setSnapshotType(SnapshotTypeEnum.ORDER_AMOUNT.getCode());
        orderAmountSnapshotDO.setSnapshotJson(null);
        orderOperateLogDOList.add(orderAmountSnapshotDO);

        // 商品条目信息
        OrderSnapshotDO orderItemSnapshotDO = new OrderSnapshotDO();
        orderItemSnapshotDO.setOrderId(orderId);
        orderItemSnapshotDO.setSnapshotType(SnapshotTypeEnum.ORDER_ITEM.getCode());
        orderItemSnapshotDO.setSnapshotJson(null);
        orderOperateLogDOList.add(orderItemSnapshotDO);

        fullOrderData.setOrderSnapshotDOList(orderOperateLogDOList);
        return this;
    }

    public FullOrderData build() {
        return this.fullOrderData;
    }

}