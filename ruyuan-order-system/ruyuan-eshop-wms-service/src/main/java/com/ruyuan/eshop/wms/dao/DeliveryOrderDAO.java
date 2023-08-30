package com.ruyuan.eshop.wms.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.wms.domain.entity.DeliveryOrderDO;
import com.ruyuan.eshop.wms.mapper.DeliveryOrderMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 出库单 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class DeliveryOrderDAO extends BaseDAO<DeliveryOrderMapper, DeliveryOrderDO> {

    /**
     * 查询出库单
     *
     * @param orderId
     * @return
     */
    public List<DeliveryOrderDO> listByOrderId(String orderId) {
        LambdaQueryWrapper<DeliveryOrderDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeliveryOrderDO::getOrderId, orderId);
        return list(queryWrapper);
    }

}
