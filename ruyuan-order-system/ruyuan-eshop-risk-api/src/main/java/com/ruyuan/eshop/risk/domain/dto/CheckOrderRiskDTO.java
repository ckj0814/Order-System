package com.ruyuan.eshop.risk.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 订单风控检查结果
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class CheckOrderRiskDTO implements Serializable {

    private static final long serialVersionUID = -2087808396344344437L;

    /**
     * 风控检查结果
     */
    private Boolean result;

    /**
     * 风控提示信息
     */
    private List<String> noticeList;

}