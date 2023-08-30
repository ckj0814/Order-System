package com.ruyuan.eshop.common.json;

import com.alibaba.fastjson.JSONObject;

/**
 * json字段值提取器
 *
 * @author zhonghuashishan
 */
public class JsonExtractor {

    /**
     * 获取表达式对应的值
     *
     * @param targetJson        目标的json对象
     * @param jsonExtractSyntax 表达式
     * @return 值
     */
    public String getString(JSONObject targetJson, String jsonExtractSyntax) {
        return String.valueOf(get(targetJson, jsonExtractSyntax));
    }

    /**
     * 获取表达式对应的值
     *
     * @param targetJson        目标的json对象
     * @param jsonExtractSyntax 表达式
     * @return 值
     */
    public Double getDouble(JSONObject targetJson, String jsonExtractSyntax) {
        return Double.valueOf(String.valueOf(get(targetJson, jsonExtractSyntax)));
    }

    /**
     * 获取表达式对应的值
     *
     * @param targetJson        目标的json对象
     * @param jsonExtractSyntax 表达式
     * @return 值
     */
    public Integer getInteger(JSONObject targetJson, String jsonExtractSyntax) {
        return Integer.valueOf(String.valueOf(get(targetJson, jsonExtractSyntax)));
    }

    /**
     * 获取表达式对应的值
     *
     * @param targetJson        目标的json对象
     * @param jsonExtractSyntax 表达式
     * @return 值
     */
    public Long getLong(JSONObject targetJson, String jsonExtractSyntax) {
        return Long.valueOf(String.valueOf(get(targetJson, jsonExtractSyntax)));
    }

    /**
     * 获取表达式对应的值
     *
     * @param targetJson        目标的json对象
     * @param jsonExtractSyntax 表达式
     * @return 值
     */
    public Object get(JSONObject targetJson, String jsonExtractSyntax) {
        JsonExpression jsonExpressionTree = JsonExtractSyntaxParser.parse(jsonExtractSyntax);
        return jsonExpressionTree.interpret(new JsonExpressionContext(targetJson));
    }

}
