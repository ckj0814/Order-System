package com.ruyuan.eshop.order.service;

import com.ruyuan.eshop.common.page.PagingInfo;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderDetailDTO;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderListDTO;
import com.ruyuan.eshop.order.domain.dto.OrderLackItemDTO;
import com.ruyuan.eshop.order.domain.query.AfterSaleQuery;

import java.util.List;

/**
 * <p>
 * 售后查询service
 * </p>
 *
 * @author zhonghuashishan
 */
public interface AfterSaleQueryService {

    /**
     * 校验列表查询参数
     *
     * @param query
     */
    void checkQueryParam(AfterSaleQuery query);

    /**
     * 执行列表查询
     *
     * @param query
     */
    PagingInfo<AfterSaleOrderListDTO> executeListQuery(AfterSaleQuery query);

    /**
     * 查询售后单详情
     *
     * @param afterSaleId
     * @return
     */
    AfterSaleOrderDetailDTO afterSaleDetail(Long afterSaleId);

    /**
     * 查询缺品信息
     *
     * @param orderId
     * @return
     */
    List<OrderLackItemDTO> getOrderLackItemInfo(String orderId);
}
