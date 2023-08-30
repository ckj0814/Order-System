package com.ruyuan.eshop.wms.domain.dto;

import com.ruyuan.eshop.wms.domain.entity.DeliveryOrderDO;
import com.ruyuan.eshop.wms.domain.entity.DeliveryOrderItemDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 调度出库结果
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDeliveryResult {

    /**
     * 调度出库单
     */
    private DeliveryOrderDO deliveryOrder;

    /**
     * 调度出库单条目
     */
    private List<DeliveryOrderItemDO> deliveryOrderItems;
}
