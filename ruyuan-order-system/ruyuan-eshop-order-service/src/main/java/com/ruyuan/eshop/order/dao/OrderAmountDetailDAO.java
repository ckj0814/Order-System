package com.ruyuan.eshop.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.order.domain.entity.OrderAmountDetailDO;
import com.ruyuan.eshop.order.mapper.OrderAmountDetailMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单价格明细表 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class OrderAmountDetailDAO extends BaseDAO<OrderAmountDetailMapper, OrderAmountDetailDO> {

    /**
     * 根据订单号查询订单费用明细
     *
     * @param orderId
     * @return
     */
    public List<OrderAmountDetailDO> listByOrderId(String orderId) {
        LambdaQueryWrapper<OrderAmountDetailDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderAmountDetailDO::getOrderId, orderId);
        return list(queryWrapper);
    }

    /**
     * 根据订单号查询订单费用明细
     *
     * @param orderId
     * @param orderItemId
     * @return
     */
    public List<OrderAmountDetailDO> listByOrderIdAndOrderItemId(String orderId, String orderItemId) {
        LambdaQueryWrapper<OrderAmountDetailDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderAmountDetailDO::getOrderId, orderId)
                .eq(OrderAmountDetailDO::getOrderItemId, orderItemId);
        return list(queryWrapper);
    }

}
