package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 订单快照DTO
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class OrderSnapshotDTO implements Serializable {

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 订单快照内容
     */
    private String snapshotJson;


}
