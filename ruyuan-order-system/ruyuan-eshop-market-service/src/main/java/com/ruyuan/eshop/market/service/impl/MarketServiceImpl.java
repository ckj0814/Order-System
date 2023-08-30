package com.ruyuan.eshop.market.service.impl;

import com.ruyuan.eshop.common.enums.AmountTypeEnum;
import com.ruyuan.eshop.common.utils.LoggerFormat;
import com.ruyuan.eshop.common.utils.ParamCheckUtil;
import com.ruyuan.eshop.market.constants.MarketConstant;
import com.ruyuan.eshop.market.convert.MarketConverter;
import com.ruyuan.eshop.market.dao.CouponDAO;
import com.ruyuan.eshop.market.dao.FreightTemplateDAO;
import com.ruyuan.eshop.market.domain.dto.CalculateOrderAmountDTO;
import com.ruyuan.eshop.market.domain.entity.CouponDO;
import com.ruyuan.eshop.market.domain.entity.FreightTemplateDO;
import com.ruyuan.eshop.market.domain.request.CalculateOrderAmountRequest;
import com.ruyuan.eshop.market.exception.MarketBizException;
import com.ruyuan.eshop.market.exception.MarketErrorCodeEnum;
import com.ruyuan.eshop.market.service.MarketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 营销管理service组件
 *
 * @author zhonghuashishan
 */
@Service
@Slf4j
public class MarketServiceImpl implements MarketService {

    @Autowired
    private CouponDAO couponDAO;

    @Autowired
    private FreightTemplateDAO freightTemplateDAO;

    @Autowired
    private MarketConverter marketConverter;

    /**
     * 计算订单费用
     * <p>
     * 假设订单有两个商品条目记录，分摊优惠券的规则如下：
     * 商品1
     * 单价（单位分）    购买数量    小计
     * 1000            10         1000 * 10
     * <p>
     * 商品2
     * 单价    购买数量    小计
     * 100    1         100 * 1
     * <p>
     * <p>
     * 整单优惠券抵扣5元，也就是500分
     * <p>
     * 则商品1分摊的优惠券抵扣金额为：
     * 优惠券抵扣总额 * (商品1单价*商品1购买数量)/((商品1单价*商品1购买数量) + (商品2单价*商品2购买数量))
     * = 500 * (1000 * 10) / ((1000 * 10)  + (100 * 1) )
     * = 5000000 / 10100
     * = 495 分
     * <p>
     * 同样的逻辑可计算出商品2分摊的优惠券抵扣金额为5分，也就是0.05元
     * <p>
     * <p>
     * 如果计算出的优惠券分摊到一条 item 上存在小数时，则向上取整，然后最后一条 item 分摊的金额就用优惠金额减掉前面所有优惠的item分摊的总额
     *
     * @param calculateOrderAmountRequest 计算订单费用入参
     * @return
     */
    @Override
    public CalculateOrderAmountDTO calculateOrderAmount(CalculateOrderAmountRequest calculateOrderAmountRequest) {
        log.info(LoggerFormat.build()
                .remark("calculateOrderAmount->request")
                .data("request", calculateOrderAmountRequest)
                .finish());

        // 检查入参
        this.checkCalculateOrderAmountRequest(calculateOrderAmountRequest);

        String orderId = calculateOrderAmountRequest.getOrderId();
        String userId = calculateOrderAmountRequest.getUserId();
        String couponId = calculateOrderAmountRequest.getCouponId();
        String regionId = calculateOrderAmountRequest.getRegionId();

        // 优惠券抵扣金额
        Integer discountAmount = 0;
        if (StringUtils.isNotEmpty(couponId)) {
            // 锁定优惠券
            CouponDO couponDO = getCouponAchieve(userId, couponId);
            discountAmount = couponDO.getAmount();
        }

        // 原订单费用信息
        List<CalculateOrderAmountDTO.OrderAmountDTO> orderAmountList = marketConverter.convertOrderAmountRequest(calculateOrderAmountRequest.getOrderAmountRequestList());
        for (CalculateOrderAmountDTO.OrderAmountDTO orderAmountDTO : orderAmountList) {
            orderAmountDTO.setOrderId(orderId);
        }

        // 订单条目费用信息
        List<CalculateOrderAmountDTO.OrderAmountDetailDTO> orderAmountDetailDTOList = new ArrayList<>();
        List<CalculateOrderAmountRequest.OrderItemRequest> orderItemRequestList =
                calculateOrderAmountRequest.getOrderItemRequestList();

        // 先统计全部商品费用
        int totalProductAmount = 0;
        for (CalculateOrderAmountRequest.OrderItemRequest orderItemRequest : orderItemRequestList) {
            totalProductAmount += orderItemRequest.getSalePrice() * orderItemRequest.getSaleQuantity();
        }

        int index = 0;
        int totalNum = orderItemRequestList.size();
        Integer notLastItemTotalDiscountAmount = 0;
        for (CalculateOrderAmountRequest.OrderItemRequest orderItemRequest : orderItemRequestList) {
            // 订单条目支付原价
            CalculateOrderAmountDTO.OrderAmountDetailDTO originPayAmountDetail =
                    createOrderAmountDetailDTO(orderId,
                            AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode(),
                            null,
                            null,
                            orderItemRequest);
            orderAmountDetailDTOList.add(originPayAmountDetail);

            // 优惠券抵扣金额
            CalculateOrderAmountDTO.OrderAmountDetailDTO couponDiscountAmountDetail;
            if (++index < totalNum) {
                // 订单条目分摊的优惠金额
                double partDiscountAmount = Integer.valueOf(discountAmount
                        * orderItemRequest.getSalePrice() * orderItemRequest.getSaleQuantity()).doubleValue()
                        / Integer.valueOf(totalProductAmount).doubleValue();

                // 遇到小数则向上取整
                double curDiscountAmount = Math.ceil(partDiscountAmount);
                couponDiscountAmountDetail =
                        createOrderAmountDetailDTO(orderId,
                                AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode(),
                                Double.valueOf(curDiscountAmount).intValue(),
                                null,
                                orderItemRequest);

                notLastItemTotalDiscountAmount += couponDiscountAmountDetail.getAmount();
            } else {
                // 最后一条item的优惠金额等于总优惠金额-前面所有item分摊的优惠总额
                couponDiscountAmountDetail =
                        createOrderAmountDetailDTO(orderId,
                                AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode(),
                                discountAmount - notLastItemTotalDiscountAmount,
                                null,
                                orderItemRequest);
            }
            orderAmountDetailDTOList.add(couponDiscountAmountDetail);

            // 实付金额
            Integer realPayAmount = originPayAmountDetail.getAmount() - couponDiscountAmountDetail.getAmount();
            CalculateOrderAmountDTO.OrderAmountDetailDTO realPayAmountDetail =
                    createOrderAmountDetailDTO(orderId,
                            AmountTypeEnum.REAL_PAY_AMOUNT.getCode(),
                            null,
                            realPayAmount,
                            orderItemRequest);
            orderAmountDetailDTOList.add(realPayAmountDetail);
        }

        // 重新计算订单支付原价、优惠券抵扣金额、实付金额
        Integer totalOriginPayAmount = 0;
        Integer totalDiscountAmount = 0;
        Integer totalRealPayAmount = 0;
        for (CalculateOrderAmountDTO.OrderAmountDetailDTO orderAmountDetailDTO : orderAmountDetailDTOList) {
            Integer amountType = orderAmountDetailDTO.getAmountType();
            Integer amount = orderAmountDetailDTO.getAmount();
            if (AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode().equals(amountType)) {
                totalOriginPayAmount += amount;
            } else if (AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode().equals(amountType)) {
                totalDiscountAmount += amount;
            } else if (AmountTypeEnum.REAL_PAY_AMOUNT.getCode().equals(amountType)) {
                totalRealPayAmount += amount;
            }
        }

        // 总的实付金额还要加上运费
        Map<Integer, CalculateOrderAmountDTO.OrderAmountDTO> orderAmountMap =
                orderAmountList.stream().collect(Collectors.toMap(
                        CalculateOrderAmountDTO.OrderAmountDTO::getAmountType, Function.identity()));
        // 运费
        Integer shippingAmount = calculateOrderShippingAmount(regionId, orderAmountMap);
        if (shippingAmount != null) {
            totalRealPayAmount += shippingAmount;
        }

        for (CalculateOrderAmountDTO.OrderAmountDTO orderAmountDTO : orderAmountList) {
            Integer amountType = orderAmountDTO.getAmountType();
            if (AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode().equals(amountType)) {
                orderAmountDTO.setAmount(totalOriginPayAmount);
            } else if (AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode().equals(amountType)) {
                orderAmountDTO.setAmount(totalDiscountAmount);
            } else if (AmountTypeEnum.REAL_PAY_AMOUNT.getCode().equals(amountType)) {
                orderAmountDTO.setAmount(totalRealPayAmount);
            }
        }

        CalculateOrderAmountDTO calculateOrderAmountDTO = new CalculateOrderAmountDTO();
        calculateOrderAmountDTO.setOrderAmountList(orderAmountList);
        calculateOrderAmountDTO.setOrderAmountDetail(orderAmountDetailDTOList);

        log.info(LoggerFormat.build()
                .remark("calculateOrderAmount->response")
                .data("response", calculateOrderAmountDTO)
                .finish());
        return calculateOrderAmountDTO;
    }

    /**
     * 计算订单价格入参检查
     *
     * @param calculateOrderAmountRequest
     */
    private void checkCalculateOrderAmountRequest(CalculateOrderAmountRequest calculateOrderAmountRequest) {
        // 订单ID
        String orderId = calculateOrderAmountRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId);

        // 用户ID
        String userId = calculateOrderAmountRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId);

        // 订单商品条目
        List<CalculateOrderAmountRequest.OrderItemRequest> orderItemRequestList =
                calculateOrderAmountRequest.getOrderItemRequestList();
        ParamCheckUtil.checkCollectionNonEmpty(orderItemRequestList);

        // 订单费用信息
        List<CalculateOrderAmountRequest.OrderAmountRequest> orderAmountRequestList =
                calculateOrderAmountRequest.getOrderAmountRequestList();
        ParamCheckUtil.checkCollectionNonEmpty(orderAmountRequestList);
    }

    /**
     * 获取用户的优惠券
     *
     * @param userId
     * @param couponId
     */
    private CouponDO getCouponAchieve(String userId, String couponId) {
        CouponDO couponDO = couponDAO.getUserCoupon(userId, couponId);
        if (couponDO == null) {
            throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_IS_NULL);
        }
        return couponDO;
    }

    /**
     * 组装订单条目费用明细
     *
     * @param orderId          订单号
     * @param amountType       费用类型
     * @param discountAmount   优惠券抵扣金额
     * @param realPayAmount    实付金额
     * @param orderItemRequest 订单条目
     * @return
     */
    private CalculateOrderAmountDTO.OrderAmountDetailDTO createOrderAmountDetailDTO(
            String orderId,
            Integer amountType,
            Integer discountAmount,
            Integer realPayAmount,
            CalculateOrderAmountRequest.OrderItemRequest orderItemRequest) {
        CalculateOrderAmountDTO.OrderAmountDetailDTO orderAmountDetailDTO =
                new CalculateOrderAmountDTO.OrderAmountDetailDTO();
        orderAmountDetailDTO.setOrderId(orderId);
        orderAmountDetailDTO.setProductType(orderItemRequest.getProductType());
        orderAmountDetailDTO.setSkuCode(orderItemRequest.getSkuCode());
        orderAmountDetailDTO.setSaleQuantity(orderItemRequest.getSaleQuantity());
        orderAmountDetailDTO.setSalePrice(orderItemRequest.getSalePrice());
        orderAmountDetailDTO.setAmountType(amountType);
        if (AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode().equals(amountType)) {
            orderAmountDetailDTO.setAmount(orderAmountDetailDTO.getSaleQuantity()
                    * orderAmountDetailDTO.getSalePrice());
        } else if (AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode().equals(amountType)) {
            orderAmountDetailDTO.setAmount(discountAmount);
        } else if (AmountTypeEnum.REAL_PAY_AMOUNT.getCode().equals(amountType)) {
            orderAmountDetailDTO.setAmount(realPayAmount);
        }
        return orderAmountDetailDTO;
    }

    /**
     * 计算订单运费
     *
     * @param regionId       区域ID
     * @param orderAmountMap 订单费用
     * @return
     */
    private Integer calculateOrderShippingAmount(String regionId,
                                                 Map<Integer, CalculateOrderAmountDTO.OrderAmountDTO> orderAmountMap) {
        // 运费
        Integer shippingAmount;
        // 满多少免运费
        Integer conditionAmount;

        // 查找运费模板
        FreightTemplateDO freightTemplateDO = freightTemplateDAO.getByRegionId(regionId);
        if (freightTemplateDO != null) {
            shippingAmount = freightTemplateDO.getShippingAmount();
            conditionAmount = freightTemplateDO.getConditionAmount();
        } else {
            shippingAmount = MarketConstant.DEFAULT_SHIPPING_AMOUNT;
            conditionAmount = MarketConstant.DEFAULT_CONDITION_AMOUNT;
        }

        // 订单金额
        Integer originPayAmount = 0;
        if (orderAmountMap.get(AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode()) != null) {
            originPayAmount = orderAmountMap.get(AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode()).getAmount();
        }
        // 如果原单金额大于指定值则免运费
        if (originPayAmount >= conditionAmount) {
            shippingAmount = 0;
        }
        return shippingAmount;
    }

}
