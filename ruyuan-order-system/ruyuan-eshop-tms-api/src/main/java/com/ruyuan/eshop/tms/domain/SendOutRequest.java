package com.ruyuan.eshop.tms.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 发货请求
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@Builder
public class SendOutRequest implements Serializable {

    /**
     * 接入方业务线标识  1, "自营商城"
     */
    private Integer businessIdentifier;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 商家id
     */
    private String sellerId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 配送类型，默认是自配送
     */
    private Integer deliveryType;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 省
     */
    private String receiverProvince;

    /**
     * 市
     */
    private String receiverCity;

    /**
     * 区
     */
    private String receiverArea;

    /**
     * 街道地址
     */
    private String receiverStreet;

    /**
     * 详细地址
     */
    private String receiverDetailAddress;

    /**
     * 经度 六位小数点
     */
    private BigDecimal receiverLon;

    /**
     * 纬度 六位小数点
     */
    private BigDecimal receiverLat;

    /**
     * 用户备注
     */
    private String userRemark;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 付款总金额
     */
    private Integer payAmount;

    /**
     * 交易总金额
     */
    private Integer totalAmount;

    /**
     * 运费
     */
    private Integer deliveryAmount;

    /**
     * 订单商品明细
     */
    private List<OrderItemRequest> orderItems;

    /**
     * 用于模拟tms服务异常
     */
    private String tmsException;

    @Tolerate
    public SendOutRequest() {
    }

    /**
     * <p>
     * 订单商品明细请求
     * </p>
     *
     * @author zhonghuashishan
     */
    @Data
    @Builder
    public static class OrderItemRequest implements Serializable {

        /**
         * 商品id
         */
        private String skuCode;

        /**
         * 商品名称
         */
        private String productName;

        /**
         * 销售单价
         */
        private Integer salePrice;

        /**
         * 销售数量
         */
        private Integer saleQuantity;

        /**
         * 商品单位
         */
        private String productUnit;

        /**
         * 付款金额
         */
        private Integer payAmount;

        /**
         * 当前商品支付原总价
         */
        private Integer originAmount;

        @Tolerate
        public OrderItemRequest() {

        }

    }

}
