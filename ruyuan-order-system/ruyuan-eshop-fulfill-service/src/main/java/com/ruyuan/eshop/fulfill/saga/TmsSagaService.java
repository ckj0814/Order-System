package com.ruyuan.eshop.fulfill.saga;


import com.ruyuan.eshop.fulfill.domain.request.ReceiveFulfillRequest;

/**
 * tms saga service
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface TmsSagaService {


    /**
     * 发货
     *
     * @param request
     * @return
     */
    Boolean sendOut(ReceiveFulfillRequest request);

    /**
     * 发货补偿
     *
     * @param request
     * @return
     */
    Boolean sendOutCompensate(ReceiveFulfillRequest request);
}
