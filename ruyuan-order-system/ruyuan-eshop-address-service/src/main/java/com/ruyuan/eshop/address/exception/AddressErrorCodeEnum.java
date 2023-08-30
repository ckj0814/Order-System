package com.ruyuan.eshop.address.exception;

import com.ruyuan.eshop.common.exception.BaseErrorCodeEnum;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public enum AddressErrorCodeEnum implements BaseErrorCodeEnum {


    PARAM_CANNOT_ALL_EMPTY("150001", "查询入参不能全部为空"),
    ;

    private String errorCode;
    private String errorMsg;

    AddressErrorCodeEnum(String errorCode, String errorMsg) {
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
