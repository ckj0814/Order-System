package com.ruyuan.eshop.fulfill.saga;

import com.ruyuan.eshop.fulfill.domain.request.ReceiveFulfillRequest;

/**
 * wms的saga service
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface WmsSagaService {

    /**
     * 捡货
     *
     * @param request
     * @return
     */
    Boolean pickGoods(ReceiveFulfillRequest request);

    /**
     * 捡货补偿
     *
     * @param request
     * @return
     */
    Boolean pickGoodsCompensate(ReceiveFulfillRequest request);
}
