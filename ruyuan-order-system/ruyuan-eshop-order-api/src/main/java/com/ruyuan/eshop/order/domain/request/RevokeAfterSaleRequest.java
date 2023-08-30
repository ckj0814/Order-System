package com.ruyuan.eshop.order.domain.request;

import lombok.Data;

/**
 * 用户撤销售后申请
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class RevokeAfterSaleRequest {
    /**
     * 售后单
     */
    private Long afterSaleId;
}
