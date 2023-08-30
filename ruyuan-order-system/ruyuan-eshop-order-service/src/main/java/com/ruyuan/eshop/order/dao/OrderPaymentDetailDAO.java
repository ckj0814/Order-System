package com.ruyuan.eshop.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.order.domain.entity.OrderPaymentDetailDO;
import com.ruyuan.eshop.order.mapper.OrderPaymentDetailMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单支付明细表 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class OrderPaymentDetailDAO extends BaseDAO<OrderPaymentDetailMapper, OrderPaymentDetailDO> {

    /**
     * 根据订单号查询支付明细
     *
     * @param orderId
     * @return
     */
    public List<OrderPaymentDetailDO> listByOrderId(String orderId) {
        LambdaQueryWrapper<OrderPaymentDetailDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderPaymentDetailDO::getOrderId, orderId);
        return list(queryWrapper);
    }

    /**
     * 根据多个订单号查询支付明细
     *
     * @param orderIds
     * @return
     */
    public List<OrderPaymentDetailDO> listByOrderIds(List<String> orderIds) {
        LambdaQueryWrapper<OrderPaymentDetailDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(OrderPaymentDetailDO::getOrderId, orderIds);
        return list(queryWrapper);
    }

    /**
     * 查询订单支付明细
     */
    public OrderPaymentDetailDO getPaymentDetailByOrderId(String orderId) {
        QueryWrapper<OrderPaymentDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        return getOne(queryWrapper);
    }

    /**
     * 更新订单支付明细
     */
    public boolean updateByOrderId(OrderPaymentDetailDO orderPaymentDetailDO, String orderId) {
        UpdateWrapper<OrderPaymentDetailDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_id", orderId);
        return update(orderPaymentDetailDO, updateWrapper);
    }

    /**
     * 批量订单支付状态
     *
     * @param orderIds
     */
    public void updateBatchByOrderIds(OrderPaymentDetailDO orderPaymentDetailDO, List<String> orderIds) {
        UpdateWrapper<OrderPaymentDetailDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("order_id", orderIds);
        update(orderPaymentDetailDO, updateWrapper);
    }
}
