package com.ruyuan.eshop.risk.api.impl;

import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.common.utils.LoggerFormat;
import com.ruyuan.eshop.risk.api.RiskApi;
import com.ruyuan.eshop.risk.domain.dto.CheckOrderRiskDTO;
import com.ruyuan.eshop.risk.domain.request.CheckOrderRiskRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@DubboService(version = "1.0.0", interfaceClass = RiskApi.class)
public class RiskApiImpl implements RiskApi {

    @Override
    public JsonResult<CheckOrderRiskDTO> checkOrderRisk(CheckOrderRiskRequest checkOrderRiskRequest) {
        log.info(LoggerFormat.build()
                .remark("checkOrderRisk->request")
                .data("request", checkOrderRiskRequest)
                .finish());
        // 执行风控检查 TODO
        CheckOrderRiskDTO checkOrderRiskDTO = new CheckOrderRiskDTO();
        checkOrderRiskDTO.setResult(true);
        // 默认风控检查通过
        log.info(LoggerFormat.build()
                .remark("checkOrderRisk->response")
                .data("response", checkOrderRiskDTO)
                .finish());
        return JsonResult.buildSuccess(checkOrderRiskDTO);
    }
}