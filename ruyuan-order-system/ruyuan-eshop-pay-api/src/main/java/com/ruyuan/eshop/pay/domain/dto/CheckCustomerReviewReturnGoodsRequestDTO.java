package com.ruyuan.eshop.pay.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class CheckCustomerReviewReturnGoodsRequestDTO implements Serializable {
    private static final long serialVersionUID = -3155487939314617012L;

    /**
     * 风控检查结果
     */
    private Boolean result;

    /**
     * 风控提示信息
     */
    private List<String> noticeList;
}
