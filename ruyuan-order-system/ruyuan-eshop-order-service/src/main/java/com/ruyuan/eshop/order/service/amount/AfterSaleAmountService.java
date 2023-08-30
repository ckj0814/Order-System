package com.ruyuan.eshop.order.service.amount;

import com.ruyuan.eshop.order.dao.OrderItemDAO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleItemDO;
import com.ruyuan.eshop.order.domain.entity.OrderItemDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 售后金额计算service
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class AfterSaleAmountService {

    @Autowired
    private OrderItemDAO orderItemDAO;

    /**
     * 计算订单条目缺品实际退款金额
     * 计算公式：（缺品数量/下单数量） * 原付款金额
     *
     * @param orderItem
     * @param lackNum
     * @return
     */
    public Integer calculateOrderItemLackRealRefundAmount(OrderItemDO orderItem, Integer lackNum) {
        double rate = lackNum / orderItem.getSaleQuantity().doubleValue();
        //金额向上取整
        Integer itemRefundAmount = Double.valueOf(Math.ceil(orderItem.getPayAmount() * rate)).intValue();
        return itemRefundAmount;
    }

    /**
     * 计算订单总申请退款金额
     *
     * @param lackItems
     * @return
     */
    public Integer calculateOrderLackApplyRefundAmount(List<AfterSaleItemDO> lackItems) {

        Integer applyRefundAmount = 0;

        for (AfterSaleItemDO item : lackItems) {
            applyRefundAmount += item.getApplyRefundAmount();
        }

        return applyRefundAmount;
    }

    /**
     * 计算订单总实际退款金额
     *
     * @param lackItems
     * @return
     */
    public Integer calculateOrderLackRealRefundAmount(List<AfterSaleItemDO> lackItems) {

        Integer realRefundAmount = 0;

        for (AfterSaleItemDO item : lackItems) {
            realRefundAmount += item.getRealRefundAmount();
        }

        return realRefundAmount;
    }

}
