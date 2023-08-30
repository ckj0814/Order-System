package com.ruyuan.eshop.pay.exception;

import com.ruyuan.eshop.common.exception.BaseBizException;
import com.ruyuan.eshop.common.exception.BaseErrorCodeEnum;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class PayBizException extends BaseBizException {

    public PayBizException(String errorMsg) {
        super(errorMsg);
    }

    public PayBizException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public PayBizException(BaseErrorCodeEnum baseErrorCodeEnum) {
        super(baseErrorCodeEnum);
    }

    public PayBizException(String errorCode, String errorMsg, Object... arguments) {
        super(errorCode, errorMsg, arguments);
    }

    public PayBizException(BaseErrorCodeEnum baseErrorCodeEnum, Object... arguments) {
        super(baseErrorCodeEnum, arguments);
    }
}