package com.ruyuan.eshop.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruyuan.eshop.wms.domain.entity.DeliveryOrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 出库单 Mapper 接口
 * </p>
 *
 * @author zhonghuashishan
 */
@Mapper
public interface DeliveryOrderMapper extends BaseMapper<DeliveryOrderDO> {

}
