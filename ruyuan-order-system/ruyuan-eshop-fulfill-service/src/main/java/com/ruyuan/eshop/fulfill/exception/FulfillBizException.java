package com.ruyuan.eshop.fulfill.exception;

import com.ruyuan.eshop.common.exception.BaseBizException;
import com.ruyuan.eshop.common.exception.BaseErrorCodeEnum;

/**
 * 履约中心自定义业务异常类
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public class FulfillBizException extends BaseBizException {

    public FulfillBizException(String errorMsg) {
        super(errorMsg);
    }

    public FulfillBizException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public FulfillBizException(BaseErrorCodeEnum baseErrorCodeEnum) {
        super(baseErrorCodeEnum);
    }

    public FulfillBizException(String errorCode, String errorMsg, Object... arguments) {
        super(errorCode, errorMsg, arguments);
    }

    public FulfillBizException(BaseErrorCodeEnum baseErrorCodeEnum, Object... arguments) {
        super(baseErrorCodeEnum, arguments);
    }
}