package com.ruyuan.eshop.order.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 平台类型枚举
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum PlatformEnum {

    IOS(10, "苹果APP"),
    ANDROID(20, "安卓APP"),
    WECHAT_APP(30, "微信小程序"),
    WECHAT_H5(40, "微信H5"),
    OTHER(127, "其他");

    private Integer code;

    private String msg;

    PlatformEnum(Integer code, String msg) {
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
        for (PlatformEnum element : PlatformEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static PlatformEnum getByCode(Integer code) {
        for (PlatformEnum element : PlatformEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }

    public static Set<Integer> allowableValues() {
        Set<Integer> allowableValues = new HashSet<>(values().length);
        for (PlatformEnum platformEnum : values()) {
            allowableValues.add(platformEnum.getCode());
        }
        return allowableValues;
    }

}