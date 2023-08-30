package com.ruyuan.eshop.tms.exception;

import com.ruyuan.eshop.common.exception.BaseBizException;
import com.ruyuan.eshop.common.exception.BaseErrorCodeEnum;

/**
 * tms中心自定义业务异常类
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public class TmsBizException extends BaseBizException {

    public TmsBizException(String errorMsg) {
        super(errorMsg);
    }

    public TmsBizException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public TmsBizException(BaseErrorCodeEnum baseErrorCodeEnum) {
        super(baseErrorCodeEnum);
    }

    public TmsBizException(String errorCode, String errorMsg, Object... arguments) {
        super(errorCode, errorMsg, arguments);
    }

    public TmsBizException(BaseErrorCodeEnum baseErrorCodeEnum, Object... arguments) {
        super(baseErrorCodeEnum, arguments);
    }
}