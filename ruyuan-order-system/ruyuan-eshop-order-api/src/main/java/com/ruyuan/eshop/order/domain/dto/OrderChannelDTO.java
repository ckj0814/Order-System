package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 订单渠道DTO
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class OrderChannelDTO implements Serializable {


    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 客户端平台
     */
    private Integer platform;

    /**
     * 客户端版本号
     */
    private Integer version;


}
