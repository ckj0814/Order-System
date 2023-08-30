package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单支付明细DTO
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class OrderPaymentDetailDTO implements Serializable {
    private static final long serialVersionUID = 4890592762126631890L;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 账户类型
     */
    private Integer accountType;

    /**
     * 支付类型  10:微信支付, 20:支付宝支付
     */
    private Integer payType;

    /**
     * 支付状态 10:未支付,20:已支付
     */
    private Integer payStatus;

    /**
     * 支付金额
     */
    private Integer payAmount;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付流水号
     */
    private String outTradeNo;

    /**
     * 支付备注信息
     */
    private String payRemark;
}
