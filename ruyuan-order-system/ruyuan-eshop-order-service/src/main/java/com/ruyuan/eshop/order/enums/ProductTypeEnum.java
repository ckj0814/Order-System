package com.ruyuan.eshop.order.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 商品类型枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum ProductTypeEnum {

    NORMAL_PRODUCT(1, "普通商品"),
    ADVANCE_SALE(2, "预售商品");

    private Integer code;

    private String msg;

    ProductTypeEnum(Integer code, String msg) {
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
        for (ProductTypeEnum element : ProductTypeEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static ProductTypeEnum getByCode(Integer code) {
        for (ProductTypeEnum element : ProductTypeEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }
}