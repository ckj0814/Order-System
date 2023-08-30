package com.ruyuan.eshop.wms.converter;

import com.ruyuan.eshop.wms.domain.PickGoodsRequest;
import com.ruyuan.eshop.wms.domain.entity.DeliveryOrderDO;
import com.ruyuan.eshop.wms.domain.entity.DeliveryOrderItemDO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface WmsConverter {

    /**
     * 对象转换
     *
     * @param request 对象
     * @return 对象
     */
    DeliveryOrderDO convertDeliverOrderDO(PickGoodsRequest request);

    /**
     * 对象转换
     *
     * @param orderItem 对象
     * @return 对象
     */
    DeliveryOrderItemDO convertDeliverOrderItemDO(PickGoodsRequest.OrderItemRequest orderItem);

    /**
     * 对象转换
     *
     * @param orderItems 对象
     * @return 对象
     */
    List<DeliveryOrderItemDO> convertDeliverOrderItemDO(List<PickGoodsRequest.OrderItemRequest> orderItems);
}
