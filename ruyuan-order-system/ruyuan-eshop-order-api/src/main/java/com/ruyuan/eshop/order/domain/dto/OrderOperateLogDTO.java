package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 订单操作日志DTO
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class OrderOperateLogDTO implements Serializable {

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 触发来源
     */
    private Integer fromSource;

    /**
     * 前置状态
     */
    private Integer preStatus;

    /**
     * 当前状态
     */
    private Integer currentStatus;

    /**
     * 备注说明
     */
    private String remark;
}
