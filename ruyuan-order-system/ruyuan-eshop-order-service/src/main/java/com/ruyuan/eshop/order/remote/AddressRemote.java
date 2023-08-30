package com.ruyuan.eshop.order.remote;

import com.ruyuan.eshop.address.api.AddressApi;
import com.ruyuan.eshop.address.domain.dto.AddressDTO;
import com.ruyuan.eshop.address.domain.query.AddressQuery;
import com.ruyuan.eshop.common.core.JsonResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * 地址服务远程接口
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class AddressRemote {

    /**
     * 地址服务
     */
    @DubboReference(version = "1.0.0")
    private AddressApi addressApi;

    /**
     * 查询行政地址
     * @param query
     * @return
     */
    public AddressDTO queryAddress(AddressQuery query) {
        JsonResult<AddressDTO> jsonResult = addressApi.queryAddress(query);
        if (jsonResult.getSuccess() && jsonResult.getData() != null) {
            return jsonResult.getData();
        }
        return null;
    }

}