package com.ruyuan.eshop.inventory.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.constants.CoreConstant;
import com.ruyuan.eshop.common.constants.RedisLockKeyConstants;
import com.ruyuan.eshop.common.redis.RedisCache;
import com.ruyuan.eshop.common.redis.RedisLock;
import com.ruyuan.eshop.common.utils.LoggerFormat;
import com.ruyuan.eshop.common.utils.ParamCheckUtil;
import com.ruyuan.eshop.inventory.cache.CacheSupport;
import com.ruyuan.eshop.inventory.dao.ProductStockDAO;
import com.ruyuan.eshop.inventory.dao.ProductStockLogDAO;
import com.ruyuan.eshop.inventory.domain.dto.DeductStockDTO;
import com.ruyuan.eshop.inventory.domain.entity.ProductStockDO;
import com.ruyuan.eshop.inventory.domain.entity.ProductStockLogDO;
import com.ruyuan.eshop.inventory.domain.request.*;
import com.ruyuan.eshop.inventory.enums.StockLogStatusEnum;
import com.ruyuan.eshop.inventory.exception.InventoryBizException;
import com.ruyuan.eshop.inventory.exception.InventoryErrorCodeEnum;
import com.ruyuan.eshop.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private ProductStockDAO productStockDAO;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AddProductStockProcessor addProductStockProcessor;

    @Autowired
    private ModifyProductStockProcessor modifyProductStockProcessor;

    @Autowired
    private ProductStockLogDAO productStockLogDAO;

    @Autowired
    private DeductProductStockProcessor deductProductStockProcessor;

    @Autowired
    private ReleaseProductStockProcessor releaseProductStockProcessor;

    @Autowired
    private SyncStockToCacheProcessor syncStockToCacheProcessor;

    /**
     * 扣减商品库存
     *
     * @param deductProductStockRequest
     * @return
     */
    @Override
    public Boolean deductProductStock(DeductProductStockRequest deductProductStockRequest) {
        // 检查入参
        checkLockProductStockRequest(deductProductStockRequest);
        String orderId = deductProductStockRequest.getOrderId();
        List<DeductProductStockRequest.OrderItemRequest> orderItemRequestList =
                deductProductStockRequest.getOrderItemRequestList();
        for (DeductProductStockRequest.OrderItemRequest orderItemRequest : orderItemRequestList) {
            String skuCode = orderItemRequest.getSkuCode();
            //1、查询mysql库存数据
            ProductStockDO productStockDO = productStockDAO.getBySkuCode(skuCode);
            log.info(LoggerFormat.build()
                    .remark("查询mysql库存数据")
                    .data("productStockDO", productStockDO)
                    .finish());
            if (productStockDO == null) {
                log.error(LoggerFormat.build()
                        .remark("商品库存记录不存在")
                        .data("skuCode", skuCode)
                        .finish());
                throw new InventoryBizException(InventoryErrorCodeEnum.PRODUCT_SKU_STOCK_NOT_FOUND_ERROR);
            }

            //2、查询redis库存数据
            String productStockKey = CacheSupport.buildProductStockKey(skuCode);
            Map<String, String> productStockValue = redisCache.hGetAll(productStockKey);
            if (productStockValue.isEmpty()) {
                // 如果查询不到redis库存数据，将mysql库存数据放入redis，以mysql的数据为准
                addProductStockProcessor.addStockToRedis(productStockDO);
            }

            //3、添加redis锁，防同一笔订单重复扣库存
            String lockKey = MessageFormat.format(RedisLockKeyConstants.ORDER_DEDUCT_PRODUCT_STOCK_KEY, orderId, skuCode);
            Boolean locked = redisLock.tryLock(lockKey);
            if (!locked) {
                log.error(LoggerFormat.build()
                        .remark("无法获取扣减库存锁")
                        .data("orderId", orderId)
                        .data("skuCode", skuCode)
                        .finish());
                throw new InventoryBizException(InventoryErrorCodeEnum.DEDUCT_PRODUCT_SKU_STOCK_CANNOT_ACQUIRE);
            }
            try {
                //4、查询库存扣减日志
                ProductStockLogDO productStockLog = productStockLogDAO.getLog(orderId, skuCode);
                if (null != productStockLog) {
                    log.info("已扣减过，扣减库存日志已存在,orderId={},skuCode={}", orderId, skuCode);
                    return true;
                }

                Integer saleQuantity = orderItemRequest.getSaleQuantity();
                Integer originSaleStock = productStockDO.getSaleStockQuantity().intValue();
                Integer originSaledStock = productStockDO.getSaledStockQuantity().intValue();

                //5、执行执库存扣减
                DeductStockDTO deductStock = new DeductStockDTO(orderId, skuCode, saleQuantity, originSaleStock, originSaledStock);
                deductProductStockProcessor.doDeduct(deductStock);
            } finally {
                redisLock.unlock(lockKey);
            }
        }
        log.info(LoggerFormat.build()
                .remark("deductProductStock->response")
                .finish());
        return true;
    }


    /**
     * 检查锁定商品库存入参
     *
     * @param deductProductStockRequest
     */
    private void checkLockProductStockRequest(DeductProductStockRequest deductProductStockRequest) {
        String orderId = deductProductStockRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId);
        List<DeductProductStockRequest.OrderItemRequest> orderItemRequestList =
                deductProductStockRequest.getOrderItemRequestList();
        ParamCheckUtil.checkCollectionNonEmpty(orderItemRequestList);
    }

    /**
     * 释放商品库存
     */
    @Override
    public Boolean releaseProductStock(ReleaseProductStockRequest releaseProductStockRequest) {
        // 检查入参
        checkReleaseProductStockRequest(releaseProductStockRequest);
        String orderId = releaseProductStockRequest.getOrderId();
        List<ReleaseProductStockRequest.OrderItemRequest> orderItemRequestList =
                releaseProductStockRequest.getOrderItemRequestList();
        for (ReleaseProductStockRequest.OrderItemRequest orderItemRequest : orderItemRequestList) {
            String skuCode = orderItemRequest.getSkuCode();

            //1、添加redis释放库存锁，作用
            // (1)防同一笔订单重复释放
            // (2)重量级锁，保证mysql+redis扣库存的原子性，同一时间只能有一个订单来释放，
            //    需要锁查询+扣库存
            String lockKey = RedisLockKeyConstants.RELEASE_PRODUCT_STOCK_KEY + skuCode;
            // 获取不到锁，等待3s
            Boolean locked = redisLock.tryLock(lockKey, CoreConstant.DEFAULT_WAIT_SECONDS);
            if (!locked) {
                log.error("无法获取释放库存锁,orderId={},skuCode={}", orderId, skuCode);
                throw new InventoryBizException(InventoryErrorCodeEnum.RELEASE_PRODUCT_SKU_STOCK_LOCK_CANNOT_ACQUIRE);
            }

            try {
                //2、查询mysql库存数据
                ProductStockDO productStockDO = productStockDAO.getBySkuCode(skuCode);
                if (productStockDO == null) {
                    throw new InventoryBizException(InventoryErrorCodeEnum.PRODUCT_SKU_STOCK_NOT_FOUND_ERROR);
                }

                //3、查询redis库存数据
                String productStockKey = CacheSupport.buildProductStockKey(skuCode);
                Map<String, String> productStockValue = redisCache.hGetAll(productStockKey);
                if (productStockValue.isEmpty()) {
                    // 如果查询不到redis库存数据，将mysql库存数据放入redis，以mysql的数据为准
                    addProductStockProcessor.addStockToRedis(productStockDO);
                }

                Integer saleQuantity = orderItemRequest.getSaleQuantity();

                //4、校验是否释放过库存
                ProductStockLogDO productStockLog = productStockLogDAO.getLog(orderId, skuCode);
                if (null != productStockLog && productStockLog.getStatus().equals(StockLogStatusEnum.RELEASED.getCode())) {
                    log.info("已释放过库存,orderId={},skuCode={}", orderId, skuCode);
                    return true;
                }

                //5、释放库存
                releaseProductStockProcessor.doRelease(orderId, skuCode, saleQuantity, productStockLog);

            } finally {
                redisLock.unlock(lockKey);
            }
        }
        return true;
    }

    @Override
    public Boolean addProductStock(AddProductStockRequest request) {
        log.info("新增商品库存：request={}", JSONObject.toJSONString(request));
        //1、校验入参
        checkAddProductStockRequest(request);

        //2、查询商品库存
        ProductStockDO productStock = productStockDAO.getBySkuCode(request.getSkuCode());
        ParamCheckUtil.checkObjectNull(productStock, InventoryErrorCodeEnum.PRODUCT_SKU_STOCK_EXISTED_ERROR);

        //3、添加redis锁，防并发
        String lockKey = RedisLockKeyConstants.ADD_PRODUCT_STOCK_KEY + request.getSkuCode();
        Boolean locked = redisLock.tryLock(lockKey);
        if (!locked) {
            throw new InventoryBizException(InventoryErrorCodeEnum.ADD_PRODUCT_SKU_STOCK_ERROR);
        }
        try {
            //4、执行添加商品库存逻辑
            addProductStockProcessor.doAdd(request);
        } finally {
            //5、解锁
            redisLock.unlock(lockKey);
        }

        return true;
    }


    @Override
    public Boolean modifyProductStock(ModifyProductStockRequest request) {
        log.info("调整商品库存：request={}", JSONObject.toJSONString(request));

        //1、校验入参
        checkModifyProductStockRequest(request);

        //2、查询商品库存
        ProductStockDO productStock = productStockDAO.getBySkuCode(request.getSkuCode());
        ParamCheckUtil.checkObjectNonNull(productStock, InventoryErrorCodeEnum.PRODUCT_SKU_STOCK_NOT_FOUND_ERROR);

        //3、校验库存被扣减后是否小于0
        Long stockIncremental = request.getStockIncremental();
        Long saleStockQuantity = productStock.getSaleStockQuantity();
        if (saleStockQuantity + stockIncremental < 0) {
            throw new InventoryBizException(InventoryErrorCodeEnum.SALE_STOCK_QUANTITY_CANNOT_BE_NEGATIVE_NUMBER);
        }

        //4、加分布式锁，保证mysql和redis数据的一致性
        String lockKey = RedisLockKeyConstants.MODIFY_PRODUCT_STOCK_KEY + request.getSkuCode();
        Boolean locked = redisLock.tryLock(lockKey);
        if (!locked) {
            throw new InventoryBizException(InventoryErrorCodeEnum.MODIFY_PRODUCT_SKU_STOCK_ERROR);
        }

        try {
            modifyProductStockProcessor.doModify(productStock, stockIncremental);
        } finally {
            redisLock.unlock(lockKey);
        }

        return true;
    }

    @Override
    public Boolean syncStockToCache(SyncStockToCacheRequest request) {
        //1、校验参数
        ParamCheckUtil.checkStringNonEmpty(request.getSkuCode(), InventoryErrorCodeEnum.SKU_CODE_IS_EMPTY);

        //2、同步
        syncStockToCacheProcessor.doSync(request.getSkuCode());

        return true;
    }

    /**
     * 检查释放商品库存入参
     *
     * @param releaseProductStockRequest
     */
    private void checkReleaseProductStockRequest(ReleaseProductStockRequest releaseProductStockRequest) {
        String orderId = releaseProductStockRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId);

        List<ReleaseProductStockRequest.OrderItemRequest> orderItemRequestList =
                releaseProductStockRequest.getOrderItemRequestList();
        ParamCheckUtil.checkCollectionNonEmpty(orderItemRequestList);
    }

    /**
     * 校验添加商品库存入参
     *
     * @param request
     */
    private void checkAddProductStockRequest(AddProductStockRequest request) {
        ParamCheckUtil.checkStringNonEmpty(request.getSkuCode(), InventoryErrorCodeEnum.SKU_CODE_IS_EMPTY);
        ParamCheckUtil.checkObjectNonNull(request.getSaleStockQuantity(), InventoryErrorCodeEnum.SALE_STOCK_QUANTITY_IS_EMPTY);
        ParamCheckUtil.checkLongMin(request.getSaleStockQuantity(), 0L, InventoryErrorCodeEnum.SALE_STOCK_QUANTITY_CANNOT_BE_NEGATIVE_NUMBER);
    }

    /**
     * 校验调整商品库存入参
     *
     * @param request
     */
    private void checkModifyProductStockRequest(ModifyProductStockRequest request) {
        ParamCheckUtil.checkStringNonEmpty(request.getSkuCode(), InventoryErrorCodeEnum.SKU_CODE_IS_EMPTY);
        ParamCheckUtil.checkObjectNonNull(request.getStockIncremental(), InventoryErrorCodeEnum.SALE_STOCK_INCREMENTAL_QUANTITY_IS_EMPTY);
        if (0L == request.getStockIncremental()) {
            throw new InventoryBizException(InventoryErrorCodeEnum.SALE_STOCK_INCREMENTAL_QUANTITY_CANNOT_BE_ZERO);
        }
    }

}
