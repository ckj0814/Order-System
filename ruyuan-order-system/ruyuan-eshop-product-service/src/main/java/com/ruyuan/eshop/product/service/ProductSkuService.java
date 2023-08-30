package com.ruyuan.eshop.product.service;

import com.ruyuan.eshop.product.domain.dto.ProductSkuDTO;

import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public interface ProductSkuService {

    /**
     * 根据skuCode获取商品sku信息
     *
     * @param skuCode
     * @return
     */
    ProductSkuDTO getProductSkuByCode(String skuCode);

    /**
     * 根据skuCode集合获取一批商品sku信息
     *
     * @param skuCodeList
     * @return
     */
    List<ProductSkuDTO> listProductSkuByCode(List<String> skuCodeList);

}