package com.ruyuan.eshop.tms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 发货结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendOutDTO implements Serializable {
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 物流单号
     */
    private String logisticsCode;
}
