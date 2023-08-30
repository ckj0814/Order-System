package com.ruyuan.eshop.market.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.utils.ParamCheckUtil;
import com.ruyuan.eshop.market.dao.CouponConfigDAO;
import com.ruyuan.eshop.market.dao.CouponDAO;
import com.ruyuan.eshop.market.domain.dto.UserCouponDTO;
import com.ruyuan.eshop.market.domain.entity.CouponConfigDO;
import com.ruyuan.eshop.market.domain.entity.CouponDO;
import com.ruyuan.eshop.market.domain.query.UserCouponQuery;
import com.ruyuan.eshop.market.domain.request.LockUserCouponRequest;
import com.ruyuan.eshop.market.domain.request.ReleaseUserCouponRequest;
import com.ruyuan.eshop.market.enums.CouponUsedStatusEnum;
import com.ruyuan.eshop.market.exception.MarketBizException;
import com.ruyuan.eshop.market.exception.MarketErrorCodeEnum;
import com.ruyuan.eshop.market.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 优惠券管理service组件
 *
 * @author zhonghuashishan
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponConfigDAO couponConfigDAO;

    @Autowired
    private CouponDAO couponDAO;

    @Override
    public UserCouponDTO getUserCoupon(UserCouponQuery userCouponQuery) {
        // 入参检查
        String userId = userCouponQuery.getUserId();
        String couponId = userCouponQuery.getCouponId();
        ParamCheckUtil.checkStringNonEmpty(userId);
        ParamCheckUtil.checkStringNonEmpty(couponId);

        // 判断用户优惠券是否存在
        CouponDO couponDO = couponDAO.getUserCoupon(userId, couponId);
        if (couponDO == null) {
            throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_IS_NULL);
        }
        String couponConfigId = couponDO.getCouponConfigId();

        // 判断优惠券活动配置信息是否存在
        CouponConfigDO couponConfigDO = couponConfigDAO.getByCouponConfigId(couponConfigId);
        if (couponConfigDO == null) {
            throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_CONFIG_IS_NULL);
        }
        UserCouponDTO userCouponDTO = new UserCouponDTO();
        userCouponDTO.setUserId(userId);
        userCouponDTO.setCouponConfigId(couponConfigId);
        userCouponDTO.setCouponId(couponId);
        userCouponDTO.setName(couponConfigDO.getName());
        userCouponDTO.setType(couponConfigDO.getType());
        userCouponDTO.setAmount(couponConfigDO.getAmount());
        userCouponDTO.setConditionAmount(couponConfigDO.getConditionAmount());
        userCouponDTO.setValidStartTime(couponConfigDO.getValidStartTime());
        userCouponDTO.setValidEndTime(couponConfigDO.getValidEndTime());
        userCouponDTO.setUsed(couponDO.getUsed());
        return userCouponDTO;
    }

    /**
     * 锁定用户优惠券
     *
     * @param lockUserCouponRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean lockUserCoupon(LockUserCouponRequest lockUserCouponRequest) {
        log.info("lockUserCoupon->request={}", JSONObject.toJSONString(lockUserCouponRequest));
        // 检查入参
        checkLockUserCouponRequest(lockUserCouponRequest);

        String userId = lockUserCouponRequest.getUserId();
        String couponId = lockUserCouponRequest.getCouponId();
        CouponDO couponDO = couponDAO.getUserCoupon(userId, couponId);
        if (couponDO == null) {
            throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_IS_NULL);
        }
        // 判断优惠券是否已经使用了
        if (CouponUsedStatusEnum.USED.getCode().equals(couponDO.getUsed())) {
            throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_IS_USED);
        }
        couponDO.setUsed(CouponUsedStatusEnum.USED.getCode());
        couponDO.setUsedTime(new Date());
        couponDAO.updateById(couponDO);
        log.info("lockUserCoupon->response={}", true);
        return true;
    }

    /**
     * 锁定用户优惠券入参检查
     *
     * @param lockUserCouponRequest
     */
    private void checkLockUserCouponRequest(LockUserCouponRequest lockUserCouponRequest) {
        String userId = lockUserCouponRequest.getUserId();
        String couponId = lockUserCouponRequest.getCouponId();
        ParamCheckUtil.checkStringNonEmpty(userId);
        ParamCheckUtil.checkStringNonEmpty(couponId);
    }

    /**
     * 释放用户优惠券
     */
    @Override
    public Boolean releaseUserCoupon(ReleaseUserCouponRequest releaseUserCouponRequest) {
        String userId = releaseUserCouponRequest.getUserId();
        String couponId = releaseUserCouponRequest.getCouponId();
        CouponDO couponAchieve = couponDAO.getUserCoupon(userId, couponId);
        if (CouponUsedStatusEnum.UN_USED.getCode().equals(couponAchieve.getUsed())) {
            log.info("当前用户未使用优惠券,不用回退,userId:{},couponId:{}", userId, couponId);
            return true;
        }
        couponAchieve.setUsed(CouponUsedStatusEnum.UN_USED.getCode());
        couponAchieve.setUsedTime(null);
        couponDAO.updateById(couponAchieve);
        return true;
    }
}
