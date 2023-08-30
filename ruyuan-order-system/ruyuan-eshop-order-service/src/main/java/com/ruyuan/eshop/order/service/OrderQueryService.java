package com.ruyuan.eshop.order.service;

import com.ruyuan.eshop.common.page.PagingInfo;
import com.ruyuan.eshop.order.domain.dto.OrderDetailDTO;
import com.ruyuan.eshop.order.domain.dto.OrderListDTO;
import com.ruyuan.eshop.order.domain.query.AcceptOrderQuery;
import com.ruyuan.eshop.order.domain.query.OrderQuery;

/**
 * <p>
 * 订单查询service
 * </p>
 *
 * @author zhonghuashishan
 */
public interface OrderQueryService {

    /**
     * 校验列表查询参数
     *
     * @param acceptOrderQuery
     */
    void checkQueryParam(AcceptOrderQuery acceptOrderQuery);

    /**
     * 执行列表查询
     *
     * @param query
     */
    PagingInfo<OrderListDTO> executeListQuery(OrderQuery query);

    /**
     * 查询订单详情
     *
     * @param orderId
     * @return
     */
    OrderDetailDTO orderDetail(String orderId);
}
