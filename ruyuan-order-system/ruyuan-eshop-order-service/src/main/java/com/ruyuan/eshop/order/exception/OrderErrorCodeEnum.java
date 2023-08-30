package com.ruyuan.eshop.order.exception;

import com.ruyuan.eshop.common.exception.BaseErrorCodeEnum;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public enum OrderErrorCodeEnum implements BaseErrorCodeEnum {

    /**
     * 正向订单错误码105开头 todo
     */
    USER_ID_IS_NULL("105001", "用户ID不能为空"),
    ORDER_NO_TYPE_ERROR("105002", "订单号类型错误"),
    CREATE_ORDER_REQUEST_ERROR("105003", "生单请求参数错误"),
    ORDER_ID_IS_NULL("105004", "订单号不能为空"),
    ORDER_EXISTED("105004", "订单已存在"),
    BUSINESS_IDENTIFIER_IS_NULL("105005", "业务线标识不能为空"),
    BUSINESS_IDENTIFIER_ERROR("105006", "业务线标识错误"),
    ORDER_TYPE_IS_NULL("105007", "订单类型不能为空"),
    ORDER_TYPE_ERROR("105008", "订单类型错误"),
    SELLER_ID_IS_NULL("105009", "卖家ID不能为空"),
    USER_ADDRESS_ERROR("105010", "地址信息错误"),
    DELIVERY_TYPE_IS_NULL("105011", "配送类型不能为空"),
    USER_LOCATION_IS_NULL("105011", "地理位置不能为空"),
    ORDER_RECEIVER_IS_NULL("105012", "收货人不能为空"),
    ORDER_CHANNEL_IS_NULL("105013", "下单渠道信息不能为空"),
    CLIENT_IP_IS_NULL("105014", "客户端IP不能为空"),
    ORDER_ITEM_IS_NULL("105015", "订单商品信息不能为空"),
    ORDER_ITEM_PARAM_ERROR("105016", "订单商品信息错误"),
    ORDER_AMOUNT_IS_NULL("105017", "订单费用信息不能为空"),
    ORDER_AMOUNT_TYPE_IS_NULL("105018", "订单费用类型不能为空"),
    ORDER_AMOUNT_TYPE_PARAM_ERROR("105019", "订单费用类型错误"),
    ORDER_ORIGIN_PAY_AMOUNT_IS_NULL("105020", "订单支付原价不能为空"),
    ORDER_SHIPPING_AMOUNT_IS_NULL("105021", "订单运费不能为空"),
    ORDER_REAL_PAY_AMOUNT_IS_NULL("105022", "订单实付金额不能为空"),
    ORDER_DISCOUNT_AMOUNT_IS_NULL("105023", "订单优惠券抵扣金额不能为空"),
    USER_COUPON_IS_USED("105023", "优惠券记录已经被使用了"),
    ORDER_PAYMENT_IS_NULL("105024", "订单支付信息不能为空"),
    PAY_TYPE_PARAM_ERROR("105025", "支付类型错误"),
    ACCOUNT_TYPE_PARAM_ERROR("105026", "账户类型错误"),
    PRODUCT_SKU_CODE_ERROR("105027", "商品{0}不存在"),
    CALCULATE_ORDER_AMOUNT_ERROR("105028", "计算订单价格失败"),
    ORDER_CHECK_REAL_PAY_AMOUNT_FAIL("105029", "订单验价失败"),
    ORDER_NOT_ALLOW_INFORM_WMS_RESULT("105029", "订单不允许通知物流配送结果"),
    ORDER_NOT_ALLOW_TO_DELIVERY("105030", "订单不允许配送"),
    ORDER_NOT_ALLOW_TO_SIGN("105031", "订单不允许签收"),
    SKU_CODE_IS_NULL("105032", "skuCode 不能为空"),
    AFTER_SALE_ID_IS_NULL("105033", "售后ID不能为空"),
    LACK_ITEM_IS_NULL("105034", "缺品项不能为空"),
    LACK_NUM_IS_LT_0("105035", "缺品数量不能小于0"),
    ORDER_NOT_FOUND("105036", "查无此订单"),
    LACK_ITEM_NOT_IN_ORDER("105037", "缺品商品并未下单"),
    LACK_NUM_IS_GE_SKU_ORDER_ITEM_SIZE("105038", "缺品数量不能大于或等于下单商品数量"),
    ORDER_NOT_ALLOW_TO_LACK("105039", "订单不允许发起缺品"),
    REGION_ID_IS_NULL("105040", "区域ID不能为空"),
    ORDER_PAY_AMOUNT_ERROR("105041", "订单支付金额错误"),
    ORDER_PRE_PAY_ERROR("105042", "订单支付发生错误"),
    ORDER_PRE_PAY_EXPIRE_ERROR("105042", "已经超过支付订单的截止时间"),
    ORDER_PAY_CALLBACK_ERROR("105043", "订单支付回调发生错误"),
    ORDER_PAY_CALLBACK_SEND_MQ_ERROR("105043", "订单支付完成发送消息失败"),
    ORDER_INFO_IS_NULL("105044", "订单信息不存在"),
    ORDER_CALLBACK_PAY_AMOUNT_ERROR("105045", "订单支付金额错误"),
    ORDER_CANCEL_PAY_CALLBACK_ERROR("105046", "接收到支付回调时，订单已经取消"),
    ORDER_CANCEL_PAY_CALLBACK_PAY_TYPE_SAME_ERROR("105047", "接收到支付回调的时候订单已经取消，且支付回调为同种支付方式"),
    ORDER_CANCEL_PAY_CALLBACK_PAY_TYPE_NO_SAME_ERROR("105047", "接收到支付回调的时候订单已经取消，且支付回调非同种支付方式"),
    ORDER_CANCEL_PAY_CALLBACK_REPEAT_ERROR("105048", "不同支付方式下的重复支付回调"),
    ORDER_CANNOT_REMOVE("105046", "订单不允许删除"),
    ORDER_NOT_ALLOW_TO_ADJUST_ADDRESS("105047", "订单不允许调整配送地址"),
    ORDER_DELIVERY_NOT_FOUND("105048", "订单配送记录不存在"),
    ORDER_DELIVERY_ADDRESS_HAS_BEEN_ADJUSTED("105049", "订单配送地址已被调整过"),
    ORDER_FULFILL_ERROR("105050", "订单履约失败"),
    AFTER_SALE_NOT_FOUND("105051", "查无此售后单"),
    AFTER_SALE_CANNOT_REVOKE("105052", "售后单无法撤销"),
    AFTER_SALE_REFUND_ID_IS_NULL("105054", "售后支付ID不能为空"),
    ORDER_STATUS_ERROR("105049", "订单状态异常"),
    ORDER_PAY_STATUS_IS_PAID("105050", "订单已经是已完成支付状态"),
    SEND_AFTER_SALE_CUSTOMER_AUDIT_MQ_FAILED("105055", "发送客服审核售后事务消息失败"),


    RETURN_GOODS_REQUEST_IS_NULL("105051", "手动退货入参不能为空"),
    AFTER_SALE_TIME_IS_NULL("105052", "申请售后时间不能为空"),
    ORDER_CURRENT_TYPE_IS_NULL("105053", "当前订单状态不能为空"),
    SKU_IS_NULL("105054", "sku列表不能为空"),
    RETURN_GOODS_NUM_IS_NULL("105055", "退货数量不能为空"),
    RETURN_GOODS_CODE_IS_NULL("105056", "退货原因选项不能为空"),
    AFTER_SALE_TYPE_IS_NULL("105057", "售后类型不能为空"),
    ORDER_STATUS_CHANGED("105058", "当前订单状态已变更，请重新刷新"),
    ORDER_STATUS_CANCELED("105058", "当前订单已取消"),
    ORDER_STATUS_IS_NULL("105059", "当前订单状态不能为空"),
    ORDER_PAY_TRADE_NO("105060", "当前订单已产生支付流水号，不能取消"),
    CANCEL_REQUEST_IS_NULL("105061", "取消订单入参不能为空"),
    CANCEL_TYPE_IS_NULL("105062", "取消订单类型不能为空"),
    CANCEL_ORDER_ID_IS_NULL("105063", "取消订单ID不能为空"),
    CANCEL_USER_ID_IS_NULL("105064", "取消订单用户ID不能为空"),
    CANCEL_ORDER_REPEAT("105065", "取消订单重复"),
    CANCEL_ORDER_FULFILL_ERROR("105066", "调用履约系统失败"),
    PROCESS_REFUND_REPEAT("105067", "处理退款重复"),
    CALCULATING_REFUND_AMOUNT_FAILED("105071", "取消订单用户ID不能为空"),
    PROCESS_REFUND_FAILED("105072", "退款前准备工作失败"),
    SEND_MQ_FAILED("105073", "发送MQ消息失败"),
    CONSUME_MQ_FAILED("105074", "消费MQ消息失败"),
    ORDER_REFUND_AMOUNT_FAILED("105075", "调用支付退款接口失败"),
    PROCESS_PAY_REFUND_CALLBACK_REPEAT("105076", "处理支付退款回调重复"),
    PROCESS_PAY_REFUND_CALLBACK_FAILED("105077", "处理支付退款回调失败"),
    PROCESS_PAY_REFUND_CALLBACK_BATCH_NO_IS_NULL("105078", "处理支付退款回调批次号不能为空"),
    PROCESS_PAY_REFUND_CALLBACK_STATUS_NO_IS_NUL("105079", "处理支付退款回调退款状态不能为空"),
    PROCESS_PAY_REFUND_CALLBACK_FEE_NO_IS_NUL("105080", "处理支付退款回调退款金额不能为空"),
    PROCESS_PAY_REFUND_CALLBACK_TOTAL_FEE_NO_IS_NUL("105081", "处理支付退款回调退款总额不能为空"),
    PROCESS_PAY_REFUND_CALLBACK_SIGN_NO_IS_NUL("105082", "处理支付退款回调签名不能为空"),
    PROCESS_PAY_REFUND_CALLBACK_TRADE_NO_IS_NUL("105083", "处理支付退款回调交易号不能为空"),
    PROCESS_AFTER_SALE_RETURN_GOODS("105084", "处理售后退款重复"),
    PROCESS_PAY_REFUND_CALLBACK_AFTER_SALE_ID_IS_NULL("105085", "处理支付退款回调售后订单号不能为空"),
    PROCESS_PAY_REFUND_CALLBACK_AFTER_SALE_REFUND_TIME_IS_NULL("105086", "处理支付退款回调售后退款时间不能为空"),
    REPEAT_CALLBACK("105087", "重复的退款回调"),
    REPEAT_CALL("105088", "当前接口被重复调用"),
    REFUND_MONEY_REPEAT("105089", "执行退款操作重复"),
    ORDER_CANNOT_REPEAT_OPERATE("105090", "当前订单不能重复操作"),
    PROCESS_APPLY_AFTER_SALE_CANNOT_REPEAT("105091", "不能重复发起售后"),
    CUSTOMER_AUDIT_CANNOT_REPEAT("105092", "不能重复处理客服审核信息"),
    AFTER_SALE_ITEM_CANNOT_NULL("105093", "售后商品信息不能为空"),
    CUSTOMER_AUDIT_RESULT_IS_NULL("105094", "处理客服审核结果不能为空"),
    INSERT_ORDER_TRANSACTION_MQ_ERROR("105095", "生成订单失败"),
    CURRENT_ORDER_STATUS_CANNOT_CANCEL("105096", "当前订单状态不允许取消"),
    SEND_TRANSACTION_MQ_FAILED("105097", "发送MQ事务消息失败"),
    UPDATE_ORDER_INFO_AND_SAVE_ORDER_LOG_FAILED("105098", "更新订单信息和保存订单操作日志失败"),
    CANCEL_ORDER_PROCESS_FAILED("105099", "取消订单过程执行失败"),
    REFUND_MONEY_RELEASE_COUPON_FAILED("105100", "实际退款释放优惠券失败"),
    SEND_AUDIT_PASS_RELEASE_ASSETS_FAILED("105101", "发送审核通过释放资产topic失败"),

    /**
     * 通用异常
     */
    COLLECTION_PARAM_CANNOT_BEYOND_MAX_SIZE("108001", "[{0}]大小不能超过{1}"),
    ENUM_PARAM_MUST_BE_IN_ALLOWABLE_VALUE("108002", "[{0}]的取值必须为{1}"),
    DELIVERY_TYPE_ERROR("105080", "配送类型错误"),
    ;

    private String errorCode;

    private String errorMsg;

    OrderErrorCodeEnum(String errorCode, String errorMsg) {
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