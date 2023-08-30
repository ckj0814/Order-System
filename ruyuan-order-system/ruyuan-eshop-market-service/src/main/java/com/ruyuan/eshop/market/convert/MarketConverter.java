package com.ruyuan.eshop.market.convert;

import com.ruyuan.eshop.market.domain.dto.CalculateOrderAmountDTO;
import com.ruyuan.eshop.market.domain.request.CalculateOrderAmountRequest;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface MarketConverter {

    /**
     * 转换对象
     *
     * @param orderAmountRequest 对象
     * @return 对象
     */
    CalculateOrderAmountDTO.OrderAmountDTO convertOrderAmountRequest(CalculateOrderAmountRequest.OrderAmountRequest orderAmountRequest);

    /**
     * 转换对象
     *
     * @param orderAmountRequestList 对象
     * @return 对象
     */
    List<CalculateOrderAmountDTO.OrderAmountDTO> convertOrderAmountRequest(List<CalculateOrderAmountRequest.OrderAmountRequest> orderAmountRequestList);
}
