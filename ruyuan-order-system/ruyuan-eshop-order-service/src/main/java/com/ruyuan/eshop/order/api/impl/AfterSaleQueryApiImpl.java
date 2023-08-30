package com.ruyuan.eshop.order.api.impl;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.common.page.PagingInfo;
import com.ruyuan.eshop.common.utils.ParamCheckUtil;
import com.ruyuan.eshop.order.api.AfterSaleQueryApi;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderDetailDTO;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderListDTO;
import com.ruyuan.eshop.order.domain.query.AfterSaleQuery;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
import com.ruyuan.eshop.order.service.AfterSaleQueryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 订单中心-售后查询业务接口
 *
 * @author zhonghuashishan
 */
@Slf4j
@DubboService(version = "1.0.0", interfaceClass = AfterSaleQueryApi.class)
public class AfterSaleQueryApiImpl implements AfterSaleQueryApi {

    @Autowired
    private AfterSaleQueryService afterSaleQueryService;

    @Override
    public JsonResult<PagingInfo<AfterSaleOrderListDTO>> listAfterSales(AfterSaleQuery query) {
        try {
            //1、参数校验
            afterSaleQueryService.checkQueryParam(query);

            //2、查询
            return JsonResult.buildSuccess(afterSaleQueryService.executeListQuery(query));

        } catch (OrderBizException e) {
            log.error("biz error", e);
            return JsonResult.buildError(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            log.error("error", e);
            return JsonResult.buildError(e.getMessage());
        }
    }

    @Override
    public JsonResult<AfterSaleOrderDetailDTO> afterSaleDetail(Long afterSaleId) {
        try {
            //1、参数校验
            ParamCheckUtil.checkObjectNonNull(afterSaleId, OrderErrorCodeEnum.AFTER_SALE_ID_IS_NULL);

            //2、查询
            return JsonResult.buildSuccess(afterSaleQueryService.afterSaleDetail(afterSaleId));

        } catch (OrderBizException e) {
            log.error("biz error", e);
            return JsonResult.buildError(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            log.error("error", e);
            return JsonResult.buildError(e.getMessage());
        }
    }

}