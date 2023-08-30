package com.ruyuan.eshop.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * DO数据对象删除状态 0:未删除  1:已删除
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum DeleteStatusEnum {

    NO(0, "未删除"),
    YES(1, "已删除");

    private Integer code;

    private String msg;

    DeleteStatusEnum(Integer code, String msg) {
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
        for (DeleteStatusEnum element : DeleteStatusEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static DeleteStatusEnum getByCode(Integer code) {
        for (DeleteStatusEnum element : DeleteStatusEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }
}