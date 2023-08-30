package com.ruyuan.eshop.order.converter;

import com.ruyuan.eshop.fulfill.domain.event.OrderDeliveredWmsEvent;
import com.ruyuan.eshop.fulfill.domain.event.OrderOutStockWmsEvent;
import com.ruyuan.eshop.fulfill.domain.event.OrderSignedWmsEvent;
import com.ruyuan.eshop.order.domain.dto.WmsShipDTO;
import org.mapstruct.Mapper;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface WmsShipDtoConverter {

    /**
     * 对象转换
     *
     * @param event 对象
     * @return 对象
     */
    WmsShipDTO convert(OrderOutStockWmsEvent event);

    /**
     * 对象转换
     *
     * @param event 对象
     * @return 对象
     */
    WmsShipDTO convert(OrderDeliveredWmsEvent event);

    /**
     * 对象转换
     *
     * @param event 对象
     * @return 对象
     */
    WmsShipDTO convert(OrderSignedWmsEvent event);

}
