package com.ruyuan.eshop.market.exception;

import com.ruyuan.eshop.common.exception.BaseErrorCodeEnum;

/**
 * 异常错误码枚举值
 * 前三位代表服务，后三位代表功能错误码
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public enum MarketErrorCodeEnum implements BaseErrorCodeEnum {

    USER_COUPON_IS_NULL("300001", "优惠券记录不存在"),
    USER_COUPON_IS_USED("300002", "优惠券记录已经被使用了"),
    USER_COUPON_CONFIG_IS_NULL("300003", "优惠券活动配置记录不存在"),
    SEND_MQ_FAILED("300004", "发送MQ消息失败"),
    CONSUME_MQ_FAILED("300005", "消费MQ消息失败"),
    RELEASE_COUPON_FAILED("300006", "释放优惠券失败"),
    ;

    private String errorCode;

    private String errorMsg;

    MarketErrorCodeEnum(String errorCode, String errorMsg) {
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