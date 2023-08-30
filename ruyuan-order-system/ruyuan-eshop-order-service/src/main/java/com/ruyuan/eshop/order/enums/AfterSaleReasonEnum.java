package com.ruyuan.eshop.order.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 售后申请原因枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum AfterSaleReasonEnum {
    ITEM_NUM(10, "商品数量原因"),
    ITEM_QUALITY(20, "商品质量原因"),
    ITEM_PACKAGE(30, "商品包装原因"),
    LOGISTICS(40, "物流原因"),
    DELIVERY(50, "快递员原因"),
    USER(60, "用户自己原因"),
    ITEM_PRICE(70, "商品价格原因"),
    CANCEL(80, "取消订单"),
    FORCED_CANCELLATION(90, "平台强制取消订单"),
    DISHONOR(100, "拒收"),
    OTHER(200, "其他");
    private Integer code;

    private String msg;

    AfterSaleReasonEnum(Integer code, String msg) {
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
        for (AfterSaleReasonEnum element : AfterSaleReasonEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static AfterSaleReasonEnum getByCode(Integer code) {
        for (AfterSaleReasonEnum element : AfterSaleReasonEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }

    public static Set<Integer> allowableValues() {
        Set<Integer> allowableValues = new HashSet<>(values().length);
        for (AfterSaleReasonEnum statusEnum : values()) {
            allowableValues.add(statusEnum.getCode());
        }
        return allowableValues;
    }
}
