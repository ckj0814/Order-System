package com.ruyuan.eshop.order.domain.dto;

import com.ruyuan.eshop.common.enums.OrderStatusChangeEnum;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 物流配送结果请求
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class WmsShipDTO {

    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 订单状态变更
     */
    private OrderStatusChangeEnum statusChange;
    /**
     * 出库时间
     */
    private Date outStockTime;
    /**
     * 签收时间
     */
    private Date signedTime;
    /**
     * 配送员code
     */
    private String delivererNo;
    /**
     * 配送员姓名
     */
    private String delivererName;
    /**
     * 配送员手机号
     */
    private String delivererPhone;
}
