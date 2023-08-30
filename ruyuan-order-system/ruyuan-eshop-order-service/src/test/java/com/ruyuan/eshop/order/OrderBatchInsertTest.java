package com.ruyuan.eshop.order;

import com.google.common.collect.Lists;
import com.ruyuan.eshop.common.utils.RandomUtil;
import com.ruyuan.eshop.order.batch.service.OrderDeliveryDetailBatchService;
import com.ruyuan.eshop.order.batch.service.OrderInfoBatchService;
import com.ruyuan.eshop.order.batch.service.OrderItemBatchService;
import com.ruyuan.eshop.order.batch.service.OrderPaymentDetailBatchService;
import com.ruyuan.eshop.order.config.OrderProperties;
import com.ruyuan.eshop.order.domain.entity.OrderDeliveryDetailDO;
import com.ruyuan.eshop.order.domain.entity.OrderInfoDO;
import com.ruyuan.eshop.order.domain.entity.OrderItemDO;
import com.ruyuan.eshop.order.domain.entity.OrderPaymentDetailDO;
import com.ruyuan.eshop.order.enums.OrderNoTypeEnum;
import com.ruyuan.eshop.order.enums.OrderTypeEnum;
import com.ruyuan.eshop.order.manager.OrderNoManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest(classes = OrderApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class OrderBatchInsertTest {

    @Autowired
    private OrderNoManager orderNoManager;

    @Autowired
    private OrderProperties orderProperties;

    @Autowired
    private OrderInfoBatchService orderInfoBatchService;

    @Autowired
    private OrderItemBatchService orderItemBatchService;

    @Autowired
    private OrderPaymentDetailBatchService orderPaymentDetailBatchService;

    @Autowired
    private OrderDeliveryDetailBatchService orderDeliveryDetailBatchService;

    /**
     * 一次性为"订单列表查询"接口提供压测数据
     */
    @Test
    public void executeListQueryTestData() {
        List<OrderInfoDO> orderInfoDOList = Lists.newArrayList();
        List<OrderItemDO> orderItemDOList = Lists.newArrayList();
        List<OrderPaymentDetailDO> orderPaymentDetailDOList = Lists.newArrayList();
        List<OrderDeliveryDetailDO> orderDeliveryDetailDOList = Lists.newArrayList();

        //  组装数据
        for (int i = 0; i < 10000; i++) {
            //  生成订单号
            String userId = String.valueOf(ThreadLocalRandom.current().nextInt(0, 1000));
            String orderId = orderNoManager.genOrderId(OrderNoTypeEnum.SALE_ORDER.getCode(), userId);

            OrderInfoDO orderInfoDO = buildOrderInfo(orderId, userId);
            OrderItemDO orderItemDO = buildOrderItem(orderId, userId);
            OrderPaymentDetailDO paymentDetailDO = buildOrderPaymentDetail(orderId, orderInfoDO);
            OrderDeliveryDetailDO orderDeliveryDetailDO = buildOrderDeliveryDetail(orderId);

            orderInfoDOList.add(orderInfoDO);
            orderItemDOList.add(orderItemDO);
            orderPaymentDetailDOList.add(paymentDetailDO);
            orderDeliveryDetailDOList.add(orderDeliveryDetailDO);
        }

        //  批量插入
        long startTime = System.currentTimeMillis();
        orderInfoBatchService.saveBatch(orderInfoDOList);
        orderItemBatchService.saveBatch(orderItemDOList);
        orderPaymentDetailBatchService.saveBatch(orderPaymentDetailDOList);
        orderDeliveryDetailBatchService.saveBatch(orderDeliveryDetailDOList);
        long endTime = System.currentTimeMillis();

        log.info("插入SQL耗时:{}", (endTime - startTime));
    }

    private OrderDeliveryDetailDO buildOrderDeliveryDetail(String orderId) {
        //  插入order_delivery_detail数据
        OrderDeliveryDetailDO orderDeliveryDetailDO = new OrderDeliveryDetailDO();
        orderDeliveryDetailDO.setOrderId(orderId);
        orderDeliveryDetailDO.setDeliveryType(1);
        orderDeliveryDetailDO.setProvince("110000");
        orderDeliveryDetailDO.setCity("110100");
        orderDeliveryDetailDO.setArea("110105");
        orderDeliveryDetailDO.setStreet("110101007");
        orderDeliveryDetailDO.setDetailAddress("压测数据");
        orderDeliveryDetailDO.setLon(BigDecimal.valueOf(100.1000000000));
        orderDeliveryDetailDO.setLat(BigDecimal.valueOf(1010.2010100000));
        orderDeliveryDetailDO.setReceiverName("压测数据");
        orderDeliveryDetailDO.setReceiverPhone("13434545545");
        orderDeliveryDetailDO.setModifyAddressCount(0);
        orderDeliveryDetailDO.setDelivererNo(null);
        orderDeliveryDetailDO.setDelivererName(null);
        orderDeliveryDetailDO.setDelivererPhone(null);
        orderDeliveryDetailDO.setOutStockTime(null);
        orderDeliveryDetailDO.setSignedTime(null);

        return orderDeliveryDetailDO;
    }

    private OrderPaymentDetailDO buildOrderPaymentDetail(String orderId, OrderInfoDO orderInfoDO) {
        //  插入order_payment_detail数据
        OrderPaymentDetailDO paymentDetailDO = new OrderPaymentDetailDO();
        paymentDetailDO.setOrderId(orderId);
        paymentDetailDO.setAccountType(1);
        paymentDetailDO.setPayType(orderInfoDO.getPayType());

        //  支付时间
        if (orderInfoDO.getOrderStatus() > 10) {
            paymentDetailDO.setPayStatus(20);
        } else {
            paymentDetailDO.setPayStatus(10);
        }
        paymentDetailDO.setPayAmount(orderInfoDO.getPayAmount());
        paymentDetailDO.setPayTime(orderInfoDO.getPayTime());
        paymentDetailDO.setOutTradeNo(RandomUtil.genRandomNumber(19));
        paymentDetailDO.setPayRemark("压测数据");

        return paymentDetailDO;
    }

    private OrderItemDO buildOrderItem(String orderId, String userId) {
        //  插入order_item数据
        OrderItemDO orderItemDO = new OrderItemDO();
        orderItemDO.setOrderId(orderId);
        orderItemDO.setOrderItemId(orderId + "001");
        //  商品类型 1:普通商品,2:预售商品
        orderItemDO.setProductType(1);
        orderItemDO.setProductId("1001010");
        orderItemDO.setProductImg("test.img");
        orderItemDO.setProductName("压测数据");
        orderItemDO.setSkuCode("10101010");
        orderItemDO.setSaleQuantity(10);
        orderItemDO.setSalePrice(1000);
        orderItemDO.setOriginAmount(10000);
        orderItemDO.setPayAmount(9505);
        orderItemDO.setProductUnit("个");
        orderItemDO.setPurchasePrice(500);
        orderItemDO.setSellerId(userId + "000");

        return orderItemDO;
    }

    private OrderInfoDO buildOrderInfo(String orderId, String userId) {
        //  插入order_info数据
        OrderInfoDO orderInfoDO = new OrderInfoDO();
        orderInfoDO.setBusinessIdentifier(1);
        orderInfoDO.setOrderId(orderId);
        orderInfoDO.setParentOrderId(null);
        orderInfoDO.setBusinessOrderId(null);
        orderInfoDO.setOrderType(OrderTypeEnum.NORMAL.getCode());
        /*  订单状态 OrderStatusEnum
        CREATED(10, "已创建"), PAID(20, "已支付"),    FULFILL(30, "已履约"),
        OUT_STOCK(40, "出库"),    DELIVERY(50, "配送中"),
        SIGNED(60, "已签收"),  CANCELED(70, "已取消"),
        REFUSED(100, "已拒收"),    INVALID(127, "无效订单"); */
        int[] orderStatusArray = {10, 20, 30, 40, 50, 60, 70, 100, 127};
        int orderStatusNum = ThreadLocalRandom.current().nextInt(orderStatusArray.length);
        orderInfoDO.setOrderStatus(orderStatusArray[orderStatusNum]);
        orderInfoDO.setCancelType(null);
        orderInfoDO.setCancelTime(null);
        orderInfoDO.setSellerId(userId + "000");
        orderInfoDO.setUserId(userId);
        orderInfoDO.setTotalAmount(10000);
        orderInfoDO.setPayAmount(9600);
        /*
           支付类型  10:微信支付, 20:支付宝支付
         */
        int[] payTypeArray = {10, 20};
        int payTypeNum = ThreadLocalRandom.current().nextInt(payTypeArray.length);
        orderInfoDO.setPayType(payTypeArray[payTypeNum]);
        orderInfoDO.setCouponId("1001001");
        //  支付时间
        if (orderInfoDO.getOrderStatus() > 10) {
            orderInfoDO.setPayTime(new Date());
        } else {
            orderInfoDO.setPayTime(null);
        }

        long currentTimeMillis = System.currentTimeMillis();
        Integer expireTime = orderProperties.getExpireTime();
        orderInfoDO.setExpireTime(new Date(currentTimeMillis + expireTime));

        orderInfoDO.setUserRemark("压测数据");
        orderInfoDO.setDeleteStatus(0);
        orderInfoDO.setCommentStatus(0);
        orderInfoDO.setExtJson(null);

        return orderInfoDO;
    }
}
