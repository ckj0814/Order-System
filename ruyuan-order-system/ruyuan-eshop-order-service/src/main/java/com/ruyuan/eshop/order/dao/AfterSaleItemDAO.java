package com.ruyuan.eshop.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleItemDO;
import com.ruyuan.eshop.order.mapper.AfterSaleItemMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单售后条目表 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class AfterSaleItemDAO extends BaseDAO<AfterSaleItemMapper, AfterSaleItemDO> {

    /**
     * 根据售后单号查询售后单条目记录
     *
     * @param afterSaleId
     * @return
     */
    public List<AfterSaleItemDO> listByAfterSaleId(Long afterSaleId) {
        LambdaQueryWrapper<AfterSaleItemDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AfterSaleItemDO::getAfterSaleId, afterSaleId);
        return list(queryWrapper);
    }

    /**
     * 根据订单号查询售后单条目
     *
     * @return
     */
    public List<AfterSaleItemDO> listByOrderId(Long orderId) {
        LambdaQueryWrapper<AfterSaleItemDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AfterSaleItemDO::getOrderId, orderId);
        return list(queryWrapper);
    }

    /**
     * 根据orderId和skuCode查询售后单条目
     * 这里做成list便于以后扩展
     * 目前仅支持整笔条目的退货，所以当前list里只有一条
     */
    public List<AfterSaleItemDO> getOrderIdAndSkuCode(String orderId, String skuCode) {
        LambdaQueryWrapper<AfterSaleItemDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AfterSaleItemDO::getOrderId, orderId);
        queryWrapper.eq(AfterSaleItemDO::getSkuCode, skuCode);
        return list(queryWrapper);
    }

    /**
     * 根据orderId和afterSaleId查询售后单条目
     */
    public AfterSaleItemDO getOrderIdAndAfterSaleId(String orderId, Long afterSaleId) {
        LambdaQueryWrapper<AfterSaleItemDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AfterSaleItemDO::getOrderId, orderId);
        queryWrapper.eq(AfterSaleItemDO::getAfterSaleId, afterSaleId);
        return getOne(queryWrapper);
    }

    /**
     * 查询出不包含当前afterSaleId的售后条目
     */
    public List<AfterSaleItemDO> listNotContainCurrentAfterSaleId(String orderId, Long afterSaleId) {
        LambdaQueryWrapper<AfterSaleItemDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AfterSaleItemDO::getOrderId, orderId);
        queryWrapper.notIn(AfterSaleItemDO::getAfterSaleId, afterSaleId);
        return list(queryWrapper);
    }
}
