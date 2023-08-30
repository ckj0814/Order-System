package com.ruyuan.eshop.fulfill.saga;


import com.ruyuan.eshop.fulfill.domain.request.ReceiveFulfillRequest;

/**
 * fulfull saga service
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface FulfillSagaService {

    /**
     * 创建履约单
     *
     * @param request
     * @return
     */
    Boolean createFulfillOrder(ReceiveFulfillRequest request);


    /**
     * 补偿创建履约单
     *
     * @param request
     * @return
     */
    Boolean createFulfillOrderCompensate(ReceiveFulfillRequest request);
}
