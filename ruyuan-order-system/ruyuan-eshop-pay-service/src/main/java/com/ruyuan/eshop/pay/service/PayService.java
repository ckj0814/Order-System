package com.ruyuan.eshop.pay.service;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public interface PayService {

    /**
     * 实时查询支付交易流水号
     */
    Boolean getRealTimeTradeNo(String orderId, Integer businessIdentifier);
}