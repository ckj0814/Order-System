package com.ruyuan.eshop.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.order.domain.entity.OrderItemDO;
import com.ruyuan.eshop.order.mapper.OrderItemMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单条目表 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class OrderItemDAO extends BaseDAO<OrderItemMapper, OrderItemDO> {

    /**
     * 根据订单号查询订单条目
     *
     * @param orderId
     * @return
     */
    public List<OrderItemDO> listByOrderId(String orderId) {
        LambdaQueryWrapper<OrderItemDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderItemDO::getOrderId, orderId);
        return list(queryWrapper);
    }

    /**
     * 按订单号与产品类型查询订单条目
     *
     * @param orderId
     * @param productType
     * @return
     */
    public List<OrderItemDO> listByOrderIdAndProductType(String orderId, Integer productType) {
        LambdaQueryWrapper<OrderItemDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderItemDO::getOrderId, orderId)
                .eq(OrderItemDO::getProductType, productType);
        return list(queryWrapper);
    }

    /**
     * 根据订单号和skuId查询订单条目
     *
     * @return
     */
    public OrderItemDO getOrderItemBySkuIdAndOrderId(String orderId, String skuId) {
        LambdaQueryWrapper<OrderItemDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderItemDO::getOrderId, orderId)
                .eq(OrderItemDO::getSkuCode, skuId);
        return getOne(queryWrapper);
    }

}
