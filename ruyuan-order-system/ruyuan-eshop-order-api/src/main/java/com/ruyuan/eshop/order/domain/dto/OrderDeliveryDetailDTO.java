package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单配送信息DTO
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class OrderDeliveryDetailDTO implements Serializable {

    /**
     * 订单编号
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
    private String address;

    /**
     * 详细地址
     */
    private String fullAddress;

    /**
     * 经度
     */
    private BigDecimal lon;

    /**
     * 维度
     */
    private BigDecimal lat;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 调整地址次数
     */
    private Integer modifyAddressCount;

    /**
     * 配送员姓名
     */
    private String delivererName;
    /**
     * 配送员手机号
     */
    private String delivererPhone;

    /**
     * 出库时间
     */
    private Date outStockTime;
    /**
     * 签收时间
     */
    private Date signedTime;
}
