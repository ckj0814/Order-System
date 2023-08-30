package com.ruyuan.eshop.wms.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.wms.domain.entity.DeliveryOrderItemDO;
import com.ruyuan.eshop.wms.mapper.DeliveryOrderItemMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 出库单条目 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class DeliveryOrderItemDAO extends BaseDAO<DeliveryOrderItemMapper, DeliveryOrderItemDO> {

    /**
     * 查询出库单item
     *
     * @param deliveryOrderId
     * @return
     */
    public List<DeliveryOrderItemDO> listByDeliveryOrderId(String deliveryOrderId) {
        LambdaQueryWrapper<DeliveryOrderItemDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeliveryOrderItemDO::getDeliveryOrderId, deliveryOrderId);
        return list(queryWrapper);
    }

    /**
     * 查询出库单item
     *
     * @param deliveryOrderIds
     * @return
     */
    public List<DeliveryOrderItemDO> listByDeliveryOrderIds(List<String> deliveryOrderIds) {
        LambdaQueryWrapper<DeliveryOrderItemDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeliveryOrderItemDO::getDeliveryOrderId, deliveryOrderIds);
        return list(queryWrapper);
    }

}
