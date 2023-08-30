package com.ruyuan.eshop.product.exception;

import com.ruyuan.eshop.common.exception.BaseBizException;
import com.ruyuan.eshop.common.exception.BaseErrorCodeEnum;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class ProductBizException extends BaseBizException {

    public ProductBizException(String errorMsg) {
        super(errorMsg);
    }

    public ProductBizException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public ProductBizException(BaseErrorCodeEnum baseErrorCodeEnum) {
        super(baseErrorCodeEnum);
    }

    public ProductBizException(String errorCode, String errorMsg, Object... arguments) {
        super(errorCode, errorMsg, arguments);
    }

    public ProductBizException(BaseErrorCodeEnum baseErrorCodeEnum, Object... arguments) {
        super(baseErrorCodeEnum, arguments);
    }
}