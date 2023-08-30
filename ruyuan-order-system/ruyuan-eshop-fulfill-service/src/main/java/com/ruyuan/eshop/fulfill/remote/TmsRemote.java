package com.ruyuan.eshop.fulfill.remote;


import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.fulfill.exception.FulfillBizException;
import com.ruyuan.eshop.tms.api.TmsApi;
import com.ruyuan.eshop.tms.domain.SendOutDTO;
import com.ruyuan.eshop.tms.domain.SendOutRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * tms服务远程接口
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class TmsRemote {


    /**
     * 库存服务
     */
    @DubboReference(version = "1.0.0", retries = 0)
    private TmsApi tmsApi;


    /**
     * 发货
     */
    public SendOutDTO sendOut(SendOutRequest request) {
        JsonResult<SendOutDTO> jsonResult = tmsApi.sendOut(request);
        if (!jsonResult.getSuccess()) {
            throw new FulfillBizException(jsonResult.getErrorCode(), jsonResult.getErrorMessage());
        }
        return jsonResult.getData();
    }

    /**
     * 取消发货
     */
    public void cancelSendOut(String orderId) {
        JsonResult<Boolean> jsonResult = tmsApi.cancelSendOut(orderId);
        if (!jsonResult.getSuccess()) {
            throw new FulfillBizException(jsonResult.getErrorCode(), jsonResult.getErrorMessage());
        }
    }

}
