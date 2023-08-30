package com.ruyuan.eshop.fulfill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ruyuan.eshop.common.bean.SpringApplicationContext;
import com.ruyuan.eshop.common.constants.RedisLockKeyConstants;
import com.ruyuan.eshop.common.redis.RedisLock;
import com.ruyuan.eshop.common.utils.RandomUtil;
import com.ruyuan.eshop.fulfill.builder.FulfillData;
import com.ruyuan.eshop.fulfill.converter.FulFillConverter;
import com.ruyuan.eshop.fulfill.dao.OrderFulfillDAO;
import com.ruyuan.eshop.fulfill.dao.OrderFulfillItemDAO;
import com.ruyuan.eshop.fulfill.domain.entity.OrderFulfillDO;
import com.ruyuan.eshop.fulfill.domain.entity.OrderFulfillItemDO;
import com.ruyuan.eshop.fulfill.domain.request.ReceiveFulfillRequest;
import com.ruyuan.eshop.fulfill.exception.FulfillBizException;
import com.ruyuan.eshop.fulfill.exception.FulfillErrorCodeEnum;
import com.ruyuan.eshop.fulfill.service.FulfillService;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.statelang.domain.ExecutionStatus;
import io.seata.saga.statelang.domain.StateMachineInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
@Slf4j
public class FulfillServiceImpl implements FulfillService {

    @Autowired
    private OrderFulfillDAO orderFulfillDAO;

    @Autowired
    private OrderFulfillItemDAO orderFulfillItemDAO;

    @Autowired
    private SpringApplicationContext springApplicationContext;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private FulFillConverter fulFillConverter;

    @Override
    public void createFulfillOrder(ReceiveFulfillRequest request) {
        // 1、生成履约单ID
        String fulfillId = genFulfillId();

        List<OrderFulfillItemDO> orderFulfillItemDOS = fulFillConverter.convertFulFillRequest(request.getReceiveOrderItems());
        // 设置履约单ID
        for (OrderFulfillItemDO item : orderFulfillItemDOS) {
            item.setFulfillId(fulfillId);
        }

        // 2、生成履约单和履约条目
        FulfillData builder = FulfillData.builder()
                .receiveFulFillRequest(request)
                .orderFulFill(fulFillConverter.convertFulFillRequest(request))
                .orderFulFillItems(orderFulfillItemDOS)
                .build();

        OrderFulfillDO orderFulFill = builder.getOrderFulFill();
        orderFulFill.setFulfillId(fulfillId);
        List<OrderFulfillItemDO> orderFulFillItems = builder.getOrderFulFillItems();

        //3、保存履约单和履约条目
        orderFulfillDAO.save(orderFulFill);
        orderFulfillItemDAO.saveBatch(orderFulFillItems);
    }

    @Override
    public void cancelFulfillOrder(String orderId) {
        //1、查询履约单
        OrderFulfillDO orderFulfill = orderFulfillDAO.getOne(orderId);

        //2、移除履约单
        if (null != orderFulfill) {
            orderFulfillDAO.removeById(orderFulfill.getId());
            //3、查询履约单条目
            List<OrderFulfillItemDO> fulfillItems = orderFulfillItemDAO
                    .listByFulfillId(orderFulfill.getFulfillId());

            //4、移除履约单条目
            if(CollectionUtils.isNotEmpty(fulfillItems)) {
                List<Long> ids = fulfillItems.stream().map(OrderFulfillItemDO::getId).collect(Collectors.toList());
                orderFulfillDAO.removeByIds(ids);
            }
        }
    }

    /**
     * 生成履约单id
     *
     * @return
     */
    private String genFulfillId() {
        return RandomUtil.genRandomNumber(10);
    }


    @Override
    public Boolean receiveOrderFulFill(ReceiveFulfillRequest request) {
        log.info("接受订单履约成功，request={}", JSONObject.toJSONString(request));

        String orderId = request.getOrderId();

        // 加分布式锁（防止重复触发履约）
        String key = RedisLockKeyConstants.ORDER_FULFILL_KEY + orderId;
        boolean lock = redisLock.tryLock(key);
        if (!lock) {
            throw new FulfillBizException(FulfillErrorCodeEnum.ORDER_FULFILL_ERROR);
        }

        try {
            //1、幂等：校验orderId是否已经履约过
            if (orderFulfilled(request.getOrderId())) {
                log.info("该订单已履约！！！,orderId={}", request.getOrderId());
                return true;
            }


            //2、saga状态机，触发wms捡货和tms发货
            StateMachineEngine stateMachineEngine = (StateMachineEngine) springApplicationContext
                    .getBean("stateMachineEngine");
            Map<String, Object> startParams = new HashMap<>(3);
            startParams.put("receiveFulfillRequest", request);

            //配置的saga状态机 json的name
            // 位于/resources/statelang/order_fulfull.json
            String stateMachineName = "order_fulfill";
            log.info("开始触发saga流程，stateMachineName={}", stateMachineName);
            StateMachineInstance inst = stateMachineEngine.startWithBusinessKey(stateMachineName, null, null, startParams);
            if (ExecutionStatus.SU.equals(inst.getStatus())) {
                log.info("订单履约流程执行完毕. xid={}", inst.getId());
            } else {
                log.error("订单履约流程执行异常. xid={}", inst.getId());
                throw new FulfillBizException(FulfillErrorCodeEnum.ORDER_FULFILL_IS_ERROR);
            }
            return true;
        } finally {
            redisLock.unlock(key);
        }
    }

    /**
     * 校验订单是否履约过
     *
     * @param orderId
     * @return
     */
    private boolean orderFulfilled(String orderId) {
        OrderFulfillDO orderFulfill = orderFulfillDAO.getOne(orderId);
        return orderFulfill != null;
    }
}
