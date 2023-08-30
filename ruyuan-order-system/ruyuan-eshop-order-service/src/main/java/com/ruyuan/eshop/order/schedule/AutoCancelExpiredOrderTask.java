package com.ruyuan.eshop.order.schedule;


import com.ruyuan.eshop.order.config.OrderProperties;
import com.ruyuan.eshop.order.dao.OrderInfoDAO;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import com.ruyuan.eshop.order.domain.request.CancelOrderRequest;
import com.ruyuan.eshop.order.enums.OrderCancelTypeEnum;
import com.ruyuan.eshop.order.service.OrderAfterSaleService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

/**
 * 自动取消超时订单任务
 *
 * @author zhonghuashishan
 */
@Slf4j
@Component
public class AutoCancelExpiredOrderTask {

    /**
     * 订单管理DAO组件
     */
    @Autowired
    private OrderInfoDAO orderInfoDAO;

    @Autowired
    private OrderAfterSaleService orderAfterSaleService;

    @Autowired
    private OrderProperties orderProperties;

    /**
     * 执行任务逻辑
     */
    @XxlJob("autoCancelExpiredOrderTask")
    public void execute() throws Exception {

        int shardIndex = Optional.ofNullable(XxlJobHelper.getShardIndex()).orElse(0);
        int totalShardNum = Optional.ofNullable(XxlJobHelper.getShardTotal()).orElse(0);
        String param = XxlJobHelper.getJobParam();

        // 扫描当前时间 - 订单超时时间 -> 前的一小段时间范围(时间范围用配置中心配置)
        // 比如当前时间11:40，订单超时时间是30分钟，扫描11:09:00 -> 11:10:00这一分钟的未支付订单，
        // 缺点：有一个订单超过了30 + 1 = 31分钟，都没有被处理(取消)，这笔订单就永久待支付
        for (OrderInfoDO order : orderInfoDAO.listAllUnPaid()) {

            if(totalShardNum<=0) {
                //不进行分片
                doExecute(order);
            }
            else {
                //分片
                int hash = hash(order.getOrderId()) % totalShardNum;
                if(hash == shardIndex) {
                    doExecute(order);
                }
            }
        }
        XxlJobHelper.handleSuccess();
    }

    private void doExecute(OrderInfoDO order) {
        if (new Date().getTime() -
                order.getExpireTime().getTime() >= orderProperties.getExpireTime()) {
            // 超过30min未支付
            CancelOrderRequest request = new CancelOrderRequest();
            request.setOrderId(order.getOrderId());
            request.setUserId(order.getUserId());
            request.setBusinessIdentifier(order.getBusinessIdentifier());
            request.setOrderType(order.getOrderType());
            request.setCancelType(OrderCancelTypeEnum.TIMEOUT_CANCELED.getCode());
            request.setOrderStatus(order.getOrderStatus());
            try {
                orderAfterSaleService.cancelOrder(request);
            } catch (Exception e) {
                log.error("AutoCancelExpiredOrderTask execute error:", e);
            }
        }
    }

    /**
     * hash
     *
     * @param orderId
     * @return
     */
    private int hash(String orderId) {
        //解决取模可能为负数的情况
        return orderId.hashCode() & Integer.MAX_VALUE;
    }
}
