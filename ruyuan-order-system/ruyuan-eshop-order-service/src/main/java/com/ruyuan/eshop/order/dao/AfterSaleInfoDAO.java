package com.ruyuan.eshop.order.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.order.domain.dto.AfterSaleListQueryDTO;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderListDTO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleInfoDO;
import com.ruyuan.eshop.order.domain.request.CustomerAuditAssembleRequest;
import com.ruyuan.eshop.order.mapper.AfterSaleInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单售后表 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
@Slf4j
public class AfterSaleInfoDAO extends BaseDAO<AfterSaleInfoMapper, AfterSaleInfoDO> {

    @Autowired
    private AfterSaleInfoMapper afterSaleInfoMapper;

    /**
     * 根据条件分页查询售后列表
     *
     * @param query
     * @return
     */
    public Page<AfterSaleOrderListDTO> listByPage(AfterSaleListQueryDTO query) {
        log.info("query={}", JSONObject.toJSONString(query));
        Page<AfterSaleOrderListDTO> page = new Page<>(query.getPageNo(), query.getPageSize());
        return afterSaleInfoMapper.listByPage(page, query);
    }

    /**
     * 根据售后单号查询售后单
     *
     * @param afterSaleId
     * @return
     */
    public AfterSaleInfoDO getOneByAfterSaleId(Long afterSaleId) {
        LambdaQueryWrapper<AfterSaleInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AfterSaleInfoDO::getAfterSaleId, afterSaleId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据订单编号，售后类型详情查询售后单
     *
     * @param afterSaleTypeDetails
     * @return
     */
    public List<AfterSaleInfoDO> listBy(String orderId, List<Integer> afterSaleTypeDetails) {
        LambdaQueryWrapper<AfterSaleInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AfterSaleInfoDO::getOrderId, orderId)
                .in(AfterSaleInfoDO::getAfterSaleTypeDetail, afterSaleTypeDetails);
        return list(queryWrapper);
    }

    /**
     * 根据订单号查售后单
     */
    public AfterSaleInfoDO getOneByOrderId(Long orderId) {
        LambdaQueryWrapper<AfterSaleInfoDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AfterSaleInfoDO::getOrderId, orderId);
        return baseMapper.selectOne(queryWrapper);
    }

    public boolean updateCustomerAuditAfterSaleResult(Integer afterSaleStatus, CustomerAuditAssembleRequest customerAuditAssembleRequest) {
        Long afterSaleId = customerAuditAssembleRequest.getAfterSaleId();
        String reviewReason = customerAuditAssembleRequest.getReviewReason();
        Integer reviewReasonCode = customerAuditAssembleRequest.getReviewReasonCode();
        Integer reviewSource = customerAuditAssembleRequest.getReviewSource();
        Date reviewTime = customerAuditAssembleRequest.getReviewTime();

        UpdateWrapper<AfterSaleInfoDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("after_sale_status", afterSaleStatus);
        updateWrapper.set(reviewReason != null, "review_reason", reviewReason);
        updateWrapper.set(reviewReasonCode != null, "review_reason_code", reviewReasonCode);
        updateWrapper.set(reviewSource != null, "review_source", reviewSource);
        updateWrapper.set(reviewTime != null, "review_time", reviewTime);
        updateWrapper.eq("after_sale_id", afterSaleId);

        return update(updateWrapper);
    }

    /**
     * 更新售后单状态
     *
     * @param afterSaleId
     * @param fromStatus
     * @param toStatus
     * @return
     */
    public boolean updateStatus(Long afterSaleId, Integer fromStatus, Integer toStatus) {
        LambdaUpdateWrapper<AfterSaleInfoDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(AfterSaleInfoDO::getAfterSaleStatus, toStatus)
                .eq(AfterSaleInfoDO::getAfterSaleId, afterSaleId)
                .eq(AfterSaleInfoDO::getAfterSaleStatus, fromStatus);
        return update(updateWrapper);
    }
}
