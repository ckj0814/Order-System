package com.ruyuan.eshop.product.converter;

import com.ruyuan.eshop.product.domain.dto.ProductSkuDTO;
import com.ruyuan.eshop.product.domain.entity.ProductSkuDO;
import com.ruyuan.eshop.product.domain.vo.ProductSkuVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface ProductConverter {

    /**
     * 对象转换
     *
     * @param productSkuDO 对象
     * @return 对象
     */
    ProductSkuDTO convert(ProductSkuDO productSkuDO);

    /**
     * 对象转换
     *
     * @param productSkuDOList 对象
     * @return 对象
     */
    List<ProductSkuDTO> convert(List<ProductSkuDO> productSkuDOList);

    /**
     * 对象转换
     *
     * @param productSkuDTO 对象
     * @return 对象
     */
    ProductSkuVO convert(ProductSkuDTO productSkuDTO);
}
