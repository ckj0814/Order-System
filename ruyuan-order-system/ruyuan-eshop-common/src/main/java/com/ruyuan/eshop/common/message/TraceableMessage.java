package com.ruyuan.eshop.common.message;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 可追溯的消息
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class TraceableMessage {
    /**
     * trace id
     */
    private String traceId;
    /**
     * 消息体对应的类
     */
    private Class<?> clazz;
    /**
     * 消息体
     */
    private String messageContent;

    public TraceableMessage(String traceId, Class<?> clazz, Object messageContent) {
        this.traceId = traceId;
        this.clazz = clazz;
        this.messageContent = JSONObject.toJSONString(messageContent);
    }
}
