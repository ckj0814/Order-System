package com.ruyuan.eshop.order.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 售后申请来源枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum AfterSaleApplySourceEnum {

    USER_APPLY(10, "用户申请退款"),
    SYSTEM(20, "系统自动退款"),
    CUSTOM_APPLY(30, "客服申请退款"),
    USER_RETURN_GOODS(40, "用户申请退货"),
    FULFILL_RETURN_MONEY(50, "履约申请退款"),
    ;


    private Integer code;

    private String msg;

    AfterSaleApplySourceEnum(Integer code, String msg) {
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
        for (AfterSaleApplySourceEnum element : AfterSaleApplySourceEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static AfterSaleApplySourceEnum getByCode(Integer code) {
        for (AfterSaleApplySourceEnum element : AfterSaleApplySourceEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }

    public static Set<Integer> allowableValues() {
        Set<Integer> allowableValues = new HashSet<>(values().length);
        for (AfterSaleApplySourceEnum sourceEnum : values()) {
            allowableValues.add(sourceEnum.getCode());
        }
        return allowableValues;
    }

    /**
     * 用户主动申请
     *
     * @return
     */
    public static Set<Integer> userApply() {
        Set<Integer> values = new HashSet<>();
        values.add(USER_APPLY.getCode());
        values.add(USER_RETURN_GOODS.getCode());
        return values;
    }
}