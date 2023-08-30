package com.ruyuan.eshop.market.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.market.domain.entity.CouponConfigDO;
import com.ruyuan.eshop.market.mapper.CouponConfigMapper;
import org.springframework.stereotype.Repository;

/**
 * 优惠券管理DAO组件
 *
 * @author zhonghuashishan
 */
@Repository
public class CouponConfigDAO extends BaseDAO<CouponConfigMapper, CouponConfigDO> {

    /**
     * 优惠券配置信息
     *
     * @param couponConfigId
     * @return
     */
    public CouponConfigDO getByCouponConfigId(String couponConfigId) {
        QueryWrapper<CouponConfigDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("coupon_config_id", couponConfigId);
        return getOne(queryWrapper);
    }

}
