package com.ruyuan.eshop.order.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 接入方业务线枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum BusinessIdentifierEnum {

    SELF_MALL(1, "自营商城");

    private Integer code;

    private String msg;

    BusinessIdentifierEnum(Integer code, String msg) {
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
        for (BusinessIdentifierEnum element : BusinessIdentifierEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static BusinessIdentifierEnum getByCode(Integer code) {
        for (BusinessIdentifierEnum element : BusinessIdentifierEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }

    public static Set<Integer> allowableValues() {
        Set<Integer> allowableValues = new HashSet<>(values().length);
        for (BusinessIdentifierEnum businessIdentifierEnum : values()) {
            allowableValues.add(businessIdentifierEnum.getCode());
        }
        return allowableValues;
    }
}