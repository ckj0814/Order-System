package com.ruyuan.eshop.order.domain.dto;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ruyuan.eshop.order.domain.request.LackItemRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 订单缺品信息
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
public class OrderLackInfoDTO {
    /**
     * 订单id
     */
    private String orderId;

    /**
     * 具体的缺品项
     */
    private List<LackItem> lackItems;

    /**
     * 申请退款金额
     */
    private Integer applyRefundAmount;

    /**
     * 实际退款金额
     */
    private Integer realRefundAmount;

    /**
     * 具体的缺品项
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LackItem {
        /**
         * sku编码
         */
        private String skuCode;

        /**
         * 缺品数量
         */
        private Integer lackNum;
    }

    public void setLackItems(Set<LackItemRequest> requests) {
        if (CollectionUtils.isEmpty(requests)) {
            return;
        }
        List<LackItem> lackItems = new ArrayList<>(requests.size());
        requests.forEach(request -> {
            lackItems.add(new LackItem(request.getSkuCode(), request.getLackNum()));
        });

        this.lackItems = lackItems;
    }
}
