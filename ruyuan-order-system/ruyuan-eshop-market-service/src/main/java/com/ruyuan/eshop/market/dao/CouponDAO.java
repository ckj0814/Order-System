package com.ruyuan.eshop.market.dao;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.market.domain.entity.CouponDO;
import com.ruyuan.eshop.market.mapper.CouponMapper;
import org.springframework.stereotype.Repository;

/**
 * 优惠券领取记录管理DAO组件
 *
 * @author zhonghuashishan
 */
@Repository
public class CouponDAO extends BaseDAO<CouponMapper, CouponDO> {

    /**
     * 查询优惠券
     *
     * @param userId
     * @param couponId
     * @return
     */
    public CouponDO getUserCoupon(String userId, String couponId) {
        QueryWrapper<CouponDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("coupon_id", couponId);
        return getOne(queryWrapper);
    }
}
