package com.ruyuan.eshop.customer.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 客服审核退货申请入参
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class CustomerReviewReturnGoodsRequest implements Serializable {
    private static final long serialVersionUID = -4113897073742442896L;

    /**
     * 售后id
     */
    private Long afterSaleId;
    /**
     * 客服id
     */
    private String customerId;
    /**
     * 审核结果 1 审核通过  2 审核拒绝
     */
    private Integer auditResult;
    /**
     * 售后支付单id
     */
    private Long afterSaleRefundId;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 客服审核结果描述信息
     */
    private String auditResultDesc;

}