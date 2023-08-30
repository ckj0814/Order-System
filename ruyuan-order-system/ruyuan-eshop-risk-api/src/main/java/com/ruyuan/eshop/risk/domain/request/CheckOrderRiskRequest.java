package com.ruyuan.eshop.risk.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单风控检查入参
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class CheckOrderRiskRequest implements Serializable {

    private static final long serialVersionUID = 122317037326617884L;

    /**
     * 业务线标识
     */
    private Integer businessIdentifier;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 卖家ID
     */
    private String sellerId;

    /**
     * 客户端ip
     */
    private String clientIp;

    /**
     * 设备标识
     */
    private String deviceId;

}