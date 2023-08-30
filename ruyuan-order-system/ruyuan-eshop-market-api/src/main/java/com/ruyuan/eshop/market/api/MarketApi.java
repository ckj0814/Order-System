package com.ruyuan.eshop.market.api;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.market.domain.dto.CalculateOrderAmountDTO;
import com.ruyuan.eshop.market.domain.dto.UserCouponDTO;
import com.ruyuan.eshop.market.domain.query.UserCouponQuery;
import com.ruyuan.eshop.market.domain.request.CalculateOrderAmountRequest;
import com.ruyuan.eshop.market.domain.request.LockUserCouponRequest;
import com.ruyuan.eshop.market.domain.request.ReleaseUserCouponRequest;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public interface MarketApi {

    /**
     * 查询用户的优惠券
     *
     * @param userCouponQuery
     * @return
     */
    JsonResult<UserCouponDTO> getUserCoupon(UserCouponQuery userCouponQuery);

    /**
     * 锁定用户优惠券记录
     *
     * @param lockUserCouponRequest
     * @return
     */
    JsonResult<Boolean> lockUserCoupon(LockUserCouponRequest lockUserCouponRequest);

    /**
     * 释放用户已使用的优惠券
     */
    JsonResult<Boolean> releaseUserCoupon(ReleaseUserCouponRequest releaseUserCouponRequest);

    /**
     * 计算订单费用
     *
     * @param calculateOrderAmountRequest
     * @return
     */
    JsonResult<CalculateOrderAmountDTO> calculateOrderAmount(CalculateOrderAmountRequest calculateOrderAmountRequest);

}