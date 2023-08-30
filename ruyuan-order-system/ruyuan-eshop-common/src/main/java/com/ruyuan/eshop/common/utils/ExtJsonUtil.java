package com.ruyuan.eshop.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
public class ExtJsonUtil {

    public static String mergeExtJson(String srcExtJson, String targetExtJson) {
        JSONObject srcJSON = parseExtJson(srcExtJson);
        JSONObject targetJSON = parseExtJson(targetExtJson);
        // 如果原值为空 直接返回新值
        if (srcJSON == null) {
            return targetExtJson;
        }
        // 如果新值为空 直接返回就值
        if (targetJSON == null) {
            return srcExtJson;
        }

        // 新的值覆盖原有的值
        srcJSON.putAll(targetJSON);

        return srcJSON.toJSONString();
    }

    public static String mergeField(String srcExtJson, String field, Object value) {
        // 如果没有新加的内容，就直接返回原值
        if (StringUtils.isBlank(field) || Objects.isNull(value)) {
            return srcExtJson;
        }
        JSONObject srcJSON = parseExtJson(srcExtJson);
        JSONObject targetJSON = new JSONObject();
        targetJSON.put(field, value);
        // 如果原值为空 直接返回新值
        if (srcJSON == null) {
            return targetJSON.toJSONString();
        }

        // 新的值覆盖原有的值
        srcJSON.putAll(targetJSON);

        return srcJSON.toJSONString();
    }

    public static JSONObject parseExtJson(String extJson) {
        try {
            return JSONObject.parseObject(extJson);
        } catch (Exception e) {
            log.error("parse extInfo error!!", e);
            return null;
        }
    }

    public static <T> T parseExtJson(String extJson, Class<T> clazz) {
        if (StringUtils.isEmpty(extJson)) {
            return null;
        }
        try {
            return JSONObject.parseObject(extJson, clazz);
        } catch (Exception e) {
            log.error("parse extInfo error!!", e);
            return null;
        }
    }


}
