package com.ruyuan.eshop.inventory.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 库存缓存key-value support
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface CacheSupport {

    String PREFIX_PRODUCT_STOCK = "PRODUCT_STOCK:";

    /**
     * 可销售库存key
     */
    String SALE_STOCK = "saleStock";

    /**
     * 已销售库存key
     */
    String SALED_STOCK = "saledStock";

    /**
     * 构造缓存商品库存key
     *
     * @param skuCode
     * @return
     */
    static String buildProductStockKey(String skuCode) {
        return PREFIX_PRODUCT_STOCK + ":" + skuCode;
    }

    /**
     * 构造缓存商品库存value
     *
     * @param saleStockQuantity
     * @param saledStockQuantity
     * @return
     */
    static Map<String, String> buildProductStockValue(Long saleStockQuantity, Long saledStockQuantity) {
        Map<String, String> value = new HashMap<>();
        value.put(SALE_STOCK, String.valueOf(saleStockQuantity));
        value.put(SALED_STOCK, String.valueOf(saledStockQuantity));
        return value;
    }
}
