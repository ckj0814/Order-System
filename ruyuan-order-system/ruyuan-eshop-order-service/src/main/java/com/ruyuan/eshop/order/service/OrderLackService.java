package com.ruyuan.eshop.order.service;

import com.ruyuan.eshop.order.domain.dto.CheckLackDTO;
import com.ruyuan.eshop.order.domain.dto.LackDTO;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import com.ruyuan.eshop.order.domain.request.LackRequest;
import com.ruyuan.eshop.order.exception.OrderBizException;

/**
 * 订单缺品相关service
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface OrderLackService {

    /**
     * 校验入参
     *
     * @param request
     */
    CheckLackDTO checkRequest(LackRequest request) throws OrderBizException;

    /**
     * 订单是否已经发起过缺品
     *
     * @param order
     * @return
     */
    boolean isOrderLacked(OrderInfoDO order);

    /**
     * 具体的缺品处理
     *
     * @param request
     * @param checkLackDTO
     * @return
     */
    LackDTO executeLackRequest(LackRequest request, CheckLackDTO checkLackDTO) throws Exception;
}
