package com.ruyuan.eshop.order.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LackDTO implements Serializable {
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 售后id
     */
    private Long afterSaleId;
}
