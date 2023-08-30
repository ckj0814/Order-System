package com.ruyuan.eshop.order.api;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.common.page.PagingInfo;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderDetailDTO;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderListDTO;
import com.ruyuan.eshop.order.domain.query.AfterSaleQuery;

/**
 * 订单中心-售后查询业务接口
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface AfterSaleQueryApi {

    /**
     * 查询售后列表
     *
     * @param query
     * @return
     */
    JsonResult<PagingInfo<AfterSaleOrderListDTO>> listAfterSales(AfterSaleQuery query);

    /**
     * 查询售后单详情
     *
     * @param afterSaleId
     * @return
     */
    JsonResult<AfterSaleOrderDetailDTO> afterSaleDetail(Long afterSaleId);

}