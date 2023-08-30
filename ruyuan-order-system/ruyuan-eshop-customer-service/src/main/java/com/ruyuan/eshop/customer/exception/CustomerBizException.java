package com.ruyuan.eshop.customer.exception;

import com.ruyuan.eshop.common.exception.BaseBizException;
import com.ruyuan.eshop.common.exception.BaseErrorCodeEnum;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class CustomerBizException extends BaseBizException {

    public CustomerBizException(String errorMsg) {
        super(errorMsg);
    }

    public CustomerBizException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public CustomerBizException(BaseErrorCodeEnum baseErrorCodeEnum) {
        super(baseErrorCodeEnum);
    }

    public CustomerBizException(String errorCode, String errorMsg, Object... arguments) {
        super(errorCode, errorMsg, arguments);
    }

    public CustomerBizException(BaseErrorCodeEnum baseErrorCodeEnum, Object... arguments) {
        super(baseErrorCodeEnum, arguments);
    }
}