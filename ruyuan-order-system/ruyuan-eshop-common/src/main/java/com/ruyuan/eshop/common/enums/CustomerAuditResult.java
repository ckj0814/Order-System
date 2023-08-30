package com.ruyuan.eshop.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public enum CustomerAuditResult {
    ACCEPT(1, "客服审核通过"),
    REJECT(2, "客服审核拒绝");
    private Integer code;

    private String msg;

    CustomerAuditResult(Integer code, String msg) {
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
