package com.ruyuan.eshop.order.manager;

import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import com.ruyuan.eshop.order.domain.request.CancelOrderAssembleRequest;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public interface AfterSaleManager {
    /**
     * 执行履约取消、更新订单状态、新增订单日志操作
     */
    void cancelOrderFulfillmentAndUpdateOrderStatus(CancelOrderAssembleRequest cancelOrderAssembleRequest);

    /**
     * 取消订单操作 记录售后信息
     */
    void insertCancelOrderAfterSale(CancelOrderAssembleRequest cancelOrderAssembleRequest, Integer afterSaleStatus,
                                    OrderInfoDO orderInfoDO, String afterSaleId);
}
