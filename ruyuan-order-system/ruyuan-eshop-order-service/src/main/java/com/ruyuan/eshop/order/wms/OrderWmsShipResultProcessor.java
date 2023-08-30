package com.ruyuan.eshop.order.wms;


import com.ruyuan.eshop.order.domain.dto.WmsShipDTO;
import com.ruyuan.eshop.order.exception.OrderBizException;

/**
 * 订单物流配送结果处理器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface OrderWmsShipResultProcessor {

    /**
     * 执行具体的业务逻辑
     *
     * @throws OrderBizException
     */
    void execute(WmsShipDTO wmsShipDTO) throws OrderBizException;
}
