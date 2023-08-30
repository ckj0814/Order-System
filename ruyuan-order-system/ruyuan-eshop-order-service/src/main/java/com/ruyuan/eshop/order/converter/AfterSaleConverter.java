package com.ruyuan.eshop.order.converter;

import com.ruyuan.eshop.order.domain.dto.*;
import com.ruyuan.eshop.order.domain.entity.AfterSaleInfoDO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleItemDO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleLogDO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleRefundDO;
import com.ruyuan.eshop.order.domain.query.AfterSaleQuery;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface AfterSaleConverter {

    /**
     * 对象转换
     *
     * @param afterSaleInfoDO 对象
     * @return 对象
     */
    AfterSaleInfoDTO afterSaleInfoDO2DTO(AfterSaleInfoDO afterSaleInfoDO);

    /**
     * 对象转换
     *
     * @param afterSaleItemDO 对象
     * @return 对象
     */
    AfterSaleItemDTO afterSaleItemDO2DTO(AfterSaleItemDO afterSaleItemDO);

    /**
     * 对象转换
     *
     * @param afterSaleItemDOs 对象
     * @return 对象
     */
    List<AfterSaleItemDTO> afterSaleItemDO2DTO(List<AfterSaleItemDO> afterSaleItemDOs);


    /**
     * 对象转换
     *
     * @param afterSalePay 对象
     * @return 对象
     */
    AfterSalePayDTO afterSalePayDO2DTO(AfterSaleRefundDO afterSalePay);

    /**
     * 对象转换
     *
     * @param afterSalePays 对象
     * @return 对象
     */
    List<AfterSalePayDTO> afterSalePayDO2DTO(List<AfterSaleRefundDO> afterSalePays);


    /**
     * 对象转换
     *
     * @param afterSaleLog 对象
     * @return 对象
     */
    AfterSaleLogDTO afterSaleLogDO2DTO(AfterSaleLogDO afterSaleLog);

    /**
     * 对象转换
     *
     * @param afterSaleLogs 对象
     * @return 对象
     */
    List<AfterSaleLogDTO> afterSaleLogDO2DTO(List<AfterSaleLogDO> afterSaleLogs);

    /**
     * 对象转换
     *
     * @param query 对象
     * @return 对象
     */
    AfterSaleListQueryDTO afterSaleListQueryDTO(AfterSaleQuery query);

    /**
     * 对象转换
     *
     * @param afterSaleItemDOList 对象
     * @return 对象
     */
    AfterSaleOrderItemDTO afterSaleOrderItemDO2DTO(AfterSaleItemDO afterSaleItemDOList);

    /**
     * 对象转换
     *
     * @param afterSaleItemDOList 对象
     * @return 对象
     */
    List<AfterSaleOrderItemDTO> afterSaleOrderItemDO2DTO(List<AfterSaleItemDO> afterSaleItemDOList);
}
