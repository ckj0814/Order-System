package com.ruyuan.eshop.market.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class CalculateOrderAmountDTO implements Serializable {

    private static final long serialVersionUID = -6463448609836937620L;

    /**
     * 订单费用信息
     */
    private List<OrderAmountDTO> orderAmountList;

    /**
     * 订单条目费用信息
     */
    private List<OrderAmountDetailDTO> orderAmountDetail;

    /**
     * 营销计算出来的费用信息
     */
    @Data
    public static class OrderAmountDTO implements Serializable {

        private static final long serialVersionUID = 1643927513628038346L;

        /**
         * 订单编号
         */
        private String orderId;

        /**
         * 收费类型
         */
        private Integer amountType;

        /**
         * 收费金额
         */
        private Integer amount;

    }

    /**
     * 营销计算出来的订单条目费用信息
     */
    @Data
    public static class OrderAmountDetailDTO implements Serializable {

        private static final long serialVersionUID = 2191438807902362511L;

        /**
         * 订单编号
         */
        private String orderId;

        /**
         * 产品类型
         */
        private Integer productType;

        /**
         * sku编码
         */
        private String skuCode;

        /**
         * 销售数量
         */
        private Integer saleQuantity;

        /**
         * 销售单价
         */
        private Integer salePrice;

        /**
         * 收费类型
         */
        private Integer amountType;

        /**
         * 收费金额
         */
        private Integer amount;

    }

}