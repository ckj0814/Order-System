package com.ruyuan.eshop.tms.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.tms.domain.entity.LogisticOrderDO;
import com.ruyuan.eshop.tms.mapper.LogisticOrderMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 物流单 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class LogisticOrderDAO extends BaseDAO<LogisticOrderMapper, LogisticOrderDO> {

    /**
     * 查询物流单
     *
     * @author zhonghuashishan
     * @version 1.0
     */
    public List<LogisticOrderDO> listByOrderId(String orderId) {
        LambdaQueryWrapper<LogisticOrderDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LogisticOrderDO::getOrderId, orderId);
        return list(queryWrapper);
    }

}
