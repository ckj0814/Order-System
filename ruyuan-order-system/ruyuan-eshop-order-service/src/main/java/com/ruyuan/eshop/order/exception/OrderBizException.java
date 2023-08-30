package com.ruyuan.eshop.order.exception;

import com.ruyuan.eshop.common.exception.BaseBizException;
import com.ruyuan.eshop.common.exception.BaseErrorCodeEnum;

/**
 * 订单中心自定义业务异常类
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public class OrderBizException extends BaseBizException {

    public OrderBizException(String errorMsg) {
        super(errorMsg);
    }

    public OrderBizException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public OrderBizException(BaseErrorCodeEnum baseErrorCodeEnum) {
        super(baseErrorCodeEnum);
    }

    public OrderBizException(String errorCode, String errorMsg, Object... arguments) {
        super(errorCode, errorMsg, arguments);
    }

    public OrderBizException(BaseErrorCodeEnum baseErrorCodeEnum, Object... arguments) {
        super(baseErrorCodeEnum, arguments);
    }
}