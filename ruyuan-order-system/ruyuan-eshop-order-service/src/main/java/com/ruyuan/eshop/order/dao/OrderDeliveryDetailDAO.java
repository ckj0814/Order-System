package com.ruyuan.eshop.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.order.domain.entity.OrderDeliveryDetailDO;
import com.ruyuan.eshop.order.domain.request.AdjustDeliveryAddressRequest;
import com.ruyuan.eshop.order.mapper.OrderDeliveryDetailMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 订单配送信息DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class OrderDeliveryDetailDAO extends BaseDAO<OrderDeliveryDetailMapper, OrderDeliveryDetailDO> {

    /**
     * 根据订单号查询订单配送信息
     *
     * @param orderId
     * @return
     */
    public OrderDeliveryDetailDO getByOrderId(String orderId) {
        LambdaQueryWrapper<OrderDeliveryDetailDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDeliveryDetailDO::getOrderId, orderId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 更新配送地址信息
     *
     * @param request
     */
    public boolean updateDeliveryAddress(Long id, Integer modifyAddressCount, AdjustDeliveryAddressRequest request) {
        LambdaUpdateWrapper<OrderDeliveryDetailDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .set(StringUtils.isNotBlank(request.getProvince()), OrderDeliveryDetailDO::getProvince, request.getProvince())
                .set(StringUtils.isNotBlank(request.getCity()), OrderDeliveryDetailDO::getCity, request.getCity())
                .set(StringUtils.isNotBlank(request.getArea()), OrderDeliveryDetailDO::getArea, request.getArea())
                .set(StringUtils.isNotBlank(request.getStreet()), OrderDeliveryDetailDO::getStreet, request.getStreet())
                .set(StringUtils.isNotBlank(request.getDetailAddress()), OrderDeliveryDetailDO::getDetailAddress, request.getDetailAddress())
                .set(Objects.nonNull(request.getLat()), OrderDeliveryDetailDO::getLat, request.getLat())
                .set(Objects.nonNull(request.getLon()), OrderDeliveryDetailDO::getLon, request.getLon())
                .set(OrderDeliveryDetailDO::getModifyAddressCount, modifyAddressCount + 1)
                .eq(OrderDeliveryDetailDO::getId, id);
        return update(updateWrapper);
    }

    /**
     * 更新出库时间
     *
     * @param id
     * @param outStockTime
     * @return
     */
    public boolean updateOutStockTime(Long id, Date outStockTime) {
        LambdaUpdateWrapper<OrderDeliveryDetailDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .set(OrderDeliveryDetailDO::getOutStockTime, outStockTime)
                .eq(OrderDeliveryDetailDO::getId, id);
        return update(updateWrapper);
    }

    /**
     * 更新配送员信息
     *
     * @param id
     * @return
     */
    public boolean updateDeliverer(Long id, String delivererNo, String delivererName, String delivererPhone) {
        LambdaUpdateWrapper<OrderDeliveryDetailDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .set(OrderDeliveryDetailDO::getDelivererNo, delivererNo)
                .set(OrderDeliveryDetailDO::getDelivererName, delivererName)
                .set(OrderDeliveryDetailDO::getDelivererPhone, delivererPhone)
                .eq(OrderDeliveryDetailDO::getId, id);
        return update(updateWrapper);
    }


    /**
     * 更新签收时间
     *
     * @param id
     * @param signedTime
     * @return
     */
    public boolean updateSignedTime(Long id, Date signedTime) {
        LambdaUpdateWrapper<OrderDeliveryDetailDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .set(OrderDeliveryDetailDO::getSignedTime, signedTime)
                .eq(OrderDeliveryDetailDO::getId, id);
        return update(updateWrapper);
    }

}
