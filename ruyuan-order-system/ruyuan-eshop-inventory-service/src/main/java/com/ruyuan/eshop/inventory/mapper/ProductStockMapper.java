package com.ruyuan.eshop.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruyuan.eshop.inventory.domain.entity.ProductStockDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 库存中心的商品库存表 Mapper 接口
 * </p>
 *
 * @author zhonghuashishan
 */
@Mapper
public interface ProductStockMapper extends BaseMapper<ProductStockDO> {

    /**
     * 扣减商品库存
     *
     * @param skuCode
     * @param saleQuantity
     * @return
     */
    int deductProductStock(@Param("skuCode") String skuCode, @Param("saleQuantity") Integer saleQuantity);

    /**
     * 扣减销售库存
     *
     * @param skuCode
     * @param saleQuantity
     * @param originSaleStock
     * @return
     */
    int deductSaleStock(@Param("skuCode") String skuCode, @Param("saleQuantity") Integer saleQuantity
            , @Param("originSaleStock") Integer originSaleStock);

    /**
     * 增加销售库存
     *
     * @param skuCode
     * @param saleQuantity
     * @param originSaledStock
     * @return
     */
    int increaseSaledStock(@Param("skuCode") String skuCode, @Param("saleQuantity") Integer saleQuantity
            , @Param("originSaledStock") Integer originSaledStock);

    /**
     * 还原销售库存
     *
     * @param skuCode
     * @param saleQuantity
     * @param originSaleStock
     * @return
     */
    int restoreSaleStock(@Param("skuCode") String skuCode, @Param("saleQuantity") Integer saleQuantity,
                         @Param("originSaleStock") Integer originSaleStock);

    /**
     * 释放商品库存
     *
     * @param skuCode
     * @param saleQuantity
     * @return
     */
    int releaseProductStock(@Param("skuCode") String skuCode, @Param("saleQuantity") Integer saleQuantity);

    /**
     * 调整商品库存
     *
     * @param skuCode
     * @param originSaleQuantity
     * @param saleIncremental
     * @return
     */
    int modifyProductStock(@Param("skuCode") String skuCode, @Param("originSaleQuantity") Long originSaleQuantity, @Param("saleIncremental") Long saleIncremental);

}
