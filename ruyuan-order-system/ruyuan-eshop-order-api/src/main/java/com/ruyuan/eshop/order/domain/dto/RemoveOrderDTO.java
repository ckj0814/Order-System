package com.ruyuan.eshop.order.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 移除订单的响应
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveOrderDTO implements Serializable {
    /**
     * 响应结果
     */
    private boolean result;

}