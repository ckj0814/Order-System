package com.ruyuan.eshop.order.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 调整订单配送地址结果
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdjustDeliveryAddressDTO implements Serializable {

    /**
     * 响应结果
     */
    private boolean result;
}