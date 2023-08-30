package com.ruyuan.eshop.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付类型枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum PayTypeEnum {

    WECHAT_PAY(10, "微信支付"),
    ALI_PAY(20, "支付宝支付");

    private Integer code;

    private String msg;

    PayTypeEnum(Integer code, String msg) {
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
        for (PayTypeEnum element : PayTypeEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static PayTypeEnum getByCode(Integer code) {
        for (PayTypeEnum element : PayTypeEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }
}