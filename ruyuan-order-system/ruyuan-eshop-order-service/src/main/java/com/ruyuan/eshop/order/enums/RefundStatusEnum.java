package com.ruyuan.eshop.order.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 退款状态枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum RefundStatusEnum {

    UN_REFUND(10, "未退款"),
    REFUND_SUCCESS(20, "退款成功"),
    REFUND_FAIL(30, "退款失败"),
    ;

    private Integer code;

    private String msg;

    RefundStatusEnum(Integer code, String msg) {
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
        for (RefundStatusEnum element : RefundStatusEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static RefundStatusEnum getByCode(Integer code) {
        for (RefundStatusEnum element : RefundStatusEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }
}