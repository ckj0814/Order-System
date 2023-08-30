package com.ruyuan.eshop.order.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 账户枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum AccountTypeEnum {

    THIRD(1, "第三方"),
    OTHER(127, "其他");

    private Integer code;

    private String msg;

    AccountTypeEnum(Integer code, String msg) {
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
        for (AccountTypeEnum element : AccountTypeEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static AccountTypeEnum getByCode(Integer code) {
        for (AccountTypeEnum element : AccountTypeEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }
}