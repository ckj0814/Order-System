package com.ruyuan.eshop.order.remote;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.product.api.ProductApi;
import com.ruyuan.eshop.product.domain.dto.ProductSkuDTO;
import com.ruyuan.eshop.product.domain.query.ProductSkuQuery;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 商品服务远程接口
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class ProductRemote {

    /**
     * 商品服务
     */
    @DubboReference(version = "1.0.0")
    private ProductApi productApi;

    /**
     * 查询商品信息
     * @param skuCode
     * @param sellerId
     * @return
     */
    public ProductSkuDTO getProductSku(String skuCode, String sellerId) {
        ProductSkuQuery productSkuQuery = new ProductSkuQuery();
        productSkuQuery.setSkuCodeList(Collections.singletonList(skuCode));
        productSkuQuery.setSellerId(sellerId);
        JsonResult<ProductSkuDTO> jsonResult = productApi.getProductSku(productSkuQuery);
        if (!jsonResult.getSuccess()) {
            throw new OrderBizException(jsonResult.getErrorCode(), jsonResult.getErrorMessage());
        }
        return jsonResult.getData();
    }

    /**
     * 查询商品信息
     * @param skuCodeList
     * @param sellerId
     * @return
     */
    public List<ProductSkuDTO> listProductSku(List<String> skuCodeList, String sellerId) {
        ProductSkuQuery productSkuQuery = new ProductSkuQuery();
        productSkuQuery.setSkuCodeList(skuCodeList);
        productSkuQuery.setSellerId(sellerId);
        JsonResult<List<ProductSkuDTO>> jsonResult = productApi.listProductSku(productSkuQuery);
        if (!jsonResult.getSuccess()) {
            throw new OrderBizException(jsonResult.getErrorCode(), jsonResult.getErrorMessage());
        }
        return jsonResult.getData();
    }

}