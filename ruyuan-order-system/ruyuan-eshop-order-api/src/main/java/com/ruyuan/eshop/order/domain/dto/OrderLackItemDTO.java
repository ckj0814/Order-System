package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 订单缺品信息DTO
 */
@Data
public class OrderLackItemDTO implements Serializable {

    /**
     * 售后信息
     */
    private AfterSaleInfoDTO afterSaleInfo;

    /**
     * 售后单条目
     */
    private List<AfterSaleItemDTO> afterSaleItems;

    /**
     * 售后支付信息
     */
    private List<AfterSalePayDTO> afterSalePays;
}
