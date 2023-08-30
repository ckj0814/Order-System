package com.ruyuan.eshop.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruyuan.eshop.order.domain.entity.OrderItemDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单条目表 Mapper 接口
 * </p>
 *
 * @author zhonghuashishan
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemDO> {

}
