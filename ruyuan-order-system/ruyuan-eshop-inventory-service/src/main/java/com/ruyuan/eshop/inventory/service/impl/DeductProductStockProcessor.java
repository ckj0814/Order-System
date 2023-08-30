package com.ruyuan.eshop.inventory.service.impl;

import com.ruyuan.eshop.common.utils.LoggerFormat;
import com.ruyuan.eshop.common.utils.MdcUtil;
import com.ruyuan.eshop.inventory.dao.ProductStockLogDAO;
import com.ruyuan.eshop.inventory.domain.dto.DeductStockDTO;
import com.ruyuan.eshop.inventory.exception.InventoryBizException;
import com.ruyuan.eshop.inventory.exception.InventoryErrorCodeEnum;
import com.ruyuan.eshop.inventory.tcc.LockMysqlStockTccService;
import com.ruyuan.eshop.inventory.tcc.LockRedisStockTccService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 扣减商品库存处理器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class DeductProductStockProcessor {

    @Autowired
    private LockMysqlStockTccService lockMysqlStockTccService;

    @Autowired
    private LockRedisStockTccService lockRedisStockTccService;

    @Autowired
    private ProductStockLogDAO productStockLogDAO;

    @Autowired
    private SyncStockToCacheProcessor syncStockToCacheProcessor;

    /**
     * 执行扣减商品库存逻辑
     */
//    @GlobalTransactional(rollbackFor = Exception.class)
    public void doDeduct(DeductStockDTO deductStock) {
        String traceId = MdcUtil.getTraceId();
        // 1、执行执行mysql库存扣减
        boolean result = lockMysqlStockTccService
                .deductStock(null, deductStock, traceId);
        if (!result) {
            throw new InventoryBizException(InventoryErrorCodeEnum.DEDUCT_PRODUCT_SKU_STOCK_ERROR);
        }

        // 2、执行redis库存扣减
        result = lockRedisStockTccService.deductStock(null, deductStock, traceId);
        if (!result) {
            // 已mysql数据为准
            log.info(LoggerFormat.build()
                    .remark("执行redis库存扣减失败！")
                    .data("deductStock", deductStock)
                    .finish());
            syncStockToCacheProcessor.doSync(deductStock.getSkuCode());
        }
    }
}
