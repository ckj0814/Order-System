package com.ruyuan.eshop.product.controller;

import com.ruyuan.eshop.product.converter.ProductConverter;
import com.ruyuan.eshop.product.domain.dto.ProductSkuDTO;
import com.ruyuan.eshop.product.domain.vo.ProductSkuVO;
import com.ruyuan.eshop.product.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private ProductConverter productConverter;

    /**
     * 根据skuCode查询商品sku信息
     *
     * @param skuCode
     * @return
     */
    @GetMapping("/{skuCode}")
    public ProductSkuVO getProductSkuByCode(@PathVariable("skuCode") String skuCode) {
        ProductSkuDTO productSkuDTO = productSkuService.getProductSkuByCode(skuCode);
        if (productSkuDTO == null) {
            return null;
        }
        return productConverter.convert(productSkuDTO);
    }


}