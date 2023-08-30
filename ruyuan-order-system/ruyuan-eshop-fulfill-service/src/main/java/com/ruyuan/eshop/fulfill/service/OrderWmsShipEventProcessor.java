package com.ruyuan.eshop.fulfill.service;

import com.ruyuan.eshop.fulfill.domain.request.TriggerOrderWmsShipEventRequest;

/**
 * 订单物流配送结果处理器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface OrderWmsShipEventProcessor {

    /**
     * 执行
     *
     * @param request
     */
    void execute(TriggerOrderWmsShipEventRequest request);

}
