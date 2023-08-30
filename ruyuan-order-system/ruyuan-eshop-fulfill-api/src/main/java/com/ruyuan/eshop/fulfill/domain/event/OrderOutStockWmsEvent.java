package com.ruyuan.eshop.fulfill.domain.event;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 订单已出库物流结果消息
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class OrderOutStockWmsEvent extends BaseWmsShipEvent {
    /**
     * 出库时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outStockTime;
}
