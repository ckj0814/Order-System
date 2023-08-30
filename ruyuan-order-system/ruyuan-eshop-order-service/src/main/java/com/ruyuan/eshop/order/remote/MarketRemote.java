package com.ruyuan.eshop.order.remote;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.market.api.MarketApi;
import com.ruyuan.eshop.market.domain.dto.CalculateOrderAmountDTO;
import com.ruyuan.eshop.market.domain.dto.UserCouponDTO;
import com.ruyuan.eshop.market.domain.query.UserCouponQuery;
import com.ruyuan.eshop.market.domain.request.CalculateOrderAmountRequest;
import com.ruyuan.eshop.market.domain.request.LockUserCouponRequest;
import com.ruyuan.eshop.order.exception.OrderBizException;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * 营销服务远程接口
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class MarketRemote {

    /**
     * 营销服务
     */
    @DubboReference(version = "1.0.0", retries = 0)
    private MarketApi marketApi;

    /**
     * 计算订单费用
     * @param calculateOrderPriceRequest
     * @return
     */
    public CalculateOrderAmountDTO calculateOrderAmount(CalculateOrderAmountRequest calculateOrderPriceRequest) {
        JsonResult<CalculateOrderAmountDTO> jsonResult = marketApi.calculateOrderAmount(calculateOrderPriceRequest);
        // 检查价格计算结果
        if (!jsonResult.getSuccess()) {
            throw new OrderBizException(jsonResult.getErrorCode(), jsonResult.getErrorMessage());
        }
        return jsonResult.getData();
    }

    /**
     * 锁定用户优惠券
     * @param lockUserCouponRequest
     * @return
     */
    public Boolean lockUserCoupon(LockUserCouponRequest lockUserCouponRequest) {
        JsonResult<Boolean> jsonResult = marketApi.lockUserCoupon(lockUserCouponRequest);
        // 检查锁定用户优惠券结果
        if (!jsonResult.getSuccess()) {
            throw new OrderBizException(jsonResult.getErrorCode(), jsonResult.getErrorMessage());
        }
        return jsonResult.getData();
    }

    /**
     * 获取用户优惠券
     * @return
     */
    public UserCouponDTO getUserCoupon(UserCouponQuery userCouponQuery) {
        JsonResult<UserCouponDTO> jsonResult = marketApi.getUserCoupon(userCouponQuery);
        if (jsonResult.getSuccess()) {
            return jsonResult.getData();
        }
        return null;
    }


}