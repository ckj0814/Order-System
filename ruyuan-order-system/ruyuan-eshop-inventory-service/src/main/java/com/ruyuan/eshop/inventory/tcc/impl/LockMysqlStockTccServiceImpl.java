package com.ruyuan.eshop.inventory.tcc.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.utils.LoggerFormat;
import com.ruyuan.eshop.common.utils.MdcUtil;
import com.ruyuan.eshop.inventory.dao.ProductStockDAO;
import com.ruyuan.eshop.inventory.dao.ProductStockLogDAO;
import com.ruyuan.eshop.inventory.domain.dto.DeductStockDTO;
import com.ruyuan.eshop.inventory.domain.entity.ProductStockLogDO;
import com.ruyuan.eshop.inventory.tcc.LockMysqlStockTccService;
import com.ruyuan.eshop.inventory.tcc.TccResultHolder;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class LockMysqlStockTccServiceImpl implements LockMysqlStockTccService {

    @Autowired
    private ProductStockDAO productStockDAO;

    @Autowired
    private ProductStockLogDAO productStockLogDAO;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deductStock(BusinessActionContext actionContext, DeductStockDTO deductStock, String traceId) {
        String xid = actionContext.getXid();
        String skuCode = deductStock.getSkuCode();
        Integer saleQuantity = deductStock.getSaleQuantity();
        Integer originSaleStock = deductStock.getOriginSaleStock();
        Integer originSaledStock = deductStock.getOriginSaledStock();

        //标识try阶段开始执行
        TccResultHolder.tagTryStart(getClass(), skuCode, xid);

        //悬挂问题：rollback接口比try接口先执行，即rollback接口进行了空回滚，try接口才执行，导致try接口预留的资源无法被取消
        //解决空悬挂的思路：即当rollback接口出现空回滚时，需要打一个标识（在数据库中查一条记录），在try这里判断一下
        if (isEmptyRollback()) {
            return false;
        }

        log.info(LoggerFormat.build()
                .remark("一阶段方法：扣减mysql销售库存")
                .data("deductStock", deductStock)
                .data("xid", xid)
                .finish());
        int result = productStockDAO.deductSaleStock(skuCode, saleQuantity, originSaleStock);

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
                .remark("二阶段方法：增加mysql已销售库存")
                .data("deductStock", deductStock)
                .data("xid", xid)
                .finish());

        //幂等
        // 当出现网络异常或者TC Server异常时，会出现重复调用commit阶段的情况，所以需要进行幂等操作
        if (!TccResultHolder.isTrySuccess(getClass(), skuCode, xid)) {
            return;
        }
        //1、增加已销售库存
        productStockDAO.increaseSaledStock(skuCode, saleQuantity, originSaledStock);
        //2、插入一条扣减日志表
        log.info(LoggerFormat.build()
                .remark("插入一条扣减日志表")
                .finish());
        productStockLogDAO.save(buildStockLog(deductStock));

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
                .remark("回滚：增加mysql销售库存")
                .data("deductStock", deductStock)
                .data("xid", xid)
                .finish());

        //空回滚处理
        if (TccResultHolder.isTagNull(getClass(), skuCode, xid)) {
            log.error(LoggerFormat.build()
                    .remark("mysql:出现空回滚")
                    .data("deductStock", deductStock)
                    .data("xid", xid)
                    .finish());
            insertEmptyRollbackTag();
            return;
        }

        //幂等处理
        //try阶段没有完成的情况下，不必执行回滚，因为try阶段有本地事务，事务失败时已经进行了回滚
        //如果try阶段成功，而其他全局事务参与者失败，这里会执行回滚
        if (!TccResultHolder.isTrySuccess(getClass(), skuCode, xid)) {
            log.info(LoggerFormat.build()
                    .remark("mysql:无需回滚")
                    .data("deductStock", deductStock)
                    .data("xid", xid)
                    .finish());
            return;
        }

        //1、还原销售库存
        productStockDAO.restoreSaleStock(skuCode, saleQuantity, originSaleStock - saleQuantity);
        //2、删除库存扣减日志
        ProductStockLogDO logDO = productStockLogDAO.getLog(deductStock.getOrderId(), skuCode);
        if (null != logDO) {
            productStockLogDAO.removeById(logDO.getId());
        }

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

    /**
     * 构建扣减库存日志
     *
     * @param deductStock
     * @return
     */
    private ProductStockLogDO buildStockLog(DeductStockDTO deductStock) {
        ProductStockLogDO logDO = new ProductStockLogDO();
        logDO.setOrderId(deductStock.getOrderId());
        logDO.setSkuCode(deductStock.getSkuCode());
        logDO.setOriginSaleStockQuantity(deductStock.getOriginSaleStock().longValue());
        logDO.setOriginSaledStockQuantity(deductStock.getOriginSaledStock().longValue());
        logDO.setDeductedSaleStockQuantity(deductStock.getOriginSaleStock().longValue()
                - deductStock.getSaleQuantity().longValue());
        logDO.setIncreasedSaledStockQuantity(deductStock.getOriginSaledStock().longValue()
                + deductStock.getSaleQuantity().longValue());
        return logDO;
    }
}
