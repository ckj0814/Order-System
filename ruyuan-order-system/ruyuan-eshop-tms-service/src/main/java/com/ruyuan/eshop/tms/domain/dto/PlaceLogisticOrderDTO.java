package com.ruyuan.eshop.tms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceLogisticOrderDTO {

    /**
     * 三方物流单号
     */
    private String logisticCode;

    /**
     * 物流单内容
     */
    private String content;
}
