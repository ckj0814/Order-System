package com.ruyuan.eshop.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.ruyuan.eshop.common.enums.AfterSaleTypeDetailEnum;
import com.ruyuan.eshop.common.enums.AfterSaleTypeEnum;
import com.ruyuan.eshop.common.page.PagingInfo;
import com.ruyuan.eshop.common.utils.ParamCheckUtil;
import com.ruyuan.eshop.order.converter.AfterSaleConverter;
import com.ruyuan.eshop.order.dao.AfterSaleInfoDAO;
import com.ruyuan.eshop.order.dao.AfterSaleItemDAO;
import com.ruyuan.eshop.order.dao.AfterSaleLogDAO;
import com.ruyuan.eshop.order.dao.AfterSaleRefundDAO;
import com.ruyuan.eshop.order.domain.dto.AfterSaleListQueryDTO;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderDetailDTO;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderListDTO;
import com.ruyuan.eshop.order.domain.dto.OrderLackItemDTO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleInfoDO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleItemDO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleLogDO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleRefundDO;
import com.ruyuan.eshop.order.domain.query.AfterSaleQuery;
import com.ruyuan.eshop.order.enums.AfterSaleApplySourceEnum;
import com.ruyuan.eshop.order.enums.AfterSaleStatusEnum;
import com.ruyuan.eshop.order.enums.BusinessIdentifierEnum;
import com.ruyuan.eshop.order.enums.OrderTypeEnum;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
import com.ruyuan.eshop.order.service.AfterSaleQueryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AfterSaleQueryServiceImpl implements AfterSaleQueryService {

    @Autowired
    private AfterSaleInfoDAO afterSaleInfoDAO;

    @Autowired
    private AfterSaleItemDAO afterSaleItemDAO;

    @Autowired
    private AfterSaleRefundDAO afterSaleRefundDAO;

    @Autowired
    private AfterSaleLogDAO afterSaleLogDAO;

    @Autowired
    private AfterSaleConverter afterSaleConverter;

    @Override
    public void checkQueryParam(AfterSaleQuery query) {

        ParamCheckUtil.checkObjectNonNull(query.getBusinessIdentifier(), OrderErrorCodeEnum.BUSINESS_IDENTIFIER_IS_NULL);
        checkIntAllowableValues(query.getBusinessIdentifier(), BusinessIdentifierEnum.allowableValues(), "businessIdentifier");
        checkIntSetAllowableValues(query.getOrderTypes(), OrderTypeEnum.allowableValues(), "orderTypes");
        checkIntSetAllowableValues(query.getAfterSaleTypes(), AfterSaleStatusEnum.allowableValues(), "afterSaleStatus");
        checkIntSetAllowableValues(query.getApplySources(), AfterSaleApplySourceEnum.allowableValues(), "applySources");
        checkIntSetAllowableValues(query.getAfterSaleTypes(), AfterSaleTypeEnum.allowableValues(), "afterSaleTypes");


        Integer maxSize = AfterSaleQuery.MAX_PAGE_SIZE;
        checkSetMaxSize(query.getAfterSaleIds(), maxSize, "afterSaleIds");
        checkSetMaxSize(query.getOrderIds(), maxSize, "orderIds");
        checkSetMaxSize(query.getUserIds(), maxSize, "userIds");
        checkSetMaxSize(query.getSkuCodes(), maxSize, "skuCodes");

    }

    @Override
    public PagingInfo<AfterSaleOrderListDTO> executeListQuery(AfterSaleQuery query) {

        //第一阶段采用连表查询
        //第二阶段会接入es

        //1、组装业务查询规则
        if (CollectionUtils.isEmpty(query.getApplySources())) {
            //默认只展示用户主动发起的售后单
            query.setApplySources(AfterSaleApplySourceEnum.userApply());
        }
        AfterSaleListQueryDTO queryDTO = afterSaleConverter.afterSaleListQueryDTO(query);

        //2、查询
        Page<AfterSaleOrderListDTO> page = afterSaleInfoDAO.listByPage(queryDTO);

        //3、转化
        return PagingInfo.toResponse(page.getRecords()
                , page.getTotal(), (int) page.getCurrent(), (int) page.getSize());
    }

    @Override
    public AfterSaleOrderDetailDTO afterSaleDetail(Long afterSaleId) {
        //1、查询售后单
        AfterSaleInfoDO afterSaleInfo = afterSaleInfoDAO.getOneByAfterSaleId(afterSaleId);

        if (null == afterSaleInfo) {
            return null;
        }

        //2、查询售后单条目
        List<AfterSaleItemDO> afterSaleItems = afterSaleItemDAO.listByAfterSaleId(afterSaleId);

        //3、查询售后支付信息
        List<AfterSaleRefundDO> afterSalePays = afterSaleRefundDAO.listByAfterSaleId(afterSaleId);

        //4、查询售后日志
        List<AfterSaleLogDO> afterSaleLogs = afterSaleLogDAO.listByAfterSaleId(afterSaleId);

        //5、构造返参
        return AfterSaleOrderDetailDTO.builder()
                .afterSaleInfo(afterSaleConverter.afterSaleInfoDO2DTO(afterSaleInfo))
                .afterSaleItems(afterSaleConverter.afterSaleItemDO2DTO(afterSaleItems))
                .afterSalePays(afterSaleConverter.afterSalePayDO2DTO(afterSalePays))
                .afterSaleLogs(afterSaleConverter.afterSaleLogDO2DTO(afterSaleLogs))
                .build();
    }

    @Override
    public List<OrderLackItemDTO> getOrderLackItemInfo(String orderId) {
        List<AfterSaleInfoDO> lackItemDO = afterSaleInfoDAO.listBy(orderId
                , Lists.newArrayList(AfterSaleTypeDetailEnum.LACK_REFUND.getCode()));
        if (CollectionUtils.isEmpty(lackItemDO)) {
            return null;
        }

        List<OrderLackItemDTO> lackItems = new ArrayList<>();

        lackItemDO.forEach(lackItem -> {
            AfterSaleOrderDetailDTO detailDTO = afterSaleDetail(lackItem.getAfterSaleId());
            OrderLackItemDTO itemDTO = new OrderLackItemDTO();
            BeanUtils.copyProperties(detailDTO, itemDTO);
            lackItems.add(itemDTO);
        });

        return lackItems;
    }

    private void checkIntAllowableValues(Integer i, Set<Integer> allowableValues, String paramName) {
        OrderErrorCodeEnum orderErrorCodeEnum = OrderErrorCodeEnum.ENUM_PARAM_MUST_BE_IN_ALLOWABLE_VALUE;
        ParamCheckUtil.checkIntAllowableValues(i
                , allowableValues,
                orderErrorCodeEnum, paramName, allowableValues);
    }

    private void checkIntSetAllowableValues(Set<Integer> set, Set<Integer> allowableValues, String paramName) {
        OrderErrorCodeEnum orderErrorCodeEnum = OrderErrorCodeEnum.ENUM_PARAM_MUST_BE_IN_ALLOWABLE_VALUE;
        ParamCheckUtil.checkIntSetAllowableValues(set
                , allowableValues,
                orderErrorCodeEnum, paramName, allowableValues);
    }

    private void checkSetMaxSize(Set setParam, Integer maxSize, String paramName) {
        OrderErrorCodeEnum orderErrorCodeEnum = OrderErrorCodeEnum.COLLECTION_PARAM_CANNOT_BEYOND_MAX_SIZE;
        ParamCheckUtil.checkSetMaxSize(setParam, maxSize,
                orderErrorCodeEnum, paramName
                , maxSize);

    }
}
