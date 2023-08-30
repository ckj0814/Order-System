package com.ruyuan.eshop.order.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 订单缺品请求
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class LackRequest implements Serializable {

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 具体的缺品项
     */
    private Set<LackItemRequest> lackItems;

}
