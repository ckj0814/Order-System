package com.ruyuan.eshop.fulfill.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.fulfill.domain.entity.OrderFulfillDO;
import com.ruyuan.eshop.fulfill.mapper.OrderFulfillMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单履约表 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class OrderFulfillDAO extends BaseDAO<OrderFulfillMapper, OrderFulfillDO> {

    /**
     * 保存物流单号
     *
     * @param fulfillId
     * @param logisticsCode
     */
    public boolean saveLogisticsCode(String fulfillId, String logisticsCode) {
        LambdaUpdateWrapper<OrderFulfillDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .set(OrderFulfillDO::getLogisticsCode, logisticsCode)
                .eq(OrderFulfillDO::getFulfillId, fulfillId);
        return update(updateWrapper);
    }

    /**
     * 查询履约单
     *
     * @param orderId
     * @return
     */
    public OrderFulfillDO getOne(String orderId) {
        LambdaQueryWrapper<OrderFulfillDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderFulfillDO::getOrderId, orderId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 更新配送员信息
     *
     * @return
     */
    public boolean updateDeliverer(String fulfillId, String delivererNo, String delivererName, String delivererPhone) {
        LambdaUpdateWrapper<OrderFulfillDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .set(OrderFulfillDO::getDelivererNo, delivererNo)
                .set(OrderFulfillDO::getDelivererName, delivererName)
                .set(OrderFulfillDO::getDelivererPhone, delivererPhone)
                .eq(OrderFulfillDO::getFulfillId, fulfillId);
        return update(updateWrapper);
    }
}
