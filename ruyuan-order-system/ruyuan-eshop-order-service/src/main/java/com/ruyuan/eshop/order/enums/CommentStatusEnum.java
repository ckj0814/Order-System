package com.ruyuan.eshop.order.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 发表评论状态枚举
 *
 * @author zhonghuashishan
 */
public enum CommentStatusEnum {

    NO(0, "未删除"),
    YES(1, "已删除");

    private Integer code;

    private String msg;

    CommentStatusEnum(Integer code, String msg) {
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
        for (CommentStatusEnum element : CommentStatusEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }
        return map;
    }

    public static CommentStatusEnum getByCode(Integer code) {
        for (CommentStatusEnum element : CommentStatusEnum.values()) {
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }
}