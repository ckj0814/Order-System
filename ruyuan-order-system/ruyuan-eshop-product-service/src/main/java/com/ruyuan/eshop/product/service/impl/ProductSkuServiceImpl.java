package com.ruyuan.eshop.product.service.impl;

import com.ruyuan.eshop.common.utils.ParamCheckUtil;
import com.ruyuan.eshop.product.converter.ProductConverter;
import com.ruyuan.eshop.product.dao.ProductSkuDAO;
import com.ruyuan.eshop.product.domain.dto.ProductSkuDTO;
import com.ruyuan.eshop.product.domain.entity.ProductSkuDO;
import com.ruyuan.eshop.product.exception.ProductErrorCodeEnum;
import com.ruyuan.eshop.product.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
public class ProductSkuServiceImpl implements ProductSkuService {

    @Autowired
    private ProductSkuDAO productSkuDAO;

    @Autowired
    private ProductConverter productConverter;

    @Override
    public ProductSkuDTO getProductSkuByCode(String skuCode) {
        ParamCheckUtil.checkStringNonEmpty(skuCode, ProductErrorCodeEnum.SKU_CODE_IS_NULL);

        ProductSkuDO productSkuDO = productSkuDAO.getProductSkuByCode(skuCode);
        if (productSkuDO == null) {
            return null;
        }
        return productConverter.convert(productSkuDO);
    }

    @Override
    public List<ProductSkuDTO> listProductSkuByCode(List<String> skuCodeList) {
        ParamCheckUtil.checkCollectionNonEmpty(skuCodeList, ProductErrorCodeEnum.SKU_CODE_IS_NULL);
        List<ProductSkuDO> productSkuDOList = productSkuDAO.listProductSkuByCode(skuCodeList);
        return productConverter.convert(productSkuDOList);
    }
}