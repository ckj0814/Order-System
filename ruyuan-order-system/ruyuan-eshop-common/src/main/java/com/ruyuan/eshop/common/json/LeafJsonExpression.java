package com.ruyuan.eshop.common.json;

import com.alibaba.fastjson.JSONObject;

/**
 * 叶子表达式
 *
 * @author zhonghuashishan
 */
public class LeafJsonExpression implements JsonExpression {

    private String jsonNodeName;

    public LeafJsonExpression(String jsonNodeName) {
        this.jsonNodeName = jsonNodeName;
    }

    @Override
    public Object interpret(JsonExpressionContext context) {
        JSONObject currentJsonNode = context.getCurrentJsonNode();
        if (currentJsonNode == null) {
            return context.getTargetJson().get(jsonNodeName);
        } else {
            return currentJsonNode.get(jsonNodeName);
        }
    }

}
