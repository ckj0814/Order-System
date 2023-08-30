package com.ruyuan.eshop.common.message;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * 逆向订单通用事件
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@Builder
public class AfterSaleEvent<T> implements Serializable {

    /**
     * 接入方业务线标识  1, "自营商城"
     */
    private Integer businessIdentifier;

    /**
     * 售后id
     */
    private Long afterSaleId;
    /**
     * 售后类型
     */
    private Integer afterSaleType;
    /**
     * 申请售后来源
     */
    private Integer applySource;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 消息体
     */
    private T messageContent;

    @Tolerate
    public AfterSaleEvent() {

    }
}
