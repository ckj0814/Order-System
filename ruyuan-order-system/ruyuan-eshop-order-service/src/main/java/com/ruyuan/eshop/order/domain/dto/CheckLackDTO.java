package com.ruyuan.eshop.order.domain.dto;

import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 校验缺品请求结果DTO
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckLackDTO {

    /**
     * 订单
     */
    private OrderInfoDO order;

    /**
     * 缺品订单item
     */
    private List<LackItemDTO> lackItems;

}
