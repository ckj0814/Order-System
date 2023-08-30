package com.ruyuan.eshop.common.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 售后类型详情枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum AfterSaleTypeDetailEnum {

    ALL_REFUND(1, "售后全额退款"),
    TIMEOUT_NO_PAY(2, "超时未支付"),
    USER_CANCEL(3, "用户自主取消"),
    CUSTOMER_CANCEL(4, "授权客服取消"),
    PART_REFUND(5, "售后退货退款"),
    LACK_REFUND(6, "缺品退款"),
    ;


    private Integer code;

    private String msg;

    AfterSaleTypeDetailEnum(Integer code, String msg) {
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
        for (AfterSaleTypeDetailEnum element : AfterSaleTypeDetailEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static AfterSaleTypeDetailEnum getByCode(Integer code) {
        for (AfterSaleTypeDetailEnum element : AfterSaleTypeDetailEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }

    public static Set<Integer> allowableValues() {
        Set<Integer> allowableValues = new HashSet<>(values().length);
        for (AfterSaleTypeDetailEnum typeEnun : values()) {
            allowableValues.add(typeEnun.getCode());
        }
        return allowableValues;
    }
}