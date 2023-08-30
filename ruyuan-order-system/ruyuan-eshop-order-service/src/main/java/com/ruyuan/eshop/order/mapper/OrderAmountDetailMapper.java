package com.ruyuan.eshop.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruyuan.eshop.order.domain.entity.OrderAmountDetailDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单价格明细表 Mapper 接口
 * </p>
 *
 * @author zhonghuashishan
 */
@Mapper
public interface OrderAmountDetailMapper extends BaseMapper<OrderAmountDetailDO> {

}
