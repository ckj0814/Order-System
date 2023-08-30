package com.ruyuan.eshop.order.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付状态枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum PayStatusEnum {

    UNPAID(10, "未支付"),
    PAID(20, "已支付");

    private Integer code;

    private String msg;

    PayStatusEnum(Integer code, String msg) {
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
        for (PayStatusEnum element : PayStatusEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static PayStatusEnum getByCode(Integer code) {
        for (PayStatusEnum element : PayStatusEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }
}