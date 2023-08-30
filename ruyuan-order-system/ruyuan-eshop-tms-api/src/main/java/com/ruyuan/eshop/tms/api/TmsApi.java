package com.ruyuan.eshop.tms.api;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.tms.domain.SendOutDTO;
import com.ruyuan.eshop.tms.domain.SendOutRequest;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public interface TmsApi {

    /**
     * 发货
     */
    JsonResult<SendOutDTO> sendOut(SendOutRequest request);

    /**
     * 取消发货
     */
    JsonResult<Boolean> cancelSendOut(String orderId);
}
