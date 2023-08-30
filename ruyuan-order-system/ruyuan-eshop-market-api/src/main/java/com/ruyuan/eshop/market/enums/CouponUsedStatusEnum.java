package com.ruyuan.eshop.market.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public enum CouponUsedStatusEnum {

    UN_USED(0, "未使用"),
    USED(1, "已使用");


    private Integer code;

    private String msg;

    CouponUsedStatusEnum(Integer code, String msg) {
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
        for (CouponUsedStatusEnum element : CouponUsedStatusEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static CouponUsedStatusEnum getByCode(Integer code) {
        for (CouponUsedStatusEnum element : CouponUsedStatusEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }

}