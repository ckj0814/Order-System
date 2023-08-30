package com.ruyuan.eshop.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.order.domain.entity.OrderSnapshotDO;
import com.ruyuan.eshop.order.mapper.OrderSnapshotMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单快照表 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class OrderSnapshotDAO extends BaseDAO<OrderSnapshotMapper, OrderSnapshotDO> {

    /**
     * 根据订单号查询订单快照
     *
     * @param orderId
     * @return
     */
    public List<OrderSnapshotDO> listByOrderId(String orderId) {
        LambdaQueryWrapper<OrderSnapshotDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderSnapshotDO::getOrderId, orderId);
        return list(queryWrapper);
    }

}
