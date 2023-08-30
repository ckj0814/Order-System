package com.ruyuan.eshop.pay.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 预支付订单返回结果
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class PayOrderDTO implements Serializable {

    private static final long serialVersionUID = -6780095800322751139L;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 支付系统交易单号
     */
    private String outTradeNo;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 第三方支付平台的支付数据
     */
    private Map<String, Object> payData;

}