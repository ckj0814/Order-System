package com.ruyuan.eshop.common.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 售后类型枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum AfterSaleTypeEnum {

    RETURN_MONEY(1, "退款"),
    RETURN_GOODS(2, "退货");


    private Integer code;

    private String msg;

    AfterSaleTypeEnum(Integer code, String msg) {
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
        for (AfterSaleTypeEnum element : AfterSaleTypeEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static AfterSaleTypeEnum getByCode(Integer code) {
        for (AfterSaleTypeEnum element : AfterSaleTypeEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }

    public static Set<Integer> allowableValues() {
        Set<Integer> allowableValues = new HashSet<>(values().length);
        for (AfterSaleTypeEnum typeEnun : values()) {
            allowableValues.add(typeEnun.getCode());
        }
        return allowableValues;
    }
}