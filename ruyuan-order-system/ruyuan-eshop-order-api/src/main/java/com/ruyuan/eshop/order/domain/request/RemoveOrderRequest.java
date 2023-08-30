package com.ruyuan.eshop.order.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 移除订单的请求
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class RemoveOrderRequest implements Serializable {

    /**
     * 要移除的订单ids
     */
    private Set<String> orderIds;

}