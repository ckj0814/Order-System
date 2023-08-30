package com.ruyuan.eshop.market.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.market.api.MarketApi;
import com.ruyuan.eshop.market.domain.request.ReleaseUserCouponRequest;
import com.ruyuan.eshop.market.exception.MarketBizException;
import com.ruyuan.eshop.market.exception.MarketErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@Component
public class ReleasePropertyListener implements MessageListenerConcurrently {

    @DubboReference(version = "1.0.0")
    private MarketApi marketApi;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt msg : list) {
                String content = new String(msg.getBody(), StandardCharsets.UTF_8);
                log.info("ReleasePropertyConsumer message:{}", content);

                ReleaseUserCouponRequest releaseUserCouponRequest = JSONObject.parseObject(content, ReleaseUserCouponRequest.class);
                //  释放优惠券
                JsonResult<Boolean> jsonResult = marketApi.releaseUserCoupon(releaseUserCouponRequest);
                if (!jsonResult.getSuccess()) {
                    throw new MarketBizException(MarketErrorCodeEnum.CONSUME_MQ_FAILED);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            log.error("consumer error", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}