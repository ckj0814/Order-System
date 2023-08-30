package com.ruyuan.eshop.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruyuan.eshop.product.domain.entity.ProductSkuDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品sku记录表 Mapper 接口
 * </p>
 *
 * @author zhonghuashishan
 */
@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSkuDO> {

}
