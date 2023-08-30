package com.ruyuan.eshop.order.controller;

import com.ruyuan.eshop.common.constants.RedisLockKeyConstants;
import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.common.page.PagingInfo;
import com.ruyuan.eshop.common.redis.RedisLock;
import com.ruyuan.eshop.order.api.AfterSaleApi;
import com.ruyuan.eshop.order.api.AfterSaleQueryApi;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderDetailDTO;
import com.ruyuan.eshop.order.domain.dto.AfterSaleOrderListDTO;
import com.ruyuan.eshop.order.domain.dto.LackDTO;
import com.ruyuan.eshop.order.domain.query.AfterSaleQuery;
import com.ruyuan.eshop.order.domain.request.CancelOrderRequest;
import com.ruyuan.eshop.order.domain.request.LackRequest;
import com.ruyuan.eshop.order.domain.request.ReturnGoodsOrderRequest;
import com.ruyuan.eshop.order.domain.request.RevokeAfterSaleRequest;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
import com.ruyuan.eshop.order.service.OrderAfterSaleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单售后流程controller
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@RestController
@RequestMapping("/afterSale")
@Slf4j
public class AfterSaleController {

    @Autowired
    private OrderAfterSaleService orderAfterSaleService;

    @DubboReference(version = "1.0.0", retries = 0)
    private AfterSaleApi afterSaleApi;

    @DubboReference(version = "1.0.0")
    private AfterSaleQueryApi afterSaleQueryApi;

    @Autowired
    private RedisLock redisLock;

    /**
     * 用户手动取消订单
     */
    @PostMapping("/cancelOrder")
    public JsonResult<Boolean> cancelOrder(@RequestBody CancelOrderRequest cancelOrderRequest) {
        return orderAfterSaleService.cancelOrder(cancelOrderRequest);
    }

    /**
     * 用户发起退货售后
     */
    @PostMapping("/applyAfterSale")
    public JsonResult<Boolean> applyAfterSale(@RequestBody ReturnGoodsOrderRequest returnGoodsOrderRequest) {
        //  分布式锁
        String orderId = returnGoodsOrderRequest.getOrderId();
        String key = RedisLockKeyConstants.REFUND_KEY + orderId;
        boolean lock = redisLock.tryLock(key);
        if (!lock) {
            throw new OrderBizException(OrderErrorCodeEnum.PROCESS_AFTER_SALE_RETURN_GOODS);
        }
        try {
            return orderAfterSaleService.processApplyAfterSale(returnGoodsOrderRequest);
        } finally {
            redisLock.unlock(key);
        }
    }

    /**
     * 缺品请求
     */
    @PostMapping("/lockItem")
    public JsonResult<LackDTO> lockItem(@RequestBody LackRequest request) {
        JsonResult<LackDTO> result = afterSaleApi.lockItem(request);
        return result;
    }

    /**
     * 用户撤销售后申请
     */
    @PostMapping("/revokeAfterSale")
    public JsonResult<Boolean> revokeAfterSale(@RequestBody RevokeAfterSaleRequest request) {
        JsonResult<Boolean> result = afterSaleApi.revokeAfterSale(request);
        return result;
    }

    /**
     * 查询售后列表
     */
    @PostMapping("/listAfterSales")
    public JsonResult<PagingInfo<AfterSaleOrderListDTO>> listAfterSales(@RequestBody AfterSaleQuery query) {
        JsonResult<PagingInfo<AfterSaleOrderListDTO>> result = afterSaleQueryApi.listAfterSales(query);
        return result;
    }

    /**
     * 查询售后单详情
     */
    @GetMapping("/afterSaleDetail")
    public JsonResult<AfterSaleOrderDetailDTO> afterSaleDetail(Long afterSaleId) {
        JsonResult<AfterSaleOrderDetailDTO> result = afterSaleQueryApi.afterSaleDetail(afterSaleId);
        return result;
    }
}
