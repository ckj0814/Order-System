package com.ruyuan.eshop.order.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class CustomerAuditAssembleRequest implements Serializable {
    /**
     * 售后id
     */
    private Long afterSaleId;
    /**
     * 订单id
     */
    private String orderId;

    /**
     * 售后支付单id
     */
    private Long afterSaleRefundId;
    /**
     * 客服审核时间
     */
    private Date reviewTime;

    /**
     * 客服审核来源
     */
    private Integer reviewSource;

    /**
     * 审核结果编码
     */
    private Integer reviewReasonCode;

    /**
     * 审核结果
     */
    private String reviewReason;

    /**
     * 当前订单是否是退最后一笔
     */
    private boolean lastReturnGoods = false;

    /**
     * 客服审核结果描述信息
     */
    private String auditResultDesc;
}
