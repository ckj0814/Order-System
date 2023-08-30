package com.ruyuan.eshop.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleLogDO;
import com.ruyuan.eshop.order.mapper.AfterSaleLogMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 售后单变更表 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class AfterSaleLogDAO extends BaseDAO<AfterSaleLogMapper, AfterSaleLogDO> {

    /**
     * 根据售后单号查询售后单变更记录
     *
     * @param afterSaleId
     * @return
     */
    public List<AfterSaleLogDO> listByAfterSaleId(Long afterSaleId) {
        LambdaQueryWrapper<AfterSaleLogDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AfterSaleLogDO::getAfterSaleId, afterSaleId);
        return list(queryWrapper);
    }

    /**
     * 根据售后单号查询售后单变更记录
     *
     * @param afterSaleId
     * @return
     */
    public AfterSaleLogDO getOneByAfterSaleId(Long afterSaleId) {
        LambdaQueryWrapper<AfterSaleLogDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AfterSaleLogDO::getAfterSaleId, afterSaleId);
        return getOne(queryWrapper);
    }

    public boolean update(AfterSaleLogDO afterSaleLogDO) {
        UpdateWrapper<AfterSaleLogDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .set("pre_status", afterSaleLogDO.getPreStatus())
                .set("current_status", afterSaleLogDO.getCurrentStatus())
                .set("remark", afterSaleLogDO.getRemark());
        updateWrapper.eq("after_sale_id", afterSaleLogDO.getAfterSaleId());

        return update(updateWrapper);
    }
}
