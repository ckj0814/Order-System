package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class GenOrderIdDTO implements Serializable {

    /**
     * 订单号
     */
    private String orderId;

}