package com.ruyuan.eshop.address.api;

import com.ruyuan.eshop.address.domain.dto.AddressDTO;
import com.ruyuan.eshop.address.domain.query.AddressQuery;
import com.ruyuan.eshop.common.core.JsonResult;

/**
 * 地址服务业务接口
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface AddressApi {

    /**
     * 查询地址
     *
     * @return
     */
    JsonResult<AddressDTO> queryAddress(AddressQuery query);
}
