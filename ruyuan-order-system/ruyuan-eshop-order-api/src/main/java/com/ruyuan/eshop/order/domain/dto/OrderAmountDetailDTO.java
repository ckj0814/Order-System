package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 订单费用明细表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class OrderAmountDetailDTO implements Serializable {

    private static final long serialVersionUID = 5812183258654799741L;

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
