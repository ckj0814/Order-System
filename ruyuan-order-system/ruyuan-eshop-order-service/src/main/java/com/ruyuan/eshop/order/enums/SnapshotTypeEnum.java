package com.ruyuan.eshop.order.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 快照类型
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum SnapshotTypeEnum {

    ORDER_COUPON(1, "订单优惠券信息"),
    ORDER_AMOUNT(2, "订单费用信息"),
    ORDER_ITEM(3, "订单条目信息");

    private Integer code;

    private String msg;

    SnapshotTypeEnum(Integer code, String msg) {
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
        for (SnapshotTypeEnum element : SnapshotTypeEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static SnapshotTypeEnum getByCode(Integer code) {
        for (SnapshotTypeEnum element : SnapshotTypeEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }

}