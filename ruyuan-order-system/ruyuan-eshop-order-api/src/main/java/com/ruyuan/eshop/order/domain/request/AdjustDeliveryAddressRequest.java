package com.ruyuan.eshop.order.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 调整订单配送地址
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@NoArgsConstructor
@Data
public class AdjustDeliveryAddressRequest implements Serializable {

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 街道地址
     */
    private String street;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 经度
     */
    private BigDecimal lon;

    /**
     * 纬度
     */
    private BigDecimal lat;
}