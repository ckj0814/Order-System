package com.ruyuan.eshop.wms.api;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.wms.domain.PickDTO;
import com.ruyuan.eshop.wms.domain.PickGoodsRequest;

/**
 * 仓储系统api
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface WmsApi {

    /**
     * 捡货
     *
     * @param request
     * @return
     */
    JsonResult<PickDTO> pickGoods(PickGoodsRequest request);

    /**
     * 取消捡货
     *
     * @param orderId
     * @return
     */
    JsonResult<Boolean> cancelPickGoods(String orderId);

}
