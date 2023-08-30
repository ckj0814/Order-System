package com.ruyuan.eshop.inventory.tcc;

import com.ruyuan.eshop.inventory.domain.dto.DeductStockDTO;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * 锁定redis库存 Seata TCC模式 service
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@LocalTCC
public interface LockRedisStockTccService {

    /**
     * 一阶段方法：扣减销售库存（saleStockQuantity-saleQuantity）
     *
     * @param actionContext
     * @return
     */
    @TwoPhaseBusinessAction(name = "lockRedisStockTccService", commitMethod = "commit", rollbackMethod = "rollback")
    boolean deductStock(BusinessActionContext actionContext,
                        @BusinessActionContextParameter(paramName = "deductStock") DeductStockDTO deductStock,
                        @BusinessActionContextParameter(paramName = "traceId") String traceId);


    /**
     * 二阶段方法：增加已销售库存（saledStockQuantity+saleQuantity）
     *
     * @param actionContext
     * @return
     */
    void commit(BusinessActionContext actionContext);

    /**
     * 回滚：增加销售库存（saleStockQuantity+saleQuantity）
     *
     * @param actionContext
     * @return
     */
    void rollback(BusinessActionContext actionContext);

}
