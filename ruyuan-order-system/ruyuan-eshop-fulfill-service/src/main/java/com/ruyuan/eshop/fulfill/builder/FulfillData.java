package com.ruyuan.eshop.fulfill.builder;

import com.ruyuan.eshop.fulfill.domain.entity.OrderFulfillDO;
import com.ruyuan.eshop.fulfill.domain.entity.OrderFulfillItemDO;
import com.ruyuan.eshop.fulfill.domain.request.ReceiveFulfillRequest;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 订单履约数据构造器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@Builder
public class FulfillData {

    /**
     * 订单履约
     */
    private OrderFulfillDO orderFulFill;

    /**
     * 订单履约条目
     */
    private List<OrderFulfillItemDO> orderFulFillItems;

    /**
     * 接受订单履约请求
     */
    private ReceiveFulfillRequest receiveFulFillRequest;
}
