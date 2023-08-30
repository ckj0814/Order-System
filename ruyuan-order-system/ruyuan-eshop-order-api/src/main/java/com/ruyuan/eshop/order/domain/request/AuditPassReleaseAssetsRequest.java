package com.ruyuan.eshop.order.domain.request;

import com.ruyuan.eshop.common.message.ActualRefundMessage;
import com.ruyuan.eshop.order.domain.dto.ReleaseProductStockDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * 客服审核通过后发送释放资产入参
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class AuditPassReleaseAssetsRequest implements Serializable {
    /**
     * 释放库存DTO
     */
    private ReleaseProductStockDTO releaseProductStockDTO;

    /**
     * 实际退款message数据
     */
    private ActualRefundMessage actualRefundMessage;
}
