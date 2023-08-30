package com.ruyuan.eshop.order.manager.impl;

import com.ruyuan.eshop.common.utils.DateFormatUtil;
import com.ruyuan.eshop.common.utils.NumberUtil;
import com.ruyuan.eshop.order.domain.entity.OrderAutoNoDO;
import com.ruyuan.eshop.order.enums.OrderNoTypeEnum;
import com.ruyuan.eshop.order.exception.OrderBizException;
import com.ruyuan.eshop.order.exception.OrderErrorCodeEnum;
import com.ruyuan.eshop.order.manager.OrderNoManager;
import com.ruyuan.eshop.order.mapper.OrderAutoNoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
public class OrderNoManagerImpl implements OrderNoManager {

    @Autowired
    private OrderAutoNoMapper orderAutoNoMapper;

    /**
     * 19位，2位是业务类型，比如10开头是正向，20开头是逆向，然后中间6位是日期，然后中间8位是序列号，最后3位是用户ID后三位
     * 用户ID不足3位前面补0
     *
     * @param userId
     * @return
     */
    @Override
    public String genOrderId(Integer orderNoType, String userId) {
        // 检查orderNoType是否正确
        OrderNoTypeEnum orderNoTypeEnum = OrderNoTypeEnum.getByCode(orderNoType);
        if (orderNoTypeEnum == null) {
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_NO_TYPE_ERROR);
        }
        return orderNoType + getOrderIdKey(userId);
    }

    /**
     * 获取订单ID
     *
     * @param userId
     * @return
     */
    private String getOrderIdKey(String userId) {
        return getDateTimeKey() + getAutoNoKey() + getUserIdKey(userId);
    }

    /**
     * 生成订单号的中间6位日期
     *
     * @return
     */
    private String getDateTimeKey() {
        return DateFormatUtil.format(new Date(), "yyMMdd");
    }

    /**
     * 生成订单号中间的8位序列号
     *
     * @return
     */
    private String getAutoNoKey() {

        OrderAutoNoDO orderAutoNoDO = new OrderAutoNoDO();
        orderAutoNoMapper.insert(orderAutoNoDO);
        Long autoNo = orderAutoNoDO.getId();
        return String.valueOf(NumberUtil.genNo(autoNo, 8));
    }

    /**
     * 截取用户ID的后三位
     *
     * @param userId
     * @return
     */
    private String getUserIdKey(String userId) {
        // 如果userId的长度大于或等于3，则直接返回
        if (userId.length() >= 3) {
            return userId.substring(userId.length() - 3);
        }

        // 如果userId的长度大于或等于3，则直接前面补0
        String userIdKey = userId;
        while (userIdKey.length() != 3) {
            userIdKey = "0" + userIdKey;
        }
        return userIdKey;
    }

}