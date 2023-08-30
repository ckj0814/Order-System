package com.ruyuan.eshop.common.constants;

/**
 * <p>
 * redis 分布式锁key
 * </p>
 *
 * @author zhonghuashishan
 */
public class RedisLockKeyConstants {

    /**
     * 订单支付
     */
    public static final String ORDER_PAY_KEY = "#ORDER_PAY_KEY:";

    public static final String ORDER_FULFILL_KEY = "#ORDER_FULFILL_KEY:";

    public static final String ORDER_WMS_RESULT_KEY = "#ORDER_WMS_RESULT_KEY:";

    public static final String REFUND_KEY = "#REFUND_KEY:";

    public static final String CANCEL_KEY = "#CANCEL_KEY:";

    /**
     * 缺品请求锁
     */
    public static final String LACK_REQUEST_KEY = "#LACK_REQUEST_KEY:";

    /**
     * 新增商品库存锁
     */
    public static final String ADD_PRODUCT_STOCK_KEY = "#ADD_PRODUCT_STOCK_KEY:";

    /**
     * 订单扣减商品库存锁
     */
    public static final String ORDER_DEDUCT_PRODUCT_STOCK_KEY = "#ORDER_DEDUCT_PRODUCT_STOCK_KEY:{0}:{1}";

    /**
     * 扣减商品库存锁（保证mysql+redis库存扣减的原子性）
     */
    public static final String DEDUCT_PRODUCT_STOCK_KEY = "#ORDER_DEDUCT_PRODUCT_STOCK_KEY:";


    /**
     * 释放商品库存锁（保证mysql+redis库存释放的原子性）
     */
    public static final String RELEASE_PRODUCT_STOCK_KEY = "#RELEASE_PRODUCT_STOCK_KEY:";

    /**
     * 调整库存存锁
     */
    public static final String MODIFY_PRODUCT_STOCK_KEY = "#MODIFY_PRODUCT_STOCK_KEY:";

    /**
     * 释放优惠券锁
     */
    public static final String RELEASE_COUPON_KEY = "#RELEASE_COUPON_KEY:";
}
