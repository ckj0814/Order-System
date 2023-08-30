package com.ruyuan.eshop.wms.exception;

import com.ruyuan.eshop.common.exception.BaseErrorCodeEnum;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public enum WmsErrorCodeEnum implements BaseErrorCodeEnum {


    DELIVERY_ORDER_ID_GEN_ERROR("108001", "出库单ID生成异常"),
    TMS_IS_ERROR("108002", "tms系统异常"),
    ;

    private String errorCode;

    private String errorMsg;

    WmsErrorCodeEnum(String errorCode, String errorMsg) {
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