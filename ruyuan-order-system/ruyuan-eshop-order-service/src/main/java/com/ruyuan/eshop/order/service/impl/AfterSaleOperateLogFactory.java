package com.ruyuan.eshop.order.service.impl;

import com.ruyuan.eshop.order.domain.entity.AfterSaleInfoDO;
import com.ruyuan.eshop.order.domain.entity.AfterSaleLogDO;
import com.ruyuan.eshop.order.enums.AfterSaleStatusChangeEnum;
import org.springframework.stereotype.Component;

/**
 * 售后单操作日志工厂
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class AfterSaleOperateLogFactory {

    /**
     * 获取售后操作日志
     */
    public AfterSaleLogDO get(AfterSaleInfoDO afterSaleInfo, AfterSaleStatusChangeEnum statusChange) {
        String operateRemark = statusChange.getOperateRemark();
        Integer preStatus = statusChange.getPreStatus().getCode();
        Integer currentStatus = statusChange.getCurrentStatus().getCode();
        return create(afterSaleInfo, preStatus, currentStatus, operateRemark);
    }

    /**
     * 创建售后单操作日志
     *
     * @param afterSaleInfo
     * @param preStatus
     * @param currentStatus
     * @param operateRemark
     * @return
     * @throws Exception
     */
    private AfterSaleLogDO create(AfterSaleInfoDO afterSaleInfo, int preStatus, int currentStatus, String operateRemark) {
        AfterSaleLogDO log = new AfterSaleLogDO();

        log.setAfterSaleId(String.valueOf(afterSaleInfo.getAfterSaleId()));
        log.setPreStatus(preStatus);
        log.setCurrentStatus(currentStatus);
        log.setRemark(operateRemark);

        return log;
    }


}
