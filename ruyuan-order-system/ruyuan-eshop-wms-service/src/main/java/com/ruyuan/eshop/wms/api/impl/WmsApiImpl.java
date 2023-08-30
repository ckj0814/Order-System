package com.ruyuan.eshop.wms.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.common.utils.RandomUtil;
import com.ruyuan.eshop.wms.api.WmsApi;
import com.ruyuan.eshop.wms.converter.WmsConverter;
import com.ruyuan.eshop.wms.dao.DeliveryOrderDAO;
import com.ruyuan.eshop.wms.dao.DeliveryOrderItemDAO;
import com.ruyuan.eshop.wms.domain.PickDTO;
import com.ruyuan.eshop.wms.domain.PickGoodsRequest;
import com.ruyuan.eshop.wms.domain.dto.ScheduleDeliveryResult;
import com.ruyuan.eshop.wms.domain.entity.DeliveryOrderDO;
import com.ruyuan.eshop.wms.domain.entity.DeliveryOrderItemDO;
import com.ruyuan.eshop.wms.exception.WmsBizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhonghuashishan
 * @version 1.0
 */

@Slf4j
@DubboService(version = "1.0.0", interfaceClass = WmsApi.class, retries = 0)
public class WmsApiImpl implements WmsApi {

    @Autowired
    private DeliveryOrderDAO deliveryOrderDAO;

    @Autowired
    private DeliveryOrderItemDAO deliveryOrderItemDAO;

    @Resource
    private WmsConverter wmsConverter;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult<PickDTO> pickGoods(PickGoodsRequest request) {
        log.info("捡货,orderId={},request={}", request.getOrderId(), JSONObject.toJSONString(request));

        String wmsException = request.getWmsException();
        if (StringUtils.isNotBlank(wmsException) && wmsException.equals("true")) {
            throw new WmsBizException("捡货异常！");
        }

        //1、捡货，调度出库
        ScheduleDeliveryResult result = scheduleDelivery(request);

        //2、存储出库单和出库单条目
        deliveryOrderDAO.save(result.getDeliveryOrder());
        deliveryOrderItemDAO.saveBatch(result.getDeliveryOrderItems());

        //3、构造返回参数
        return JsonResult.buildSuccess(new PickDTO(request.getOrderId()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult<Boolean> cancelPickGoods(String orderId) {
        log.info("取消捡货,orderId={}", orderId);

        // 1、查询出库单
        List<DeliveryOrderDO> deliveryOrders = deliveryOrderDAO.listByOrderId(orderId);

        if (CollectionUtils.isNotEmpty(deliveryOrders)) {
            // 2、移除出库单
            List<Long> ids = deliveryOrders.stream().map(DeliveryOrderDO::getId).collect(Collectors.toList());
            deliveryOrderDAO.removeByIds(ids);


            // 3、移除条目
            List<String> deliveryOrderIds = deliveryOrders.stream()
                    .map(DeliveryOrderDO::getDeliveryOrderId).collect(Collectors.toList());
            List<DeliveryOrderItemDO> items = deliveryOrderItemDAO
                    .listByDeliveryOrderIds(deliveryOrderIds);
            if(CollectionUtils.isNotEmpty(items)) {
                ids = items.stream().map(DeliveryOrderItemDO::getId).collect(Collectors.toList());
                deliveryOrderItemDAO.removeByIds(ids);
            }
        }

        return JsonResult.buildSuccess(true);
    }



    /**
     * 调度出库
     *
     * @param request
     */
    private ScheduleDeliveryResult scheduleDelivery(PickGoodsRequest request) {
        log.info("orderId={}的订单进行调度出库", request.getOrderId());

        //1、生成出库单ID
        String deliveryOrderId = genDeliveryOrderId();

        //2、生成出库单
        DeliveryOrderDO deliveryOrder = wmsConverter.convertDeliverOrderDO(request);
        deliveryOrder.setDeliveryOrderId(deliveryOrderId);

        //3、生成出库单条目
        List<DeliveryOrderItemDO> deliveryOrderItems = wmsConverter.convertDeliverOrderItemDO(request.getOrderItems());
        for (DeliveryOrderItemDO item : deliveryOrderItems) {
            item.setDeliveryOrderId(deliveryOrderId);
        }

        //4、sku调度出库
        // 这里仅仅只是模拟，假设有一个无限货物的仓库货柜(id = 1)
        for (DeliveryOrderItemDO item : deliveryOrderItems) {
            item.setPickingCount(item.getSaleQuantity());
            item.setSkuContainerId(1);
        }
        return new ScheduleDeliveryResult(deliveryOrder, deliveryOrderItems);
    }

    /**
     * 生成履约单id
     *
     * @return
     */
    private String genDeliveryOrderId() {
        return RandomUtil.genRandomNumber(10);
    }


}
