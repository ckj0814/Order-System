package com.ruyuan.eshop.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 费用类型枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum AmountTypeEnum {

    ORIGIN_PAY_AMOUNT(10, "订单支付原价"),
    COUPON_DISCOUNT_AMOUNT(20, "优惠券抵扣金额"),
    SHIPPING_AMOUNT(30, "运费"),
    BOX_AMOUNT(40, "包装费"),
    REAL_PAY_AMOUNT(50, "实付金额"),
    OTHER_AMOUNT(127, "其他费用");

    private Integer code;

    private String msg;

    AmountTypeEnum(Integer code, String msg) {
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
        for (AmountTypeEnum element : AmountTypeEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static AmountTypeEnum getByCode(Integer code) {
        for (AmountTypeEnum element : AmountTypeEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }
}