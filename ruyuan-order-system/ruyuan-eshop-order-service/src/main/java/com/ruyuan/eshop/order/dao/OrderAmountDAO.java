package com.ruyuan.eshop.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.order.domain.entity.OrderAmountDO;
import com.ruyuan.eshop.order.mapper.OrderAmountMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单价格表 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class OrderAmountDAO extends BaseDAO<OrderAmountMapper, OrderAmountDO> {

    /**
     * 根据订单号查询订单价格
     *
     * @param orderId
     * @return
     */
    public List<OrderAmountDO> listByOrderId(String orderId) {
        LambdaQueryWrapper<OrderAmountDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderAmountDO::getOrderId, orderId);
        return list(queryWrapper);
    }

    /**
     * 查询订单指定类型费用
     *
     * @param orderId
     * @param amountType
     * @return
     */
    public OrderAmountDO getOne(String orderId, Integer amountType) {
        LambdaQueryWrapper<OrderAmountDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderAmountDO::getOrderId, orderId)
                .eq(OrderAmountDO::getAmountType, amountType);
        return baseMapper.selectOne(queryWrapper);
    }
}
