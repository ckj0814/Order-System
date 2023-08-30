package com.ruyuan.eshop.order.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 创建订单请求入参
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class CreateOrderRequest implements Serializable {

    private static final long serialVersionUID = -3719117561480569064L;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 业务线标识
     */
    private Integer businessIdentifier;

    // 用户信息
    /**
     * 微信openid
     */
    private String openid;

    /**
     * 用户ID
     */
    private String userId;

    // 订单主体信息
    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 卖家ID
     */
    private String sellerId;

    /**
     * 用户备注
     */
    private String userRemark;

    /**
     * 优惠券ID
     */
    private String couponId;

    // 收货地址信息

    /**
     * 提货方式
     */
    private Integer deliveryType;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 街道
     */
    private String street;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 经度
     */
    private BigDecimal lon;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 用户收货地址id
     */
    private String userAddressId;

    /**
     * 地区编码
     */
    private String addressCode;

    /**
     * 区域ID
     */
    private String regionId;

    /**
     * 配送区域ID
     */
    private String shippingAreaId;

    // 下单客户端信息，主要用于风控检查

    /**
     * 客户端ip
     */
    private String clientIp;

    /**
     * 设备编号
     */
    private String deviceId;

    /**
     * 订单商品信息
     */
    private List<OrderItemRequest> orderItemRequestList;


    /**
     * 订单费用信息
     */
    private List<OrderAmountRequest> orderAmountRequestList;

    /**
     * 支付信息
     */
    private List<PaymentRequest> paymentRequestList;

    /**
     * 订单条目信息
     */
    @Data
    public static class OrderItemRequest implements Serializable {

        private static final long serialVersionUID = 8267460170612816097L;

        /**
         * 商品类型
         */
        private Integer productType;

        /**
         * 销售数量
         */
        private Integer saleQuantity;

        /**
         * sku编号
         */
        private String skuCode;
    }

    /**
     * 订单费用信息
     */
    @Data
    public static class OrderAmountRequest implements Serializable {

        private static final long serialVersionUID = -8189987703740512851L;

        /**
         * 费用类型
         */
        private Integer amountType;

        /**
         * 费用金额
         */
        private Integer amount;
    }

    /**
     * 订单支付信息
     */
    @Data
    public static class PaymentRequest implements Serializable {


        private static final long serialVersionUID = -1821079125013490176L;

        /**
         * 支付类型
         */
        private Integer payType;

        /**
         * 账户类型
         */
        private Integer accountType;

    }
}