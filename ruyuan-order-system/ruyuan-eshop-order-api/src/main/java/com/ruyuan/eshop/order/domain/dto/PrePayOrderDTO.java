package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class PrePayOrderDTO implements Serializable {

    private static final long serialVersionUID = 8216786870818620855L;

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
     * 支付系统返回的信息
     * 不同的字符方式，返回的内容会不一样
     */
    private Map<String, Object> payData;

}