package com.ruyuan.eshop.order.batch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruyuan.eshop.order.batch.service.OrderDeliveryDetailBatchService;
import com.ruyuan.eshop.order.domain.entity.OrderDeliveryDetailDO;
import com.ruyuan.eshop.order.mapper.OrderDeliveryDetailMapper;
import org.springframework.stereotype.Service;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
public class OrderDeliveryDetailBatchServiceImpl extends ServiceImpl<OrderDeliveryDetailMapper, OrderDeliveryDetailDO>
        implements OrderDeliveryDetailBatchService {

}
