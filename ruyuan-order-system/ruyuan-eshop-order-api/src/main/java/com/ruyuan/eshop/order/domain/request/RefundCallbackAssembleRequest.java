package com.ruyuan.eshop.order.domain.request;

import com.ruyuan.eshop.order.domain.dto.OrderInfoDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class RefundCallbackAssembleRequest implements Serializable {
    /**
     * 退款批次号
     */
    private Integer batchNo;
    /**
     * 退款状态
     */
    private String refundStatus;
    /**
     * 退款费用
     */
    private Integer refundFee;
    /**
     * 退款总额
     */
    private Integer totalFee;
    /**
     * 支付退款签名
     */
    private String sign;
    /**
     * 交易流水号
     */
    private String tradeNo;
    /**
     * 支付退款时间
     */
    private Date refundTime;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 售后id
     */
    private String afterSaleId;
    /**
     * 订单信息
     */
    private OrderInfoDTO orderInfoDTO;

}
