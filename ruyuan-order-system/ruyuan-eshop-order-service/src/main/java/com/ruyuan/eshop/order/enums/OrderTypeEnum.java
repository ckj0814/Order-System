package com.ruyuan.eshop.order.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 订单类型枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum OrderTypeEnum {

    NORMAL(1, "一般订单"),
    UNKNOWN(127, "其他");


    private Integer code;

    private String msg;

    OrderTypeEnum(Integer code, String msg) {
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
        for (OrderTypeEnum element : OrderTypeEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static OrderTypeEnum getByCode(Integer code) {
        for (OrderTypeEnum element : OrderTypeEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }

    public static Set<Integer> allowableValues() {
        Set<Integer> allowableValues = new HashSet<>(values().length);
        for (OrderTypeEnum orderTypeEnum : values()) {
            allowableValues.add(orderTypeEnum.getCode());
        }
        return allowableValues;
    }

}