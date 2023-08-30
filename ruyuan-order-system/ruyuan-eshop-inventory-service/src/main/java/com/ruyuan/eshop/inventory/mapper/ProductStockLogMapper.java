package com.ruyuan.eshop.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruyuan.eshop.inventory.domain.entity.ProductStockLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 库存扣减日志表 Mapper 接口
 * </p>
 *
 * @author zhonghuashishan
 */
@Mapper
public interface ProductStockLogMapper extends BaseMapper<ProductStockLogDO> {

}
