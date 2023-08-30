package com.ruyuan.eshop.product.exception;

import com.ruyuan.eshop.common.exception.BaseErrorCodeEnum;

/**
 * 异常错误码枚举值
 * 前三位代表服务，后三位代表功能错误码，比如200表示商品服务 001表示查询商品参数错误
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum ProductErrorCodeEnum implements BaseErrorCodeEnum {

    SKU_CODE_IS_NULL("200001", "sku编号不能为空"),
    ;

    private String errorCode;

    private String errorMsg;

    ProductErrorCodeEnum(String errorCode, String errorMsg) {
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