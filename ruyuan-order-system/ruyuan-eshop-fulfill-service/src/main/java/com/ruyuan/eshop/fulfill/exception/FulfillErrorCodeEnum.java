package com.ruyuan.eshop.fulfill.exception;

import com.ruyuan.eshop.common.exception.BaseErrorCodeEnum;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public enum FulfillErrorCodeEnum implements BaseErrorCodeEnum {


    FULFILL_ID_GEN_ERROR("107001", "履约单ID生成异常"),
    WMS_IS_ERROR("107002", "调用WMS系统异常"),
    TMS_IS_ERROR("107003", "调用TMS系统异常"),
    ORDER_FULFILL_IS_ERROR("107004", "履约流程执行异常"),
    SEND_MQ_FAILED("107005", "发送MQ消息失败"),
    ORDER_FULFILL_ERROR("107006", "订单履约错误"),
    ;

    private String errorCode;

    private String errorMsg;

    FulfillErrorCodeEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}