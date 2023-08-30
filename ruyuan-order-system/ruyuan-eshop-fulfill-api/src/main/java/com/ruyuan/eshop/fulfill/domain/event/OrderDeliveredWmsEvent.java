package com.ruyuan.eshop.fulfill.domain.event;

import lombok.Data;

/**
 * 订单已配送物流结果消息
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class OrderDeliveredWmsEvent extends BaseWmsShipEvent {
    /**
     * 配送员code
     */
    private String delivererNo;
    /**
     * 配送员姓名
     */
    private String delivererName;
    /**
     * 配送员手机号
     */
    private String delivererPhone;
}
