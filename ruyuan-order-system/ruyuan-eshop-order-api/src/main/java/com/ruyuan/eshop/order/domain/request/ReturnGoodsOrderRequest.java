package com.ruyuan.eshop.order.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 售后退货入参
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class ReturnGoodsOrderRequest implements Serializable {
    private static final long serialVersionUID = 4893844329777688246L;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 接入方业务线标识  1, "自营商城"
     */
    private Integer businessIdentifier;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 退货原因选项
     */
    private Integer returnGoodsCode;

    /**
     * 退货原因说明
     */
    private String returnGoodsDesc;

    /**
     * 退货sku编号
     */
    private String skuCode;
}
