package com.ruyuan.eshop.fulfill.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.fulfill.domain.entity.OrderFulfillItemDO;
import com.ruyuan.eshop.fulfill.mapper.OrderFulfillItemMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单履约条目表 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class OrderFulfillItemDAO extends BaseDAO<OrderFulfillItemMapper, OrderFulfillItemDO> {

    /**
     * 查询履约单item
     *
     * @param fulfillId
     * @return
     */
    public List<OrderFulfillItemDO> listByFulfillId(String fulfillId) {
        LambdaQueryWrapper<OrderFulfillItemDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderFulfillItemDO::getFulfillId, fulfillId);
        return list(queryWrapper);
    }
}
