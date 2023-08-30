package com.ruyuan.eshop.order.service.impl;

import com.ruyuan.eshop.common.enums.AfterSaleTypeDetailEnum;
import com.ruyuan.eshop.common.enums.AfterSaleTypeEnum;
import com.ruyuan.eshop.order.dao.AfterSaleInfoDAO;
import com.ruyuan.eshop.order.dao.AfterSaleItemDAO;
import com.ruyuan.eshop.order.dao.AfterSaleRefundDAO;
import com.ruyuan.eshop.order.dao.OrderInfoDAO;
import com.ruyuan.eshop.order.domain.dto.LackItemDTO;
import com.ruyuan.eshop.order.domain.dto.OrderExtJsonDTO;
import com.ruyuan.eshop.order.domain.dto.OrderLackInfo;
import com.ruyuan.eshop.order.domain.dto.OrderLackInfoDTO;
import com.ruyuan.eshop.order.domain.entity.*;
import com.ruyuan.eshop.order.domain.request.LackRequest;
import com.ruyuan.eshop.order.enums.*;
import com.ruyuan.eshop.order.manager.OrderNoManager;
import com.ruyuan.eshop.order.service.amount.AfterSaleAmountService;
import com.ruyuan.eshop.product.domain.dto.ProductSkuDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 订单缺品处理器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
@Slf4j
public class OrderLackProcessor {

    @Autowired
    private OrderNoManager orderNoManager;

    @Autowired
    private OrderInfoDAO orderInfoDAO;

    @Autowired
    private AfterSaleInfoDAO afterSaleInfoDAO;

    @Autowired
    private AfterSaleItemDAO afterSaleItemDAO;

    @Autowired
    private AfterSaleRefundDAO afterSaleRefundDAO;

    @Autowired
    private AfterSaleAmountService afterSaleAmountService;

    /**
     * 保存缺品数据
     *
     * @param orderLackInfo
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveLackInfo(OrderLackInfo orderLackInfo) {

        // 1、存储售后单,item和退款单;
        afterSaleInfoDAO.save(orderLackInfo.getLackAfterSaleOrder());
        afterSaleItemDAO.saveBatch(orderLackInfo.getAfterSaleItems());
        afterSaleRefundDAO.save(orderLackInfo.getAfterSaleRefund());
        // 2、更新订单扩展信息
        orderInfoDAO.updateOrderExtJson(orderLackInfo.getOrderId(), orderLackInfo.getLackExtJson());
    }

    /**
     * 构造缺品售后单
     *
     * @param order
     * @return
     */
    private AfterSaleInfoDO buildLackAfterSaleInfo(OrderInfoDO order) {

        //构造售后单
        String userId = order.getUserId();
        AfterSaleInfoDO afterSaleInfoDO = new AfterSaleInfoDO();
        String afterSaleId = orderNoManager.genOrderId(OrderNoTypeEnum.AFTER_SALE.getCode(), userId);
        afterSaleInfoDO.setAfterSaleId(Long.valueOf(afterSaleId));
        afterSaleInfoDO.setOrderId(order.getOrderId());
        afterSaleInfoDO.setOrderSourceChannel(BusinessIdentifierEnum.SELF_MALL.getCode());
        afterSaleInfoDO.setUserId(userId);
        afterSaleInfoDO.setOrderType(OrderTypeEnum.NORMAL.getCode());
        afterSaleInfoDO.setAfterSaleType(AfterSaleTypeEnum.RETURN_MONEY.getCode());
        afterSaleInfoDO.setAfterSaleTypeDetail(AfterSaleTypeDetailEnum.LACK_REFUND.getCode());
        afterSaleInfoDO.setApplySource(AfterSaleApplySourceEnum.SYSTEM.getCode());
        afterSaleInfoDO.setAfterSaleStatus(AfterSaleStatusEnum.REVIEW_PASS.getCode());
        afterSaleInfoDO.setApplyTime(new Date());
        afterSaleInfoDO.setReviewTime(new Date());

        return afterSaleInfoDO;

    }

    /**
     * 构造缺品售后单item
     *
     * @param order
     * @return
     */
    private AfterSaleItemDO buildLackAfterSaleItem(OrderInfoDO order, AfterSaleInfoDO lackAfterSale
            , LackItemDTO lackItemDTO) {
        Integer lackNum = lackItemDTO.getLackNum();
        ProductSkuDTO productSku = lackItemDTO.getProductSku();
        OrderItemDO orderItem = lackItemDTO.getOrderItem();

        AfterSaleItemDO afterSaleItemDO = new AfterSaleItemDO();
        afterSaleItemDO.setAfterSaleId(lackAfterSale.getAfterSaleId());
        afterSaleItemDO.setOrderId(order.getOrderId());
        afterSaleItemDO.setProductName(productSku.getProductName());
        afterSaleItemDO.setSkuCode(productSku.getSkuCode());
        afterSaleItemDO.setReturnQuantity(lackNum);
        afterSaleItemDO.setProductImg(orderItem.getProductImg());
        afterSaleItemDO.setOriginAmount(orderItem.getOriginAmount());
        //计算sku缺品退款金额
        afterSaleItemDO.setApplyRefundAmount(orderItem.getSalePrice() * lackNum);
        afterSaleItemDO.setRealRefundAmount(afterSaleAmountService.calculateOrderItemLackRealRefundAmount(orderItem, lackNum));
        return afterSaleItemDO;
    }

    /**
     * 构造缺品售后支付单
     *
     * @param order
     * @return
     */
    private AfterSaleRefundDO buildLackAfterSaleRefundDO(OrderInfoDO order, AfterSaleInfoDO afterSaleInfo) {

        //构造售后单
        AfterSaleRefundDO AfterSaleRefundDO = new AfterSaleRefundDO();
        AfterSaleRefundDO.setAfterSaleId(String.valueOf(afterSaleInfo.getAfterSaleId()));
        AfterSaleRefundDO.setOrderId(order.getOrderId());
        AfterSaleRefundDO.setAccountType(AccountTypeEnum.THIRD.getCode());
        AfterSaleRefundDO.setPayType(order.getPayType());
        AfterSaleRefundDO.setRefundAmount(afterSaleInfo.getRealRefundAmount());
        AfterSaleRefundDO.setRefundStatus(RefundStatusEnum.UN_REFUND.getCode());

        return AfterSaleRefundDO;
    }

    /**
     * 构造订单缺品扩展信息
     *
     * @return
     */
    private OrderExtJsonDTO buildOrderLackExtJson(LackRequest request, OrderInfoDO order) {

        OrderExtJsonDTO orderExtJson = new OrderExtJsonDTO();
        orderExtJson.setLackFlag(true);

        OrderLackInfoDTO lackInfo = new OrderLackInfoDTO();
        lackInfo.setLackItems(request.getLackItems());
        lackInfo.setOrderId(order.getOrderId());
        lackInfo.setApplyRefundAmount(lackInfo.getApplyRefundAmount());
        lackInfo.setRealRefundAmount(lackInfo.getRealRefundAmount());
        orderExtJson.setLackInfo(lackInfo);

        return orderExtJson;
    }
}
