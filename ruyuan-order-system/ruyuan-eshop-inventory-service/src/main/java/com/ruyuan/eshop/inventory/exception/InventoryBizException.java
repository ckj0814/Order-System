package com.ruyuan.eshop.inventory.exception;

import com.ruyuan.eshop.common.exception.BaseBizException;
import com.ruyuan.eshop.common.exception.BaseErrorCodeEnum;

/**
 * 库存服务自定义业务异常
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public class InventoryBizException extends BaseBizException {

    public InventoryBizException(String errorMsg) {
        super(errorMsg);
    }

    public InventoryBizException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public InventoryBizException(BaseErrorCodeEnum baseErrorCodeEnum) {
        super(baseErrorCodeEnum);
    }

    public InventoryBizException(String errorCode, String errorMsg, Object... arguments) {
        super(errorCode, errorMsg, arguments);
    }

    public InventoryBizException(BaseErrorCodeEnum baseErrorCodeEnum, Object... arguments) {
        super(baseErrorCodeEnum, arguments);
    }
}