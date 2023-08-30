package com.ruyuan.eshop.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruyuan.eshop.common.enums.OrderStatusEnum;
import com.ruyuan.eshop.common.page.PagingInfo;
import com.ruyuan.eshop.common.utils.LoggerFormat;
import com.ruyuan.eshop.common.utils.ParamCheckUtil;
import com.ruyuan.eshop.order.converter.OrderConverter;
import com.ruyuan.eshop.order.dao.*;
import com.ruyuan.eshop.order.domain.dto.OrderDetailDTO;
import com.ruyuan.eshop.order.domain.dto.OrderLackItemDTO;
import com.ruyuan.eshop.order.domain.dto.OrderListDTO;
import com.ruyuan.eshop.order.domain.dto.OrderListQueryDTO;
import com.ruyuan.eshop.order.domain.entity.*;
import com.ruyuan.eshop.order.domain.query.AcceptOrderQuery;
import com.ruyuan.eshop.order.domain.query.OrderQuery;
import com.ruyuan.eshop.order.enums.BusinessIdentifierEnum;
import com.ruyuan.eshop.order.enums.OrderTypeEnum;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
import com.ruyuan.eshop.order.service.AfterSaleQueryService;
import com.ruyuan.eshop.order.service.OrderLackService;
import com.ruyuan.eshop.order.service.OrderQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderQueryServiceImpl implements OrderQueryService {

    @Autowired
    private OrderInfoDAO orderInfoDAO;

    @Autowired
    private OrderItemDAO orderItemDAO;

    @Autowired
    private OrderAmountDetailDAO orderAmountDetailDAO;

    @Autowired
    private OrderDeliveryDetailDAO orderDeliveryDetailDAO;

    @Autowired
    private OrderPaymentDetailDAO orderPaymentDetailDAO;

    @Autowired
    private OrderSnapshotDAO orderSnapshotDAO;

    @Autowired
    private OrderAmountDAO orderAmountDAO;

    @Autowired
    private OrderOperateLogDAO orderOperateLogDAO;

    @Autowired
    private AfterSaleQueryService afterSaleQueryService;

    @Autowired
    private OrderLackService orderLackService;

    @Autowired
    private OrderConverter orderConverter;


    @Override
    public void checkQueryParam(AcceptOrderQuery acceptOrderQuery) {

        ParamCheckUtil.checkObjectNonNull(acceptOrderQuery.getBusinessIdentifier(), OrderErrorCodeEnum.BUSINESS_IDENTIFIER_IS_NULL);
        checkIntAllowableValues(acceptOrderQuery.getBusinessIdentifier(), BusinessIdentifierEnum.allowableValues(), "businessIdentifier");
        checkIntSetAllowableValues(acceptOrderQuery.getOrderTypes(), OrderTypeEnum.allowableValues(), "orderTypes");
        checkIntSetAllowableValues(acceptOrderQuery.getOrderStatus(), OrderStatusEnum.allowableValues(), "orderStatus");


        Integer maxSize = OrderQuery.MAX_PAGE_SIZE;
        checkSetMaxSize(acceptOrderQuery.getOrderIds(), maxSize, "orderIds");
        checkSetMaxSize(acceptOrderQuery.getSellerIds(), maxSize, "sellerIds");
        checkSetMaxSize(acceptOrderQuery.getParentOrderIds(), maxSize, "parentOrderIds");
        checkSetMaxSize(acceptOrderQuery.getReceiverNames(), maxSize, "receiverNames");
        checkSetMaxSize(acceptOrderQuery.getReceiverPhones(), maxSize, "receiverPhones");
        checkSetMaxSize(acceptOrderQuery.getTradeNos(), maxSize, "tradeNos");
        checkSetMaxSize(acceptOrderQuery.getUserIds(), maxSize, "userIds");
        checkSetMaxSize(acceptOrderQuery.getSkuCodes(), maxSize, "skuCodes");
        checkSetMaxSize(acceptOrderQuery.getProductNames(), maxSize, "productNames");
    }

    @Override
    public PagingInfo<OrderListDTO> executeListQuery(OrderQuery query) {

        //第一阶段采用很low的连表查询，连接5张表，即使加索引，只要数据量稍微大一点查询性能就很低了
        //第二阶段会接入es，优化这块的查询性能

        //1、组装业务查询规则
        if (CollectionUtils.isEmpty(query.getOrderStatus())) {
            //不展示无效订单
            query.setOrderStatus(OrderStatusEnum.validStatus());
        }
        OrderListQueryDTO queryDTO = orderConverter.orderListQuery2DTO(query);
        log.info(LoggerFormat.build()
                .remark("executeListQuery->request")
                .data("request", query)
                .finish());
        //2、查询
        Page<OrderListDTO> page = orderInfoDAO.listByPage(queryDTO);

        //3、转化
        return PagingInfo.toResponse(page.getRecords()
                , page.getTotal(), (int) page.getCurrent(), (int) page.getSize());
    }

    private void checkIntAllowableValues(Integer i, Set<Integer> allowableValues, String paramName) {
        OrderErrorCodeEnum orderErrorCodeEnum = OrderErrorCodeEnum.ENUM_PARAM_MUST_BE_IN_ALLOWABLE_VALUE;
        ParamCheckUtil.checkIntAllowableValues(i
                , allowableValues,
                orderErrorCodeEnum, paramName, allowableValues);
    }

    private void checkIntSetAllowableValues(Set<Integer> set, Set<Integer> allowableValues, String paramName) {
        OrderErrorCodeEnum orderErrorCodeEnum = OrderErrorCodeEnum.ENUM_PARAM_MUST_BE_IN_ALLOWABLE_VALUE;
        ParamCheckUtil.checkIntSetAllowableValues(set
                , allowableValues,
                orderErrorCodeEnum, paramName, allowableValues);
    }

    private void checkSetMaxSize(Set setParam, Integer maxSize, String paramName) {
        OrderErrorCodeEnum orderErrorCodeEnum = OrderErrorCodeEnum.COLLECTION_PARAM_CANNOT_BEYOND_MAX_SIZE;
        ParamCheckUtil.checkSetMaxSize(setParam, maxSize,
                orderErrorCodeEnum, paramName
                , maxSize);

    }

    @Override
    public OrderDetailDTO orderDetail(String orderId) {
        log.info(LoggerFormat.build()
                .remark("orderDetail->request")
                .data("orderId", orderId)
                .finish());

        //1、查询订单
        OrderInfoDO orderInfo = orderInfoDAO.getByOrderId(orderId);
        if (null == orderInfo) {
            return null;
        }

        //2、查询订单条目
        List<OrderItemDO> orderItems = orderItemDAO.listByOrderId(orderId);

        //3、查询订单费用明细
        List<OrderAmountDetailDO> orderAmountDetails = orderAmountDetailDAO.listByOrderId(orderId);

        //4、查询订单配送信息
        OrderDeliveryDetailDO orderAmountDetail = orderDeliveryDetailDAO.getByOrderId(orderId);

        //5、查询订单支付明细
        List<OrderPaymentDetailDO> orderPaymentDetails = orderPaymentDetailDAO.listByOrderId(orderId);

        //6、查询订单费用类型
        List<OrderAmountDO> orderAmounts = orderAmountDAO.listByOrderId(orderId);

        //7、查询订单操作日志
        List<OrderOperateLogDO> orderOperateLogs = orderOperateLogDAO.listByOrderId(orderId);

        //8、查询订单快照
        List<OrderSnapshotDO> orderSnapshots = orderSnapshotDAO.listByOrderId(orderId);

        //9、查询缺品退款信息
        List<OrderLackItemDTO> lackItems = null;
        if (orderLackService.isOrderLacked(orderInfo)) {
            lackItems = afterSaleQueryService.getOrderLackItemInfo(orderId);
        }

        //10、构造返参
        return OrderDetailDTO.builder()
                .orderInfo(orderConverter.orderInfoDO2DTO(orderInfo))
                .orderItems(orderConverter.orderItemDO2DTO(orderItems))
                .orderAmountDetails(orderConverter.orderAmountDetailDO2DTO(orderAmountDetails))
                .orderDeliveryDetail(orderConverter.orderDeliveryDetailDO2DTO(orderAmountDetail))
                .orderPaymentDetails(orderConverter.orderPaymentDetailDO2DTO(orderPaymentDetails))
                .orderAmounts(orderAmounts.stream().collect(
                        Collectors.toMap(OrderAmountDO::getAmountType, OrderAmountDO::getAmount, (v1, v2) -> v1)))
                .orderOperateLogs(orderConverter.orderOperateLogsDO2DTO(orderOperateLogs))
                .orderSnapshots(orderConverter.orderSnapshotsDO2DTO(orderSnapshots))
                .lackItems(lackItems)
                .build();
    }

}
