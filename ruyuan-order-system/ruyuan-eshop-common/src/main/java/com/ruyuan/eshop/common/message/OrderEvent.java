package com.ruyuan.eshop.common.message;

import com.ruyuan.eshop.common.enums.OrderStatusChangeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * 正向订单通用事件
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@Builder
public class OrderEvent<T> implements Serializable {

    private static final long serialVersionUID = 3183277975908088839L;

    /**
     * 接入方业务线标识  1, "自营商城"
     */
    private Integer businessIdentifier;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 订单类型 1:一般订单  255:其它
     */
    private Integer orderType;
    /**
     * 卖家编号
     */
    private String sellerId;
    /**
     * 订单变更事件
     */
    private OrderStatusChangeEnum orderStatusChange;
    /**
     * 消息体
     */
    private T messageContent;

    @Tolerate
    public OrderEvent() {

    }
}
