package com.ruyuan.eshop.order.manager;

/**
 * 订单号生成manager组件
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface OrderNoManager {

    /**
     * 生成订单号
     *
     * @param orderNoType 订单号类型
     * @param userId      用户ID
     * @return
     */
    String genOrderId(Integer orderNoType, String userId);

}