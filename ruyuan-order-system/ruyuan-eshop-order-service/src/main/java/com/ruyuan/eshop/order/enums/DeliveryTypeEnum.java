package com.ruyuan.eshop.order.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 配送方式枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum DeliveryTypeEnum {

    NULL(0, "无配送方式"),
    SELF(1, "自主配送");

    private Integer code;

    private String msg;

    DeliveryTypeEnum(Integer code, String msg) {
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
        for (DeliveryTypeEnum element : DeliveryTypeEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static DeliveryTypeEnum getByCode(Integer code) {
        for (DeliveryTypeEnum element : DeliveryTypeEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }

}