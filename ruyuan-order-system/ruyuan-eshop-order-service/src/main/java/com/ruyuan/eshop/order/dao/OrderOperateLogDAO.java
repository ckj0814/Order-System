package com.ruyuan.eshop.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.order.domain.entity.OrderOperateLogDO;
import com.ruyuan.eshop.order.mapper.OrderOperateLogMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单操作日志表 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class OrderOperateLogDAO extends BaseDAO<OrderOperateLogMapper, OrderOperateLogDO> {
    /**
     * 根据订单号查询订单操作日志
     *
     * @param orderId
     * @return
     */
    public List<OrderOperateLogDO> listByOrderId(String orderId) {
        LambdaQueryWrapper<OrderOperateLogDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderOperateLogDO::getOrderId, orderId);
        return list(queryWrapper);
    }

}
