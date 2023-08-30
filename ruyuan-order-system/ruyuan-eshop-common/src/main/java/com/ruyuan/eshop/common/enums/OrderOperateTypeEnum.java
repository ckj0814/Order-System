package com.ruyuan.eshop.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单操作类型枚举值
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum OrderOperateTypeEnum {

    // 新增订单
    NEW_ORDER(10, "新建订单"),

    // 手工取消订单
    MANUAL_CANCEL_ORDER(20, "手工取消订单"),

    // 超时未支付-自动取消订单
    AUTO_CANCEL_ORDER(30, "超时未支付自动取消订单"),

    // 完成订单支付
    PAID_ORDER(40, "完成订单支付"),

    // 推送订单至履约
    PUSH_ORDER_FULFILL(50, "推送订单至履约"),


    // 订单已出库
    ORDER_OUT_STOCK(60, "订单已出库"),

    ORDER_DELIVERED(70, "订单已配送"),

    ORDER_SIGNED(80, "订单已签收");

    private Integer code;

    private String msg;

    OrderOperateTypeEnum(Integer code, String msg) {
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
        for (OrderOperateTypeEnum element : OrderOperateTypeEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static OrderOperateTypeEnum getByCode(Integer code) {
        for (OrderOperateTypeEnum element : OrderOperateTypeEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }
}