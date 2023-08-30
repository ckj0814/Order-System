package com.ruyuan.eshop.inventory.domain.request;

import lombok.Data;

import java.util.List;

/**
 * 压测数据初始化
 */
@Data
public class InitMeasureDataRequest {

    /**
     *
     */
    private List<String> skuCodes;

}