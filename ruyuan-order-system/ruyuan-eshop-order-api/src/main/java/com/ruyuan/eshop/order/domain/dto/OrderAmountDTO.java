package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 订单费用DTO
 * </p>
 *
 * @author zhonghuashishan
 * @since 2021-11-23
 */
@Data
public class OrderAmountDTO implements Serializable {

    private static final long serialVersionUID = -5562281872061469918L;
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
