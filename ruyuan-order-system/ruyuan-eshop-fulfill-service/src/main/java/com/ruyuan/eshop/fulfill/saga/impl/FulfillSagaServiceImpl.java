package com.ruyuan.eshop.fulfill.saga.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruyuan.eshop.fulfill.domain.request.ReceiveFulfillRequest;
import com.ruyuan.eshop.fulfill.exception.FulfillBizException;
import com.ruyuan.eshop.fulfill.saga.FulfillSagaService;
import com.ruyuan.eshop.fulfill.service.FulfillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Service("fulfillSagaService")
@Slf4j
public class FulfillSagaServiceImpl implements FulfillSagaService {

    @Autowired
    private FulfillService fulfillService;


    @Override
    public Boolean createFulfillOrder(ReceiveFulfillRequest request) {

        log.info("创建履约单，request={}", JSONObject.toJSONString(request));

        String fulfillException = request.getFulfillException();
        if (StringUtils.isNotBlank(fulfillException) && fulfillException.equals("true")) {
            throw new FulfillBizException("创建履约单异常！");
        }

        //创建履约单
        fulfillService.createFulfillOrder(request);

        return true;
    }

    @Override
    public Boolean createFulfillOrderCompensate(ReceiveFulfillRequest request) {
        log.info("补偿创建履约单，request={}", JSONObject.toJSONString(request));

        //取消履约单
        fulfillService.cancelFulfillOrder(request.getOrderId());

        log.info("补偿创建履约单结束，request={}", JSONObject.toJSONString(request));

        return true;
    }


}
