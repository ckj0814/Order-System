package com.ruyuan.eshop.wms.exception;

import com.ruyuan.eshop.common.exception.BaseBizException;
import com.ruyuan.eshop.common.exception.BaseErrorCodeEnum;

/**
 * wms中心自定义业务异常类
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public class WmsBizException extends BaseBizException {

    public WmsBizException(String errorMsg) {
        super(errorMsg);
    }

    public WmsBizException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public WmsBizException(BaseErrorCodeEnum baseErrorCodeEnum) {
        super(baseErrorCodeEnum);
    }

    public WmsBizException(String errorCode, String errorMsg, Object... arguments) {
        super(errorCode, errorMsg, arguments);
    }

    public WmsBizException(BaseErrorCodeEnum baseErrorCodeEnum, Object... arguments) {
        super(baseErrorCodeEnum, arguments);
    }
}