package com.ruyuan.eshop.order.domain.dto;

import com.ruyuan.eshop.order.domain.entity.OrderItemDO;
import com.ruyuan.eshop.product.domain.dto.ProductSkuDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LackItemDTO {

    /**
     * 缺品订单条目
     */
    private OrderItemDO orderItem;

    /**
     * 缺品数量
     */
    private Integer lackNum;

    /**
     * 缺品商品sku
     */
    private ProductSkuDTO productSku;
}
