package com.ruyuan.eshop.order.api.impl;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.common.page.PagingInfo;
import com.ruyuan.eshop.common.utils.ParamCheckUtil;
import com.ruyuan.eshop.order.api.OrderQueryApi;
import com.ruyuan.eshop.order.converter.OrderConverter;
import com.ruyuan.eshop.order.domain.dto.OrderDetailDTO;
import com.ruyuan.eshop.order.domain.dto.OrderListDTO;
import com.ruyuan.eshop.order.domain.query.AcceptOrderQuery;
import com.ruyuan.eshop.order.domain.query.OrderQuery;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
import com.ruyuan.eshop.order.service.OrderQueryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


/**
 * 订单中心-订单查询业务接口
 *
 * @author zhonghuashishan
 */
@Slf4j
@DubboService(version = "1.0.0", interfaceClass = OrderQueryApi.class)
public class OrderQueryApiImpl implements OrderQueryApi {

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private OrderConverter orderConverter;

    /**
     * 查询订单列表
     *
     * @param acceptOrderQuery
     * @return
     */
    @Override
    public JsonResult<PagingInfo<OrderListDTO>> listOrders(AcceptOrderQuery acceptOrderQuery) {
        try {
            // 1、参数校验
            orderQueryService.checkQueryParam(acceptOrderQuery);

            // 2、组装查询参数
            OrderQuery query = buildQueryParam(acceptOrderQuery);

            // 3、查询
            return JsonResult.buildSuccess(orderQueryService.executeListQuery(query));

        } catch (OrderBizException e) {
            log.error("biz error", e);
            return JsonResult.buildError(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            log.error("error", e);
            return JsonResult.buildError(e.getMessage());
        }
    }


    /**
     * 组装查询参数
     */
    private OrderQuery buildQueryParam(AcceptOrderQuery acceptOrderQuery) {
        OrderQuery query = orderConverter.convertAcceptOrderQuery(acceptOrderQuery);

        //  查询创建时间的区间范围
        Date queryStartCreatedTime = acceptOrderQuery.getQueryStartCreatedTime();
        Date queryEndCreatedTime = acceptOrderQuery.getQueryEndCreatedTime();
        Pair<Date, Date> createdTimeInterval = Pair.of(queryStartCreatedTime, queryEndCreatedTime);
        query.setCreatedTimeInterval(createdTimeInterval);

        //  查询支付时间的区间范围
        Date queryStartPayTime = acceptOrderQuery.getQueryStartPayTime();
        Date queryEndPayTime = acceptOrderQuery.getQueryEndPayTime();
        Pair<Date, Date> payTimeInterval = Pair.of(queryStartPayTime, queryEndPayTime);
        query.setPayTimeInterval(payTimeInterval);

        //  查询支付金额的区间范围
        Integer queryStartPayAmount = acceptOrderQuery.getQueryStartPayAmount();
        Integer queryEndPayAmount = acceptOrderQuery.getQueryEndPayAmount();
        Pair<Integer, Integer> payAmountInterval = Pair.of(queryStartPayAmount, queryEndPayAmount);
        query.setPayAmountInterval(payAmountInterval);

        return query;
    }

    /**
     * 查询订单详情
     *
     * @param orderId
     * @return
     */
    @Override
    public JsonResult<OrderDetailDTO> orderDetail(String orderId) {
        try {
            //1、参数校验
            ParamCheckUtil.checkStringNonEmpty(orderId, OrderErrorCodeEnum.ORDER_ID_IS_NULL);

            //2、查询
            return JsonResult.buildSuccess(orderQueryService.orderDetail(orderId));

        } catch (OrderBizException e) {
            log.error("biz error", e);
            return JsonResult.buildError(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            log.error("error", e);
            return JsonResult.buildError(e.getMessage());
        }
    }
}
