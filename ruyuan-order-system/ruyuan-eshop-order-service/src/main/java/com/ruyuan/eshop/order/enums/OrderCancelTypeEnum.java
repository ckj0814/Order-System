package com.ruyuan.eshop.order.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单取消类型枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum OrderCancelTypeEnum {

    USER_CANCELED(0, "用户手动取消"),
    TIMEOUT_CANCELED(1, "超时未支付自动取消"),
    CUSTOMER_CANCELED(2, "用户授权客服取消");

    private Integer code;

    private String msg;

    OrderCancelTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<>(16);
        for (OrderCancelTypeEnum element : OrderCancelTypeEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static OrderCancelTypeEnum getByCode(Integer code) {
        for (OrderCancelTypeEnum element : OrderCancelTypeEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }
}