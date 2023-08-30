package com.ruyuan.eshop.order.batch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruyuan.eshop.order.batch.service.OrderInfoBatchService;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import com.ruyuan.eshop.order.mapper.OrderInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
public class OrderInfoBatchServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfoDO> implements OrderInfoBatchService {

}
