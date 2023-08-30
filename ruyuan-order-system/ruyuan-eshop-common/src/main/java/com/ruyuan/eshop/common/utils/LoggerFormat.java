package com.ruyuan.eshop.common.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 日志格式器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public class LoggerFormat {
    private final Map<String, String> logInfo = new LinkedHashMap<>();
    private static final String USER_TRACE_ID = "TraceId";

    private LoggerFormat() {
    }

    public static LoggerFormat build() {
        return new LoggerFormat();
    }

    /**
     * 日志备注
     *
     * @param remark
     * @return
     */
    public LoggerFormat remark(String remark) {
        logInfo.put("remark", remark);
        return this;
    }

    /**
     * 日志数据
     *
     * @param key
     * @param value
     * @return
     */
    public LoggerFormat data(String key, String value) {
        logInfo.put(key, value);
        return this;
    }

    public LoggerFormat data(String key, Object value) {
        logInfo.put(key, JSONObject.toJSONString(value));
        return this;
    }

    public String finish() {
        StringBuilder sb = new StringBuilder();
        if (!logInfo.isEmpty()) {
            Set<String> keys = logInfo.keySet();
            if (MdcUtil.isUserCustomized()) {
                sb.append(USER_TRACE_ID).append("=[").append(MdcUtil.getTraceId()).append("] ");
            }
            for (String key : keys) {
                sb.append(key).append("=[").append(logInfo.get(key)).append("] ");
            }
        }
        return sb.toString();
    }
}