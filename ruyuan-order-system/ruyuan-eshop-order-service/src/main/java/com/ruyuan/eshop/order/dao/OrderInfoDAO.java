package com.ruyuan.eshop.order.dao;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.common.enums.DeleteStatusEnum;
import com.ruyuan.eshop.common.enums.OrderStatusEnum;
import com.ruyuan.eshop.order.domain.dto.OrderExtJsonDTO;
import com.ruyuan.eshop.order.domain.dto.OrderListDTO;
import com.ruyuan.eshop.order.domain.dto.OrderListQueryDTO;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import com.ruyuan.eshop.order.mapper.OrderInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
@Slf4j
public class OrderInfoDAO extends BaseDAO<OrderInfoMapper, OrderInfoDO> {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    /**
     * 根据订单号查询订单号
     *
     * @param orderIds
     * @return
     */
    public List<OrderInfoDO> listByOrderIds(List<String> orderIds) {
        LambdaQueryWrapper<OrderInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(OrderInfoDO::getOrderId, orderIds);
        return list(queryWrapper);
    }

    /**
     * 软删除订单
     *
     * @param ids 订单主键id
     */
    public void softRemoveOrders(List<Long> ids) {
        LambdaUpdateWrapper<OrderInfoDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(OrderInfoDO::getDeleteStatus, DeleteStatusEnum.YES.getCode())
                .in(OrderInfoDO::getId, ids);
        this.update(updateWrapper);
    }

    /**
     * 根据订单号查询订单
     *
     * @param orderId
     * @return
     */
    public OrderInfoDO getByOrderId(String orderId) {
        LambdaQueryWrapper<OrderInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfoDO::getOrderId, orderId);
        return getOne(queryWrapper);
    }

    /**
     * 根据订单号更新订单
     *
     * @param orderInfoDO
     * @param orderId
     * @return
     */
    public boolean updateByOrderId(OrderInfoDO orderInfoDO, String orderId) {
        LambdaUpdateWrapper<OrderInfoDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(OrderInfoDO::getOrderId, orderId);
        return update(orderInfoDO, updateWrapper);
    }


    /**
     * 根据父订单号更新订单
     *
     * @param orderInfoDO
     * @param orderIds
     * @return
     */
    public boolean updateBatchByOrderIds(OrderInfoDO orderInfoDO, List<String> orderIds) {
        LambdaUpdateWrapper<OrderInfoDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(OrderInfoDO::getOrderId, orderIds);
        return update(orderInfoDO, updateWrapper);
    }

    /**
     * 统计子订单数量
     * @param orderId
     * @return
     */
    public List<String> listSubOrderIds(String orderId) {
        LambdaQueryWrapper<OrderInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(OrderInfoDO::getOrderId);
        queryWrapper.eq(OrderInfoDO::getParentOrderId, orderId);
        return list(queryWrapper).stream().map(OrderInfoDO::getOrderId).collect(Collectors.toList());
    }

    /**
     * 根据父订单号查询子订单号
     *
     * @param orderId
     * @return
     */
    public List<OrderInfoDO> listByParentOrderId(String orderId) {
        LambdaQueryWrapper<OrderInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfoDO::getParentOrderId, orderId);
        return list(queryWrapper);
    }

    /**
     * 根据条件分页查询订单列表
     *
     * @param query
     * @return
     */
    public Page<OrderListDTO> listByPage(OrderListQueryDTO query) {
        Page<OrderListDTO> page = new Page<>(query.getPageNo(), query.getPageSize());
        return orderInfoMapper.listByPage(page, query);
    }

    /**
     * 更新订单扩展信息
     *
     * @param orderId
     * @return
     */
    public boolean updateOrderExtJson(String orderId, OrderExtJsonDTO extJson) {
        String extJsonStr = JSONObject.toJSONString(extJson);
        LambdaUpdateWrapper<OrderInfoDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(OrderInfoDO::getExtJson, extJsonStr)
                .eq(OrderInfoDO::getOrderId, orderId);
        return this.update(updateWrapper);
    }

    public boolean updateOrderInfo(OrderInfoDO orderInfoDO) {
        UpdateWrapper<OrderInfoDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_id", orderInfoDO.getOrderId());
        return update(orderInfoDO, updateWrapper);
    }

    public boolean updateOrderStatus(String orderId, Integer fromStatus, Integer toStatus) {
        LambdaUpdateWrapper<OrderInfoDO> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.set(OrderInfoDO::getOrderStatus, toStatus)
                .eq(OrderInfoDO::getOrderId, orderId)
                .eq(OrderInfoDO::getOrderStatus, fromStatus);

        return update(updateWrapper);
    }

    /**
     * 扫描所有未支付订单
     *
     * @return
     */
    public List<OrderInfoDO> listAllUnPaid() {
        LambdaQueryWrapper<OrderInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(OrderInfoDO::getOrderStatus, OrderStatusEnum.unPaidStatus());
        return list(queryWrapper);
    }
}
