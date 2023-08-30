package com.ruyuan.eshop.tms.converter;

import com.ruyuan.eshop.tms.domain.SendOutRequest;
import com.ruyuan.eshop.tms.domain.entity.LogisticOrderDO;
import org.mapstruct.Mapper;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface TmsConverter {
    /**
     * 对象转换
     *
     * @param request 对象
     * @return 对象
     */
    LogisticOrderDO convertLogisticOrderDO(SendOutRequest request);
}
