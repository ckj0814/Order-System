package com.ruyuan.eshop.risk.api;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.risk.domain.dto.CheckOrderRiskDTO;
import com.ruyuan.eshop.risk.domain.request.CheckOrderRiskRequest;

/**
 * 风控服务API
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface RiskApi {

    /**
     * 订单风控检查
     *
     * @param checkOrderRiskRequest
     * @return
     */
    JsonResult<CheckOrderRiskDTO> checkOrderRisk(CheckOrderRiskRequest checkOrderRiskRequest);

}