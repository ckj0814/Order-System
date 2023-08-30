package com.ruyuan.eshop.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruyuan.eshop.order.domain.entity.OrderPaymentDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 订单支付明细表 Mapper 接口
 * </p>
 *
 * @author zhonghuashishan
 */
@Mapper
public interface OrderPaymentDetailMapper extends BaseMapper<OrderPaymentDetailDO> {
    /**
     * 自定义查询 通过订单号查询订单状态
     *
     * @param orderId 订单Id
     * @return 支付状态
     */
    @Select("SELECT pay_status FROM order_payment_detail WHERE order_id = ${orderId}")
    Integer getPayStatusByOrderNo(@Param("orderId") String orderId);
}
