package com.ruyuan.eshop.common.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单完成支付消息
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class PaidOrderSuccessMessage implements Serializable {

    private static final long serialVersionUID = 2575864833116171389L;

    private String orderId;
}