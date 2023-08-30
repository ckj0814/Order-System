package com.ruyuan.eshop.pay.service.impl;

import com.ruyuan.eshop.pay.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Override
    public Boolean getRealTimeTradeNo(String orderId, Integer businessIdentifier) {
        log.info("调用支付接口，查询到该订单的支付信息");
        return true;
    }

}