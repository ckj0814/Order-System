package com.ruyuan.eshop.inventory.cache;

/**
 * lua脚本
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface LuaScript {

    /**
     * 调整商品库存
     */
    String MODIFY_PRODUCT_STOCK =
            "local productStockKey = KEYS[1];"
                    + "local saleStockKey = KEYS[2];"
                    + "local originSaleStockQuantity = tonumber(ARGV[1]);"
                    + "local stockIncremental = tonumber(ARGV[2]);"
                    + "local saleStock = tonumber(redis.call('hget', productStockKey, saleStockKey));"
                    + "if saleStock ~= originSaleStockQuantity then"
                    + "   return -1;"
                    + "end;"
                    + "if (saleStock+stockIncremental) < 0 then "
                    + "   return -1;"
                    + "end;"
                    + "redis.call('hset', productStockKey, saleStockKey, saleStock + stockIncremental);"
                    + "return 1;";

    /**
     * 扣减商品库存
     */
    String DEDUCT_PRODUCT_STOCK =
            "local productStockKey = KEYS[1];"
                    + "local saleStockKey = KEYS[2];"
                    + "local saledStockKey = KEYS[3];"
                    + "local saleQuantity = tonumber(ARGV[1]);"
                    + "local saleStock   = tonumber(redis.call('hget', productStockKey, saleStockKey));"
                    + "local saledStock = tonumber(redis.call('hget', productStockKey, saledStockKey));"
                    + "if saleStock < saleQuantity then"
                    + "   return -1;"
                    + "end;"
                    + "redis.call('hset', productStockKey, saleStockKey,   saleStock - saleQuantity);"
                    + "redis.call('hset', productStockKey, saledStockKey, saledStock + saleQuantity);"
                    + "return 1;";

    /**
     * 扣减销售库存
     */
    String DEDUCT_SALE_STOCK =
            "local productStockKey = KEYS[1];"
                    + "local saleStockKey = KEYS[2];"
                    + "local saleQuantity = tonumber(ARGV[1]);"
                    + "local originSaleStock = tonumber(ARGV[2]);"
                    + "local saleStock   = tonumber(redis.call('hget', productStockKey, saleStockKey));"
                    + "if saleStock < saleQuantity then"
                    + "   return -1;"
                    + "end;"
                    + "if saleStock ~= originSaleStock then"
                    + "   return -1;"
                    + "end;"
                    + "redis.call('hset', productStockKey, saleStockKey,   saleStock - saleQuantity);"
                    + "return 1;";

    /**
     * 增加已销售库存
     */
    String INCREASE_SALED_STOCK =
            "local productStockKey = KEYS[1];"
                    + "local saledStockKey = KEYS[2];"
                    + "local saleQuantity = tonumber(ARGV[1]);"
                    + "local originSaledStock = tonumber(ARGV[2]);"
                    + "local saledStock = tonumber(redis.call('hget', productStockKey, saledStockKey));"
                    + "if saledStock ~= originSaledStock then"
                    + "   return -1;"
                    + "end;"
                    + "redis.call('hset', productStockKey, saledStockKey, saledStock + saleQuantity);"
                    + "return 1;";

    /**
     * 还原销售库存
     */
    String RESTORE_SALE_STOCK =
            "local productStockKey = KEYS[1];"
                    + "local saleStockKey = KEYS[2];"
                    + "local saleQuantity = tonumber(ARGV[1]);"
                    + "local originSaleStock = tonumber(ARGV[2]);"
                    + "local saleStock   = tonumber(redis.call('hget', productStockKey, saleStockKey));"
                    + "if saleStock ~= originSaleStock then"
                    + "   return -1;"
                    + "end;"
                    + "redis.call('hset', productStockKey, saleStockKey,   saleStock + saleQuantity);"
                    + "return 1;";

    /**
     * 释放商品库存
     */
    String RELEASE_PRODUCT_STOCK =
            "local productStockKey = KEYS[1];"
                    + "local saleStockKey = KEYS[2];"
                    + "local saledStockKey = KEYS[3];"
                    + "local saleQuantity = tonumber(ARGV[1]);"
                    + "local saleStock   = tonumber(redis.call('hget', productStockKey, saleStockKey));"
                    + "local saledStock = tonumber(redis.call('hget', productStockKey, saledStockKey));"
                    + "if saledStock < saleQuantity then"
                    + "   return -1;"
                    + "end;"
                    + "redis.call('hset', productStockKey, saleStockKey,   saleStock + saleQuantity);"
                    + "redis.call('hset', productStockKey, saledStockKey, saledStock - saleQuantity);"
                    + "return 1;";
}
