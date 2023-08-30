package com.ruyuan.eshop.order.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 售后单变更表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class AfterSaleLogDTO implements Serializable {

    /**
     * 售后单号
     */
    private String afterSaleId;

    /**
     * 前一个状态
     */
    private Integer preStatus;

    /**
     * 当前状态
     */
    private Integer currentStatus;

    /**
     * 备注
     */
    private String remark;
}
