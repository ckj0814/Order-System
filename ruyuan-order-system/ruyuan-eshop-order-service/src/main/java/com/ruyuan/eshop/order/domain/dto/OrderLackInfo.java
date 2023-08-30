package com.ruyuan.eshop.order.domain.dto;

import com.ruyuan.eshop.order.domain.entity.AfterSaleInfoDO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleItemDO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleRefundDO;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 缺品数据
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLackInfo {

    /**
     * 缺品售后单
     */
    private AfterSaleInfoDO lackAfterSaleOrder;

    /**
     * 缺品售后单条目
     */
    private List<AfterSaleItemDO> afterSaleItems;

    /**
     * 售后退款单
     */
    private AfterSaleRefundDO afterSaleRefund;

    /**
     * 订单缺品扩展信息
     */
    private OrderExtJsonDTO lackExtJson;

    /**
     * 订单Id
     */
    private String orderId;

}
