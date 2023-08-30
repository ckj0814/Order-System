package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 售后支付表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class AfterSalePayDTO implements Serializable {

    /**
     * 售后单号
     */
    private String afterSaleId;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 售后批次编号
     */
    private String afterSaleBatchNo;

    /**
     * 账户类型
     */
    private Integer accountType;

    /**
     * 支付类型
     */
    private Integer payType;

    /**
     * 售后单状态
     */
    private Integer afterSaleStatus;

    /**
     * 退款金额
     */
    private Integer refundAmount;

    /**
     * 退款支付时间
     */
    private Date refundPayTime;

    /**
     * 交易单号
     */
    private String outTradeNo;

    /**
     * 备注
     */
    private String remark;
}
