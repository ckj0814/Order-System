package com.ruyuan.eshop.customer.converter;

import com.ruyuan.eshop.customer.domain.entity.CustomerReceivesAfterSaleInfoDO;
import com.ruyuan.eshop.customer.domain.request.CustomerReceiveAfterSaleRequest;
import org.mapstruct.Mapper;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface CustomerConverter {

    /**
     * 对象转换
     *
     * @param customerReceiveAfterSaleRequest 对象
     * @return 对象
     */
    CustomerReceivesAfterSaleInfoDO convertCustomerReceivesAfterSalInfoDO(CustomerReceiveAfterSaleRequest customerReceiveAfterSaleRequest);
}
