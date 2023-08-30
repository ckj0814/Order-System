package com.ruyuan.eshop.fulfill.domain.event;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 订单已签收物流结果消息
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class OrderSignedWmsEvent extends BaseWmsShipEvent {
    /**
     * 签收事件
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signedTime;
}
