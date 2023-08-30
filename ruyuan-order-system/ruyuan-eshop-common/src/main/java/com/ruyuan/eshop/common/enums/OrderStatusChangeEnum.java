package com.ruyuan.eshop.common.enums;

import lombok.Getter;

/**
 * 订单状态变化枚举
 */
@Getter
public enum OrderStatusChangeEnum {
    /**
     * 订单已履约
     */
    ORDER_FULFILLED(OrderStatusEnum.PAID, OrderStatusEnum.FULFILL, OrderOperateTypeEnum.PUSH_ORDER_FULFILL),
    /**
     * 订单已出库
     */
    ORDER_OUT_STOCKED(OrderStatusEnum.FULFILL, OrderStatusEnum.OUT_STOCK, OrderOperateTypeEnum.ORDER_OUT_STOCK),
    /**
     * 订单已配送
     */
    ORDER_DELIVERED(OrderStatusEnum.OUT_STOCK, OrderStatusEnum.DELIVERY, OrderOperateTypeEnum.ORDER_DELIVERED),
    /**
     * 订单已签收
     */
    ORDER_SIGNED(OrderStatusEnum.DELIVERY, OrderStatusEnum.SIGNED, OrderOperateTypeEnum.ORDER_SIGNED),
    ;

    OrderStatusChangeEnum(OrderStatusEnum preStatus, OrderStatusEnum currentStatus, OrderOperateTypeEnum operateType) {
        this.preStatus = preStatus;
        this.currentStatus = currentStatus;
        this.operateType = operateType;
    }

    private OrderStatusEnum preStatus;
    private OrderStatusEnum currentStatus;
    private OrderOperateTypeEnum operateType;


    public static OrderStatusChangeEnum getBy(int preStatus, int currentStatus) {
        for (OrderStatusChangeEnum element : OrderStatusChangeEnum.values()) {
            if (preStatus == element.preStatus.getCode() &&
                    currentStatus == element.currentStatus.getCode()) {
                return element;
            }
        }
        return null;
    }

    public static OrderStatusChangeEnum getBy(int operateType) {
        for (OrderStatusChangeEnum element : OrderStatusChangeEnum.values()) {
            if (operateType == element.operateType.getCode()) {
                return element;
            }
        }
        return null;
    }
}
