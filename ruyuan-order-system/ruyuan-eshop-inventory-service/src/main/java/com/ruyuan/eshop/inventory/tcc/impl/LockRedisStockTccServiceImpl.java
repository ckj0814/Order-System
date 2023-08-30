package com.ruyuan.eshop.inventory.tcc.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.redis.RedisCache;
import com.ruyuan.eshop.common.utils.LoggerFormat;
import com.ruyuan.eshop.common.utils.MdcUtil;
import com.ruyuan.eshop.inventory.cache.CacheSupport;
import com.ruyuan.eshop.inventory.cache.LuaScript;
import com.ruyuan.eshop.inventory.domain.dto.DeductStockDTO;
import com.ruyuan.eshop.inventory.tcc.LockRedisStockTccService;
import com.ruyuan.eshop.inventory.tcc.TccResultHolder;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class LockRedisStockTccServiceImpl implements LockRedisStockTccService {

    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean deductStock(BusinessActionContext actionContext, DeductStockDTO deductStock, String traceId) {
        String xid = actionContext.getXid();
        String skuCode = deductStock.getSkuCode();
        Integer saleQuantity = deductStock.getSaleQuantity();
        Integer originSaleStock = deductStock.getOriginSaleStock();
        Integer originSaledStock = deductStock.getOriginSaledStock();

        //标识try阶段开始执行
        TccResultHolder.tagTryStart(getClass(), skuCode, xid);
        log.info(LoggerFormat.build()
                .remark("一阶段方法：扣减redis销售库存")
                .data("deductStock", deductStock)
                .data("xid", xid)
                .finish());

        //悬挂问题：rollback接口比try接口先执行，即rollback接口进行了空回滚，try接口才执行，导致try接口预留的资源无法被取消
        //解决空悬挂的思路：即当rollback接口出现空回滚时，需要打一个标识（在数据库中查一条记录），在try这里判断一下
        if (isEmptyRollback()) {
            return false;
        }

        String luaScript = LuaScript.DEDUCT_SALE_STOCK;
        String saleStockKey = CacheSupport.SALE_STOCK;
        String productStockKey = CacheSupport.buildProductStockKey(skuCode);
        Long result = redisCache.execute(new DefaultRedisScript<>(luaScript, Long.class),
                Arrays.asList(productStockKey, saleStockKey), String.valueOf(saleQuantity), String.valueOf(originSaleStock));

        //标识try阶段执行成功
        if (result > 0) {
            TccResultHolder.tagTrySuccess(getClass(), skuCode, xid);
        }

        return result > 0;
    }

    @Override
    public void commit(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        DeductStockDTO deductStock = ((JSONObject) actionContext.getActionContext("deductStock")).toJavaObject(DeductStockDTO.class);
        String traceId = (String) actionContext.getActionContext("traceId");
        MdcUtil.setUserTraceId(traceId);

        String skuCode = deductStock.getSkuCode();
        Integer saleQuantity = deductStock.getSaleQuantity();
        Integer originSaleStock = deductStock.getOriginSaleStock();
        Integer originSaledStock = deductStock.getOriginSaledStock();

        log.info(LoggerFormat.build()
                .remark("二阶段方法：增加redis已销售库存")
                .data("deductStock", deductStock)
                .data("xid", xid)
                .finish());
        //幂等
        // 当出现网络异常或者TC Server异常时，会出现重复调用commit阶段的情况，所以需要进行幂等操作
        if (!TccResultHolder.isTrySuccess(getClass(), skuCode, xid)) {
            log.info(LoggerFormat.build()
                    .remark("已经执行过commit阶段")
                    .data("deductStock", deductStock)
                    .data("xid", xid)
                    .finish());
            return;
        }

        String luaScript = LuaScript.INCREASE_SALED_STOCK;
        String saledStockKey = CacheSupport.SALED_STOCK;
        String productStockKey = CacheSupport.buildProductStockKey(skuCode);
        redisCache.execute(new DefaultRedisScript<>(luaScript, Long.class),
                Arrays.asList(productStockKey, saledStockKey), String.valueOf(saleQuantity), String.valueOf(originSaledStock));

        //移除标识
        TccResultHolder.removeResult(getClass(), skuCode, xid);
    }

    @Override
    public void rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        DeductStockDTO deductStock = ((JSONObject) actionContext.getActionContext("deductStock")).toJavaObject(DeductStockDTO.class);
        String traceId = (String) actionContext.getActionContext("traceId");
        MdcUtil.setUserTraceId(traceId);

        String skuCode = deductStock.getSkuCode();
        Integer saleQuantity = deductStock.getSaleQuantity();
        Integer originSaleStock = deductStock.getOriginSaleStock();
        Integer originSaledStock = deductStock.getOriginSaledStock();

        log.info(LoggerFormat.build()
                .remark("回滚：增加redis销售库存")
                .data("deductStock", deductStock)
                .data("xid", xid)
                .finish());

        //空回滚处理
        if (TccResultHolder.isTagNull(getClass(), skuCode, xid)) {
            log.info(LoggerFormat.build()
                    .remark("redis:出现空回滚")
                    .data("deductStock", deductStock)
                    .data("xid", xid)
                    .finish());
            insertEmptyRollbackTag();
            return;
        }

        //幂等处理
        //try阶段没有完成的情况下，不必执行回滚
        //如果try阶段成功，而其他全局事务参与者失败，这里会执行回滚
        if (!TccResultHolder.isTrySuccess(getClass(), skuCode, xid)) {
            log.info(LoggerFormat.build()
                    .remark("redis:无需回滚")
                    .data("deductStock", deductStock)
                    .data("xid", xid)
                    .finish());
            return;
        }


        String luaScript = LuaScript.RESTORE_SALE_STOCK;
        String saleStockKey = CacheSupport.SALE_STOCK;
        String productStockKey = CacheSupport.buildProductStockKey(skuCode);
        redisCache.execute(new DefaultRedisScript<>(luaScript, Long.class),
                Arrays.asList(productStockKey, saleStockKey), String.valueOf(saleQuantity), String.valueOf(originSaleStock - saleQuantity));

        //移除标识
        TccResultHolder.removeResult(getClass(), skuCode, xid);
    }

    /**
     * 判断是否发生的空回滚
     *
     * @return
     */
    private Boolean isEmptyRollback() {
        //需要查询本地数据库，看是否发生了空回滚
        return false;
    }

    /**
     * 插入空回滚标识
     */
    private void insertEmptyRollbackTag() {
        //在数据库插入空回滚的标识
    }

}
