package com.ruyuan.eshop.fulfill.remote;


import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.fulfill.exception.FulfillBizException;
import com.ruyuan.eshop.wms.api.WmsApi;
import com.ruyuan.eshop.wms.domain.PickDTO;
import com.ruyuan.eshop.wms.domain.PickGoodsRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * wms服务远程接口
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class WmsRemote {


    /**
     * 库存服务
     */
    @DubboReference(version = "1.0.0", retries = 0)
    private WmsApi wmsApi;


    /**
     * 捡货
     */
    public PickDTO pickGoods(PickGoodsRequest request) {
        JsonResult<PickDTO> jsonResult = wmsApi.pickGoods(request);
        if (!jsonResult.getSuccess()) {
            throw new FulfillBizException(jsonResult.getErrorCode(), jsonResult.getErrorMessage());
        }
        return jsonResult.getData();
    }

    /**
     * 取消捡货
     */
    public void cancelPickGoods(String orderId) {
        JsonResult<Boolean> jsonResult = wmsApi.cancelPickGoods(orderId);
        if (!jsonResult.getSuccess()) {
            throw new FulfillBizException(jsonResult.getErrorCode(), jsonResult.getErrorMessage());
        }
    }

}
