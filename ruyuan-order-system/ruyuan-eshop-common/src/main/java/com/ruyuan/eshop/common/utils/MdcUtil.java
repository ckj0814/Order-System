package com.ruyuan.eshop.common.utils;

import com.ruyuan.eshop.common.constants.CoreConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * 对MDC进行一层封装
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
public class MdcUtil {

    /**
     * 用户自定义标识
     */
    private static final String USER_CUSTOMIZED_FLAG_KEY = "USER_CUSTOMIZED_FLAG";
    private static final String USER_CUSTOMIZED_FLAG_VALUE = "true";

    private MdcUtil() {
    }

    /**
     * 获取当前线程的traceId
     *
     * @return
     */
    public static String getTraceId() {
        return MDC.get(CoreConstant.TRACE_ID);
    }


    /**
     * 获取当前线程的traceId
     *
     * @return
     */
    public static String getUkId() {
        return String.valueOf(SnowFlake.generateId());
    }

    /**
     * 初始化当前线程的traceId
     *
     * @return
     */
    public static void initTraceId() {
        String id = String.valueOf(SnowFlake.generateId());
        MDC.put(CoreConstant.TRACE_ID, id);
    }

    /**
     * 初始化当前线程的traceId,根据父线程的traceId
     *
     * @return
     */
    public static void initTraceId(String parentTraceId) {
        String id = parentTraceId + SnowFlake.generateId();
        MDC.put(CoreConstant.TRACE_ID, id);
    }

    /**
     * 获取当前线程的traceId,如果没有，初始化
     *
     * @return
     */
    public static String getOrInitTraceId() {
        String id = MDC.get(CoreConstant.TRACE_ID);
        if (StringUtils.isBlank(id)) {
            id = String.valueOf(SnowFlake.generateId());
            MDC.put(CoreConstant.TRACE_ID, id);
        }
        return id;
    }

    /**
     * 设置当前线程的traceId
     *
     * @return
     */
    public static void setUserTraceId(String traceId) {
        if (StringUtils.isBlank(traceId)) {
            traceId = SnowFlake.generateIdStr();
        }
        MDC.put(CoreConstant.TRACE_ID, traceId);
        MDC.put(USER_CUSTOMIZED_FLAG_KEY, USER_CUSTOMIZED_FLAG_VALUE);
    }

    /**
     * 设置当前线程的traceId
     *
     * @return
     */
    public static Boolean isUserCustomized() {
        String value = MDC.get(USER_CUSTOMIZED_FLAG_KEY);
        if (StringUtils.isNotEmpty(value) && value.equals(USER_CUSTOMIZED_FLAG_VALUE)) {
            return true;
        }
        return false;
    }

    /**
     * 设置当前线程的traceId
     *
     * @return
     */
    public static void setTraceId(String traceId) {
        if (!StringUtils.isBlank(traceId)) {
            MDC.put(CoreConstant.TRACE_ID, traceId);
        } else {
            MDC.remove(CoreConstant.TRACE_ID);
        }
    }

    /**
     * 移除当前线程的traceId
     */
    public static void removeTraceId() {
        MDC.remove(CoreConstant.TRACE_ID);
    }

    /**
     * 清除
     */
    public static void clear() {
        MDC.clear();
    }
}
