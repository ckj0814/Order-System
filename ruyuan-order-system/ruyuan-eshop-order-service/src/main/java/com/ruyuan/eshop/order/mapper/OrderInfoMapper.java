package com.ruyuan.eshop.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruyuan.eshop.order.domain.dto.OrderListDTO;
import com.ruyuan.eshop.order.domain.dto.OrderListQueryDTO;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
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
public interface OrderInfoMapper extends BaseMapper<OrderInfoDO> {

    /**
     * 订单分页查询
     *
     * @param query
     * @return
     */
    Page<OrderListDTO> listByPage(Page<OrderListDTO> page, @Param("query") OrderListQueryDTO query);

}
