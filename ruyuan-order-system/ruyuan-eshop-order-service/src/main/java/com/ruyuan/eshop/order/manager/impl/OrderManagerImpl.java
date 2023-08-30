package com.ruyuan.eshop.order.manager.impl;

import com.ruyuan.eshop.address.domain.dto.AddressDTO;
import com.ruyuan.eshop.address.domain.query.AddressQuery;
import com.ruyuan.eshop.common.enums.AmountTypeEnum;
import com.ruyuan.eshop.common.enums.OrderOperateTypeEnum;
import com.ruyuan.eshop.common.enums.OrderStatusEnum;
import com.ruyuan.eshop.common.utils.JsonUtil;
import com.ruyuan.eshop.common.utils.LoggerFormat;
import com.ruyuan.eshop.inventory.domain.request.DeductProductStockRequest;
import com.ruyuan.eshop.market.domain.dto.CalculateOrderAmountDTO;
import com.ruyuan.eshop.market.domain.dto.UserCouponDTO;
import com.ruyuan.eshop.market.domain.query.UserCouponQuery;
import com.ruyuan.eshop.market.domain.request.LockUserCouponRequest;
import com.ruyuan.eshop.order.builder.FullOrderData;
import com.ruyuan.eshop.order.builder.NewOrderBuilder;
import com.ruyuan.eshop.order.config.OrderProperties;
import com.ruyuan.eshop.order.converter.OrderConverter;
import com.ruyuan.eshop.order.dao.*;
import com.ruyuan.eshop.order.domain.entity.*;
import com.ruyuan.eshop.order.domain.request.CreateOrderRequest;
import com.ruyuan.eshop.order.domain.request.PayCallbackRequest;
import com.ruyuan.eshop.order.enums.OrderNoTypeEnum;
import com.ruyuan.eshop.order.enums.PayStatusEnum;
import com.ruyuan.eshop.order.enums.SnapshotTypeEnum;
import com.ruyuan.eshop.order.manager.OrderManager;
import com.ruyuan.eshop.order.manager.OrderNoManager;
import com.ruyuan.eshop.order.remote.AddressRemote;
import com.ruyuan.eshop.order.remote.InventoryRemote;
import com.ruyuan.eshop.order.remote.MarketRemote;
import com.ruyuan.eshop.order.service.impl.NewOrderDataHolder;
import com.ruyuan.eshop.product.domain.dto.ProductSkuDTO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
@Slf4j
public class OrderManagerImpl implements OrderManager {

    @Autowired
    private OrderInfoDAO orderInfoDAO;

    @Autowired
    private OrderItemDAO orderItemDAO;

    @Autowired
    private OrderPaymentDetailDAO orderPaymentDetailDAO;

    @Autowired
    private OrderOperateLogDAO orderOperateLogDAO;

    @Autowired
    private OrderAmountDAO orderAmountDAO;

    @Autowired
    private OrderAmountDetailDAO orderAmountDetailDAO;

    @Autowired
    private OrderDeliveryDetailDAO orderDeliveryDetailDAO;

    @Autowired
    private OrderSnapshotDAO orderSnapshotDAO;

    @Autowired
    private OrderProperties orderProperties;

    /**
     * 营销服务
     */
    @Autowired
    private MarketRemote marketRemote;

    /**
     * 地址服务
     */
    @Autowired
    private AddressRemote addressRemote;

    @Autowired
    private OrderNoManager orderNoManager;

    /**
     * 库存服务
     */
    @Autowired
    private InventoryRemote inventoryRemote;

    @Autowired
    private OrderConverter orderConverter;

    /**
     * 支付回调更新订单状态
     *
     * @param payCallbackRequest
     * @param orderInfoDO
     * @param orderPaymentDetailDO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrderStatusPaid(PayCallbackRequest payCallbackRequest, OrderInfoDO orderInfoDO, OrderPaymentDetailDO orderPaymentDetailDO) {

        // 主单信息
        String orderId = payCallbackRequest.getOrderId();
        Integer preOrderStatus = orderInfoDO.getOrderStatus();
        orderInfoDO.setOrderStatus(OrderStatusEnum.PAID.getCode());
        orderInfoDAO.updateById(orderInfoDO);

        // 主单支付信息
        orderPaymentDetailDO.setPayStatus(PayStatusEnum.PAID.getCode());
        orderPaymentDetailDAO.updateById(orderPaymentDetailDO);

        // 新增订单状态变更日志
        OrderOperateLogDO orderOperateLogDO = new OrderOperateLogDO();
        orderOperateLogDO.setOrderId(orderId);
        orderOperateLogDO.setOperateType(OrderOperateTypeEnum.PAID_ORDER.getCode());
        orderOperateLogDO.setPreStatus(preOrderStatus);
        orderOperateLogDO.setCurrentStatus(orderInfoDO.getOrderStatus());
        orderOperateLogDO.setRemark("订单支付回调操作"
                + orderOperateLogDO.getPreStatus() + "-"
                + orderOperateLogDO.getCurrentStatus());
        orderOperateLogDAO.save(orderOperateLogDO);

        // 判断是否存在子订单
        List<OrderInfoDO> subOrderInfoDOList = orderInfoDAO.listByParentOrderId(orderId);
        if (subOrderInfoDOList != null && !subOrderInfoDOList.isEmpty()) {
            // 先将主订单状态设置为无效订单
            Integer newPreOrderStatus = orderInfoDO.getOrderStatus();
            orderInfoDO.setOrderStatus(OrderStatusEnum.INVALID.getCode());
            orderInfoDAO.updateById(orderInfoDO);

            // 新增订单状态变更日志
            OrderOperateLogDO newOrderOperateLogDO = new OrderOperateLogDO();
            newOrderOperateLogDO.setOrderId(orderId);
            newOrderOperateLogDO.setOperateType(OrderOperateTypeEnum.PAID_ORDER.getCode());
            newOrderOperateLogDO.setPreStatus(newPreOrderStatus);
            newOrderOperateLogDO.setCurrentStatus(OrderStatusEnum.INVALID.getCode());
            orderOperateLogDO.setRemark("订单支付回调操作，主订单状态变更"
                    + newOrderOperateLogDO.getPreStatus() + "-"
                    + newOrderOperateLogDO.getCurrentStatus());
            orderOperateLogDAO.save(newOrderOperateLogDO);

            // 再更新子订单的状态
            List<OrderInfoDO> tempSubOrderInfoDOList = new ArrayList<>();
            List<String> tempSubOrderIdList = new ArrayList<>();
            List<OrderOperateLogDO> tempSubOrderOperateLogDOList = new ArrayList<>();
            for (OrderInfoDO subOrderInfo : subOrderInfoDOList) {
                Integer subPreOrderStatus = subOrderInfo.getOrderStatus();
                subOrderInfo.setOrderStatus(OrderStatusEnum.PAID.getCode());
                tempSubOrderInfoDOList.add(subOrderInfo);

                // 子订单的支付明细
                String subOrderId = subOrderInfo.getOrderId();
                tempSubOrderIdList.add(subOrderId);

                // 订单状态变更日志
                OrderOperateLogDO subOrderOperateLogDO = new OrderOperateLogDO();
                subOrderOperateLogDO.setOrderId(subOrderId);
                subOrderOperateLogDO.setOperateType(OrderOperateTypeEnum.PAID_ORDER.getCode());
                subOrderOperateLogDO.setPreStatus(subPreOrderStatus);
                subOrderOperateLogDO.setCurrentStatus(OrderStatusEnum.PAID.getCode());
                orderOperateLogDO.setRemark("订单支付回调操作，子订单状态变更"
                        + subOrderOperateLogDO.getPreStatus() + "-"
                        + subOrderOperateLogDO.getCurrentStatus());
                tempSubOrderOperateLogDOList.add(subOrderOperateLogDO);
            }

            // 更新子订单
            if (!tempSubOrderInfoDOList.isEmpty()) {
                orderInfoDAO.updateBatchById(tempSubOrderInfoDOList);
            }

            // 更新子订单的支付明细
            if (!tempSubOrderIdList.isEmpty()) {
                OrderPaymentDetailDO subOrderPaymentDetailDO = new OrderPaymentDetailDO();
                subOrderPaymentDetailDO.setPayStatus(PayStatusEnum.PAID.getCode());
                orderPaymentDetailDAO.updateBatchByOrderIds(subOrderPaymentDetailDO, tempSubOrderIdList);
            }

            // 新增订单状态变更日志
            if (!tempSubOrderOperateLogDOList.isEmpty()) {
                orderOperateLogDAO.saveBatch(tempSubOrderOperateLogDOList);
            }
        }

    }

    /**
     * 生成订单
     *
     * @param createOrderRequest
     * @param productSkuList
     * @param calculateOrderAmountDTO
     */
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void createOrder(CreateOrderRequest createOrderRequest, List<ProductSkuDTO> productSkuList, CalculateOrderAmountDTO calculateOrderAmountDTO) {
        // 锁定优惠券
        lockUserCoupon(createOrderRequest);

        // 扣减库存
        deductProductStock(createOrderRequest);

        // 生成订单到数据库
        addNewOrder(createOrderRequest, productSkuList, calculateOrderAmountDTO);
    }


    /**
     * 锁定用户优惠券
     */
    private void lockUserCoupon(CreateOrderRequest createOrderRequest) {
        String couponId = createOrderRequest.getCouponId();
        if (StringUtils.isEmpty(couponId)) {
            return;
        }
        LockUserCouponRequest lockUserCouponRequest = orderConverter.convertLockUserCouponRequest(createOrderRequest);
        // 调用营销服务锁定用户优惠券
        marketRemote.lockUserCoupon(lockUserCouponRequest);
    }

    /**
     * 锁定商品库存
     *
     * @param createOrderRequest 订单信息
     */
    private void deductProductStock(CreateOrderRequest createOrderRequest) {
        String orderId = createOrderRequest.getOrderId();
        List<DeductProductStockRequest.OrderItemRequest> orderItemRequestList =
                orderConverter.convertOrderItemRequest(createOrderRequest.getOrderItemRequestList());
        DeductProductStockRequest lockProductStockRequest = new DeductProductStockRequest();
        lockProductStockRequest.setOrderId(orderId);
        lockProductStockRequest.setOrderItemRequestList(orderItemRequestList);
        inventoryRemote.deductProductStock(lockProductStockRequest);
    }

    /**
     * 新增订单数据到数据库
     */
    private void addNewOrder(CreateOrderRequest createOrderRequest, List<ProductSkuDTO> productSkuList, CalculateOrderAmountDTO calculateOrderAmountDTO) {
        String orderId = createOrderRequest.getOrderId();
        // 封装新订单数据
        NewOrderDataHolder newOrderDataHolder = new NewOrderDataHolder();

        // 生成主订单
        FullOrderData fullMasterOrderData = addNewMasterOrder(createOrderRequest, productSkuList, calculateOrderAmountDTO);

        // 封装主订单数据到NewOrderData对象中
        newOrderDataHolder.appendOrderData(fullMasterOrderData);


        // 如果存在多种商品类型，需要按商品类型进行拆单
        Map<Integer, List<ProductSkuDTO>> productTypeMap = productSkuList.stream().collect(Collectors.groupingBy(ProductSkuDTO::getProductType));
        if (productTypeMap.keySet().size() > 1) {
            for (Integer productType : productTypeMap.keySet()) {
                // 生成子订单
                FullOrderData fullSubOrderData = addNewSubOrder(fullMasterOrderData, productType);

                // 封装子订单数据到NewOrderData对象中
                newOrderDataHolder.appendOrderData(fullSubOrderData);
            }
        }

        // 保存订单到数据库
        // 订单信息
        List<OrderInfoDO> orderInfoDOList = newOrderDataHolder.getOrderInfoDOList();
        if (!orderInfoDOList.isEmpty()) {
            log.info(LoggerFormat.build()
                    .remark("保存订单信息")
                    .data("orderId", orderId)
                    .finish());
            orderInfoDAO.saveBatch(orderInfoDOList);
        }

        // 订单条目
        List<OrderItemDO> orderItemDOList = newOrderDataHolder.getOrderItemDOList();
        if (!orderItemDOList.isEmpty()) {
            log.info(LoggerFormat.build()
                    .remark("保存订单条目")
                    .data("orderId", orderId)
                    .finish());
            orderItemDAO.saveBatch(orderItemDOList);
        }

        // 订单配送信息
        List<OrderDeliveryDetailDO> orderDeliveryDetailDOList = newOrderDataHolder.getOrderDeliveryDetailDOList();
        if (!orderDeliveryDetailDOList.isEmpty()) {
            log.info(LoggerFormat.build()
                    .remark("保存订单配送信息")
                    .data("orderId", orderId)
                    .finish());
            orderDeliveryDetailDAO.saveBatch(orderDeliveryDetailDOList);
        }

        // 订单支付信息
        List<OrderPaymentDetailDO> orderPaymentDetailDOList = newOrderDataHolder.getOrderPaymentDetailDOList();
        if (!orderPaymentDetailDOList.isEmpty()) {
            log.info(LoggerFormat.build()
                    .remark("保存订单支付信息")
                    .data("orderId", orderId)
                    .finish());
            orderPaymentDetailDAO.saveBatch(orderPaymentDetailDOList);
        }

        // 订单费用信息
        List<OrderAmountDO> orderAmountDOList = newOrderDataHolder.getOrderAmountDOList();
        if (!orderAmountDOList.isEmpty()) {
            log.info(LoggerFormat.build()
                    .remark("保存订单费用信息")
                    .data("orderId", orderId)
                    .finish());
            orderAmountDAO.saveBatch(orderAmountDOList);
        }

        // 订单费用明细
        List<OrderAmountDetailDO> orderAmountDetailDOList = newOrderDataHolder.getOrderAmountDetailDOList();
        if (!orderAmountDetailDOList.isEmpty()) {
            log.info(LoggerFormat.build()
                    .remark("保存订单费用明细")
                    .data("orderId", orderId)
                    .finish());
            orderAmountDetailDAO.saveBatch(orderAmountDetailDOList);
        }

        // 订单状态变更日志信息
        List<OrderOperateLogDO> orderOperateLogDOList = newOrderDataHolder.getOrderOperateLogDOList();
        if (!orderOperateLogDOList.isEmpty()) {
            log.info(LoggerFormat.build()
                    .remark("保存订单状态变更日志信息")
                    .data("orderId", orderId)
                    .finish());
            orderOperateLogDAO.saveBatch(orderOperateLogDOList);
        }

        // 订单快照数据
        List<OrderSnapshotDO> orderSnapshotDOList = newOrderDataHolder.getOrderSnapshotDOList();
        if (!orderSnapshotDOList.isEmpty()) {
            log.info(LoggerFormat.build()
                    .remark("保存订单快照数据")
                    .data("orderId", orderId)
                    .finish());
            orderSnapshotDAO.saveBatch(orderSnapshotDOList);
        }
    }

    /**
     * 新增主订单信息订单
     */
    private FullOrderData addNewMasterOrder(CreateOrderRequest createOrderRequest, List<ProductSkuDTO> productSkuList,
                                            CalculateOrderAmountDTO calculateOrderAmountDTO) {
        NewOrderBuilder newOrderBuilder = new NewOrderBuilder(createOrderRequest, productSkuList,
                calculateOrderAmountDTO, orderProperties, orderConverter);
        FullOrderData fullOrderData = newOrderBuilder.buildOrder()
                .buildOrderItems()
                .buildOrderDeliveryDetail()
                .buildOrderPaymentDetail()
                .buildOrderAmount()
                .buildOrderAmountDetail()
                .buildOperateLog()
                .buildOrderSnapshot()
                .build();

        // 订单信息
        OrderInfoDO orderInfoDO = fullOrderData.getOrderInfoDO();

        // 订单条目信息
        List<OrderItemDO> orderItemDOList = fullOrderData.getOrderItemDOList();

        // 订单费用信息
        List<OrderAmountDO> orderAmountDOList = fullOrderData.getOrderAmountDOList();

        // 补全地址信息
        OrderDeliveryDetailDO orderDeliveryDetailDO = fullOrderData.getOrderDeliveryDetailDO();
        String detailAddress = getDetailAddress(orderDeliveryDetailDO);
        orderDeliveryDetailDO.setDetailAddress(detailAddress);

        // 补全订单状态变更日志
        OrderOperateLogDO orderOperateLogDO = fullOrderData.getOrderOperateLogDO();
        String remark = "创建订单操作0-10";
        orderOperateLogDO.setRemark(remark);

        // 补全订单商品快照信息
        List<OrderSnapshotDO> orderSnapshotDOList = fullOrderData.getOrderSnapshotDOList();
        for (OrderSnapshotDO orderSnapshotDO : orderSnapshotDOList) {
            // 优惠券信息
            if (orderSnapshotDO.getSnapshotType().equals(SnapshotTypeEnum.ORDER_COUPON.getCode())) {
                String couponId = orderInfoDO.getCouponId();
                String userId = orderInfoDO.getUserId();
                UserCouponQuery userCouponQuery = new UserCouponQuery();
                userCouponQuery.setCouponId(couponId);
                userCouponQuery.setUserId(userId);
                UserCouponDTO userCouponDTO = marketRemote.getUserCoupon(userCouponQuery);
                if (userCouponDTO != null) {
                    orderSnapshotDO.setSnapshotJson(JsonUtil.object2Json(userCouponDTO));
                } else {
                    orderSnapshotDO.setSnapshotJson(JsonUtil.object2Json(couponId));
                }
            }
            // 订单费用信息
            else if (orderSnapshotDO.getSnapshotType().equals(SnapshotTypeEnum.ORDER_AMOUNT.getCode())) {
                orderSnapshotDO.setSnapshotJson(JsonUtil.object2Json(orderAmountDOList));
            }
            // 订单条目信息
            else if (orderSnapshotDO.getSnapshotType().equals(SnapshotTypeEnum.ORDER_ITEM.getCode())) {
                orderSnapshotDO.setSnapshotJson(JsonUtil.object2Json(orderItemDOList));
            }
        }

        return fullOrderData;
    }

    /**
     * 获取用户收货详细地址
     */
    private String getDetailAddress(OrderDeliveryDetailDO orderDeliveryDetailDO) {
        String provinceCode = orderDeliveryDetailDO.getProvince();
        String cityCode = orderDeliveryDetailDO.getCity();
        String areaCode = orderDeliveryDetailDO.getArea();
        String streetCode = orderDeliveryDetailDO.getStreet();
        AddressQuery query = new AddressQuery();
        query.setProvinceCode(provinceCode);
        query.setCityCode(cityCode);
        query.setAreaCode(areaCode);
        query.setStreetCode(streetCode);
        AddressDTO addressDTO = addressRemote.queryAddress(query);
        if (addressDTO == null) {
            return orderDeliveryDetailDO.getDetailAddress();
        }

        StringBuilder detailAddress = new StringBuilder();
        if (StringUtils.isNotEmpty(addressDTO.getProvince())) {
            detailAddress.append(addressDTO.getProvince());
        }
        if (StringUtils.isNotEmpty(addressDTO.getCity())) {
            detailAddress.append(addressDTO.getCity());
        }
        if (StringUtils.isNotEmpty(addressDTO.getArea())) {
            detailAddress.append(addressDTO.getArea());
        }
        if (StringUtils.isNotEmpty(addressDTO.getStreet())) {
            detailAddress.append(addressDTO.getStreet());
        }
        if (StringUtils.isNotEmpty(orderDeliveryDetailDO.getDetailAddress())) {
            detailAddress.append(orderDeliveryDetailDO.getDetailAddress());
        }
        return detailAddress.toString();
    }

    /**
     * 生成子单
     *
     * @param fullOrderData 主单数据
     * @param productType   商品类型
     */
    private FullOrderData addNewSubOrder(FullOrderData fullOrderData, Integer productType) {

        // 主单信息
        OrderInfoDO orderInfoDO = fullOrderData.getOrderInfoDO();
        // 主订单条目
        List<OrderItemDO> orderItemDOList = fullOrderData.getOrderItemDOList();
        // 主订单配送信息
        OrderDeliveryDetailDO orderDeliveryDetailDO = fullOrderData.getOrderDeliveryDetailDO();
        // 主订单支付信息
        List<OrderPaymentDetailDO> orderPaymentDetailDOList = fullOrderData.getOrderPaymentDetailDOList();
        // 主订单费用信息
        List<OrderAmountDO> orderAmountDOList = fullOrderData.getOrderAmountDOList();
        // 主订单费用明细
        List<OrderAmountDetailDO> orderAmountDetailDOList = fullOrderData.getOrderAmountDetailDOList();
        // 主订单状态变更日志信息
        OrderOperateLogDO orderOperateLogDO = fullOrderData.getOrderOperateLogDO();
        // 主订单快照数据
        List<OrderSnapshotDO> orderSnapshotDOList = fullOrderData.getOrderSnapshotDOList();


        // 父订单号
        String orderId = orderInfoDO.getOrderId();
        // 用户ID
        String userId = orderInfoDO.getUserId();

        // 生成新的子订单的订单号
        String subOrderId = orderNoManager.genOrderId(OrderNoTypeEnum.SALE_ORDER.getCode(), userId);

        // 子订单全量的数据
        FullOrderData subFullOrderData = new FullOrderData();

        // 过滤出当前商品类型的订单条目信息
        List<OrderItemDO> subOrderItemDOList = orderItemDOList.stream()
                .filter(orderItemDO -> productType.equals(orderItemDO.getProductType()))
                .collect(Collectors.toList());

        // 统计子单总金额
        Integer subTotalAmount = 0;
        Integer subRealPayAmount = 0;
        for (OrderItemDO subOrderItemDO : subOrderItemDOList) {
            subTotalAmount += subOrderItemDO.getOriginAmount();
            subRealPayAmount += subOrderItemDO.getPayAmount();
        }

        // 订单主信息
        OrderInfoDO newSubOrderInfo = orderConverter.copyOrderInfoDTO(orderInfoDO);
        newSubOrderInfo.setId(null);
        newSubOrderInfo.setOrderId(subOrderId);
        newSubOrderInfo.setParentOrderId(orderId);
        newSubOrderInfo.setOrderStatus(OrderStatusEnum.INVALID.getCode());
        newSubOrderInfo.setTotalAmount(subTotalAmount);
        newSubOrderInfo.setPayAmount(subRealPayAmount);
        subFullOrderData.setOrderInfoDO(newSubOrderInfo);

        // 订单条目
        List<OrderItemDO> newSubOrderItemList = new ArrayList<>();
        for (OrderItemDO orderItemDO : subOrderItemDOList) {
            OrderItemDO newSubOrderItem = orderConverter.copyOrderItemDO(orderItemDO);
            newSubOrderItem.setId(null);
            newSubOrderItem.setOrderId(subOrderId);
            String subOrderItemId = getSubOrderItemId(orderItemDO.getOrderItemId(), subOrderId);
            newSubOrderItem.setOrderItemId(subOrderItemId);
            newSubOrderItemList.add(newSubOrderItem);
        }
        subFullOrderData.setOrderItemDOList(newSubOrderItemList);

        // 订单配送地址信息
        OrderDeliveryDetailDO newSubOrderDeliveryDetail = orderConverter.copyOrderDeliverDetailDO(orderDeliveryDetailDO);
        newSubOrderDeliveryDetail.setId(null);
        newSubOrderDeliveryDetail.setOrderId(subOrderId);
        subFullOrderData.setOrderDeliveryDetailDO(newSubOrderDeliveryDetail);


        Map<String, OrderItemDO> subOrderItemMap = subOrderItemDOList.stream()
                .collect(Collectors.toMap(OrderItemDO::getOrderItemId, Function.identity()));

        // 统计子订单费用信息
        Integer subTotalOriginPayAmount = 0;
        Integer subTotalCouponDiscountAmount = 0;
        Integer subTotalRealPayAmount = 0;

        // 订单费用明细
        List<OrderAmountDetailDO> subOrderAmountDetailList = new ArrayList<>();
        for (OrderAmountDetailDO orderAmountDetailDO : orderAmountDetailDOList) {
            String orderItemId = orderAmountDetailDO.getOrderItemId();
            if (!subOrderItemMap.containsKey(orderItemId)) {
                continue;
            }
            OrderAmountDetailDO subOrderAmountDetail = orderConverter.copyOrderAmountDetail(orderAmountDetailDO);
            subOrderAmountDetail.setId(null);
            subOrderAmountDetail.setOrderId(subOrderId);
            String subOrderItemId = getSubOrderItemId(orderItemId, subOrderId);
            subOrderAmountDetail.setOrderItemId(subOrderItemId);
            subOrderAmountDetailList.add(subOrderAmountDetail);

            Integer amountType = orderAmountDetailDO.getAmountType();
            Integer amount = orderAmountDetailDO.getAmount();
            if (AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode().equals(amountType)) {
                subTotalOriginPayAmount += amount;
            }
            if (AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode().equals(amountType)) {
                subTotalCouponDiscountAmount += amount;
            }
            if (AmountTypeEnum.REAL_PAY_AMOUNT.getCode().equals(amountType)) {
                subTotalRealPayAmount += amount;
            }
        }
        subFullOrderData.setOrderAmountDetailDOList(subOrderAmountDetailList);

        // 订单费用信息
        List<OrderAmountDO> subOrderAmountList = new ArrayList<>();
        for (OrderAmountDO orderAmountDO : orderAmountDOList) {
            Integer amountType = orderAmountDO.getAmountType();
            OrderAmountDO subOrderAmount = orderConverter.copyOrderAmountDO(orderAmountDO);
            subOrderAmount.setId(null);
            subOrderAmount.setOrderId(subOrderId);
            if (AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode().equals(amountType)) {
                subOrderAmount.setAmount(subTotalOriginPayAmount);
                subOrderAmountList.add(subOrderAmount);
            }
            if (AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode().equals(amountType)) {
                subOrderAmount.setAmount(subTotalCouponDiscountAmount);
                subOrderAmountList.add(subOrderAmount);
            }
            if (AmountTypeEnum.REAL_PAY_AMOUNT.getCode().equals(amountType)) {
                subOrderAmount.setAmount(subTotalRealPayAmount);
                subOrderAmountList.add(subOrderAmount);
            }
        }
        subFullOrderData.setOrderAmountDOList(subOrderAmountList);

        // 订单支付信息
        List<OrderPaymentDetailDO> subOrderPaymentDetailDOList = new ArrayList<>();
        for (OrderPaymentDetailDO orderPaymentDetailDO : orderPaymentDetailDOList) {
            OrderPaymentDetailDO subOrderPaymentDetail = orderConverter.copyOrderPaymentDetailDO(orderPaymentDetailDO);
            subOrderPaymentDetail.setId(null);
            subOrderPaymentDetail.setOrderId(subOrderId);
            subOrderPaymentDetail.setPayAmount(subTotalRealPayAmount);
            subOrderPaymentDetailDOList.add(subOrderPaymentDetail);
        }
        subFullOrderData.setOrderPaymentDetailDOList(subOrderPaymentDetailDOList);

        // 订单状态变更日志信息
        OrderOperateLogDO subOrderOperateLogDO = orderConverter.copyOrderOperationLogDO(orderOperateLogDO);
        subOrderOperateLogDO.setId(null);
        subOrderOperateLogDO.setOrderId(subOrderId);
        subFullOrderData.setOrderOperateLogDO(subOrderOperateLogDO);

        // 订单商品快照信息
        List<OrderSnapshotDO> subOrderSnapshotDOList = new ArrayList<>();
        for (OrderSnapshotDO orderSnapshotDO : orderSnapshotDOList) {
            OrderSnapshotDO subOrderSnapshotDO = orderConverter.copyOrderSnapshot(orderSnapshotDO);
            subOrderSnapshotDO.setId(null);
            subOrderSnapshotDO.setOrderId(subOrderId);
            if (SnapshotTypeEnum.ORDER_AMOUNT.getCode().equals(orderSnapshotDO.getSnapshotType())) {
                subOrderSnapshotDO.setSnapshotJson(JsonUtil.object2Json(subOrderAmountList));
            } else if (SnapshotTypeEnum.ORDER_ITEM.getCode().equals(orderSnapshotDO.getSnapshotType())) {
                subOrderSnapshotDO.setSnapshotJson(JsonUtil.object2Json(subOrderItemDOList));
            }
            subOrderSnapshotDOList.add(subOrderSnapshotDO);
        }
        subFullOrderData.setOrderSnapshotDOList(subOrderSnapshotDOList);
        return subFullOrderData;
    }

    /**
     * 获取子订单的orderItemId值
     */
    private String getSubOrderItemId(String orderItemId, String subOrderId) {
        String postfix = orderItemId.substring(orderItemId.indexOf("_"));
        return subOrderId + postfix;
    }


}