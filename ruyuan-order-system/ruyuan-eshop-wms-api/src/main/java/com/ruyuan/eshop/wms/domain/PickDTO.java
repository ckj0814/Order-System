package com.ruyuan.eshop.wms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 捡货结果DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickDTO implements Serializable {
    /**
     * 订单ID
     */
    private String orderId;
}
