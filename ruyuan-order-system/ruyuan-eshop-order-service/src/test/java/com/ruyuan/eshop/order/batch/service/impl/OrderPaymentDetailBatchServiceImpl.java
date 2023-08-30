package com.ruyuan.eshop.order.batch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruyuan.eshop.order.batch.service.OrderPaymentDetailBatchService;
import com.ruyuan.eshop.order.domain.entity.OrderPaymentDetailDO;
import com.ruyuan.eshop.order.mapper.OrderPaymentDetailMapper;
import org.springframework.stereotype.Service;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
public class OrderPaymentDetailBatchServiceImpl extends ServiceImpl<OrderPaymentDetailMapper, OrderPaymentDetailDO>
        implements OrderPaymentDetailBatchService {

}
