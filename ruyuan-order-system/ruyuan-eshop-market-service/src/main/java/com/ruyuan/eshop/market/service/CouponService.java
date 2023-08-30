package com.ruyuan.eshop.market.service;

import com.ruyuan.eshop.market.domain.dto.UserCouponDTO;
import com.ruyuan.eshop.market.domain.query.UserCouponQuery;
import com.ruyuan.eshop.market.domain.request.LockUserCouponRequest;
import com.ruyuan.eshop.market.domain.request.ReleaseUserCouponRequest;


/**
 * 优惠券管理service接口
 *
 * @author zhonghuashishan
 */
public interface CouponService {

    /**
     * 查询用户的优惠券信息
     *
     * @param userCouponQuery
     * @return
     */
    UserCouponDTO getUserCoupon(UserCouponQuery userCouponQuery);

    /**
     * 锁定用户优惠券
     *
     * @param lockUserCouponRequest
     * @return
     */
    Boolean lockUserCoupon(LockUserCouponRequest lockUserCouponRequest);

    /**
     * 释放用户优惠券
     */
    Boolean releaseUserCoupon(ReleaseUserCouponRequest releaseUserCouponRequest);
}
