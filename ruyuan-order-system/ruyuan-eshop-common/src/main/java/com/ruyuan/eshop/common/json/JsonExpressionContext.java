package com.ruyuan.eshop.common.json;

import com.alibaba.fastjson.JSONObject;

/**
 * json表达式上下文
 *
 * @author zhonghuashishan
 */
public class JsonExpressionContext {

    /**
     * 目标json
     */
    private JSONObject targetJson;
    /**
     * 当前节点
     */
    private JSONObject currentJsonNode;

    public JsonExpressionContext(JSONObject targetJson) {
        this.targetJson = targetJson;
    }

    public JSONObject getCurrentJsonNode() {
        return currentJsonNode;
    }

    public void setCurrentJsonNode(JSONObject currentJsonNode) {
        this.currentJsonNode = currentJsonNode;
    }

    public JSONObject getTargetJson() {
        return targetJson;
    }

}
