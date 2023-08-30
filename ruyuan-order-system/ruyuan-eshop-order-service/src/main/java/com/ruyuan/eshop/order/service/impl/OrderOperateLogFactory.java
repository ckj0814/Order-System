package com.ruyuan.eshop.order.service.impl;

import com.ruyuan.eshop.common.enums.OrderOperateTypeEnum;
import com.ruyuan.eshop.common.enums.OrderStatusChangeEnum;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import com.ruyuan.eshop.order.domain.entity.OrderOperateLogDO;
import org.springframework.stereotype.Component;

/**
 * 订单操作日志工厂
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class OrderOperateLogFactory {

    /**
     * 获取订单操作日志
     *
     * @param statusChange 订单状态变更
     * @return 订单操作内容
     */
    public OrderOperateLogDO get(OrderInfoDO order, OrderStatusChangeEnum statusChange) {
        OrderOperateTypeEnum operateType = statusChange.getOperateType();
        Integer preStatus = statusChange.getPreStatus().getCode();
        Integer currentStatus = statusChange.getCurrentStatus().getCode();
        return create(order, operateType, preStatus, currentStatus, operateType.getMsg());
    }

    /**
     * 创建订单操作日志
     *
     * @param order
     * @param operateType
     * @param preStatus
     * @param currentStatus
     * @param operateRemark
     * @return
     * @throws Exception
     */
    private OrderOperateLogDO create(OrderInfoDO order,
                                     OrderOperateTypeEnum operateType, int preStatus, int currentStatus, String operateRemark) {
        OrderOperateLogDO log = new OrderOperateLogDO();

        log.setOrderId(order.getOrderId());
        log.setOperateType(operateType.getCode());
        log.setPreStatus(preStatus);
        log.setCurrentStatus(currentStatus);
        log.setRemark(operateRemark);

        return log;
    }


}
