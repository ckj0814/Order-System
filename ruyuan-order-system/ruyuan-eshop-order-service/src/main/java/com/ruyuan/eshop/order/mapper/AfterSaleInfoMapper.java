package com.ruyuan.eshop.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruyuan.eshop.order.domain.dto.AfterSaleListQueryDTO;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderListDTO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 *
 * </p>
 *
 * @author zhonghuashishan
 */
@Mapper
public interface AfterSaleInfoMapper extends BaseMapper<AfterSaleInfoDO> {

    /**
     * 售后单分页查询
     *
     * @param query
     * @return
     */
    Page<AfterSaleOrderListDTO> listByPage(Page<AfterSaleOrderListDTO> page, @Param("query") AfterSaleListQueryDTO query);

}
