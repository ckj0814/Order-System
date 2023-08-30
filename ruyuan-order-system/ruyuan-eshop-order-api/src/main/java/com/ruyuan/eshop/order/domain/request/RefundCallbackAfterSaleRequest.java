package com.ruyuan.eshop.order.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class RefundCallbackAfterSaleRequest implements Serializable {
    /**
     * 退款状态
     */
    private Integer refundStatus;
    /**
     * 售后id
     */
    private String afterSaleId;
    /**
     * 订单信息
     */
    private Date refundTime;
    /**
     * 备注
     */
    private String remark;
}
