package com.ruyuan.eshop.order.remote;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.risk.api.RiskApi;
import com.ruyuan.eshop.risk.domain.dto.CheckOrderRiskDTO;
import com.ruyuan.eshop.risk.domain.request.CheckOrderRiskRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * 营销服务远程接口
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class RiskRemote {

    /**
     * 风控服务
     */
    @DubboReference(version = "1.0.0", retries = 0)
    private RiskApi riskApi;

    /**
     * 订单风控检查
     * @param checkOrderRiskRequest
     * @return
     */
    public CheckOrderRiskDTO checkOrderRisk(CheckOrderRiskRequest checkOrderRiskRequest) {
        JsonResult<CheckOrderRiskDTO> jsonResult = riskApi.checkOrderRisk(checkOrderRiskRequest);
        if (!jsonResult.getSuccess()) {
            throw new OrderBizException(jsonResult.getErrorCode(), jsonResult.getErrorMessage());
        }
        return jsonResult.getData();
    }

}