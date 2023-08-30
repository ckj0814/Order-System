package com.ruyuan.eshop.tms.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.common.utils.RandomUtil;
import com.ruyuan.eshop.tms.api.TmsApi;
import com.ruyuan.eshop.tms.converter.TmsConverter;
import com.ruyuan.eshop.tms.dao.LogisticOrderDAO;
import com.ruyuan.eshop.tms.domain.SendOutDTO;
import com.ruyuan.eshop.tms.domain.SendOutRequest;
import com.ruyuan.eshop.tms.domain.dto.PlaceLogisticOrderDTO;
import com.ruyuan.eshop.tms.domain.entity.LogisticOrderDO;
import com.ruyuan.eshop.tms.exception.TmsBizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@DubboService(version = "1.0.0", interfaceClass = TmsApi.class, retries = 0)
public class TmsApiImpl implements TmsApi {

    @Autowired
    private LogisticOrderDAO logisticOrderDAO;

    @Autowired
    private TmsConverter tmsConverter;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult<SendOutDTO> sendOut(SendOutRequest request) {
        log.info("发货,orderId={},request={}", request.getOrderId(), JSONObject.toJSONString(request));

        String tmsException = request.getTmsException();
        if (StringUtils.isNotBlank(tmsException) && tmsException.equals("true")) {
            throw new TmsBizException("发货异常！");
        }

        //1、调用三方物流系统，下物流单子单
        PlaceLogisticOrderDTO result = thirdPartyLogisticApi(request);

        //2、生成物流单
        LogisticOrderDO logisticOrder = tmsConverter.convertLogisticOrderDO(request);
        logisticOrder.setLogisticCode(result.getLogisticCode());
        logisticOrder.setContent(result.getContent());
        logisticOrderDAO.save(logisticOrder);

        return JsonResult.buildSuccess(new SendOutDTO(request.getOrderId(), result.getLogisticCode()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult<Boolean> cancelSendOut(String orderId) {
        log.info("取消发货,orderId={}", orderId);
        //1、查询物流单
        List<LogisticOrderDO> logisticOrders = logisticOrderDAO.listByOrderId(orderId);

        //2、移除物流单
        if(CollectionUtils.isNotEmpty(logisticOrders)) {
            List<Long> ids = logisticOrders.stream().map(LogisticOrderDO::getId).collect(Collectors.toList());
            logisticOrderDAO.removeByIds(ids);
        }

        return JsonResult.buildSuccess(true);
    }

    /**
     * 调用三方物流系统接口，下物流单子单
     *
     * @return
     */
    private PlaceLogisticOrderDTO thirdPartyLogisticApi(SendOutRequest request) {
        //模拟调用了第三方物流系统
        String logisticCode = RandomUtil.genRandomNumber(11);
        String content = "测试物流单内容";

        return new PlaceLogisticOrderDTO(logisticCode, content);
    }

}
