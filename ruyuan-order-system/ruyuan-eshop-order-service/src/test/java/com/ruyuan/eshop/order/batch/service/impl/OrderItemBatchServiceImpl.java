package com.ruyuan.eshop.order.batch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruyuan.eshop.order.batch.service.OrderItemBatchService;
import com.ruyuan.eshop.order.domain.entity.OrderItemDO;
import com.ruyuan.eshop.order.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
public class OrderItemBatchServiceImpl extends ServiceImpl<OrderItemMapper, OrderItemDO> implements OrderItemBatchService {

}
