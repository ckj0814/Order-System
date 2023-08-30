package com.ruyuan.eshop.wms.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 出库单
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@TableName("delivery_order")
public class DeliveryOrderDO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
    /**
     * 接入方业务线标识  1, "自营商城"
     */
    private Integer businessIdentifier;

    /**
     * 出库单ID
     */
    private String deliveryOrderId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 商家id
     */
    private String sellerId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 付款总金额
     */
    private Integer payAmount;

    /**
     * 交易总金额
     */
    private Integer totalAmount;

    /**
     * 运费
     */
    private Integer deliveryAmount;

}
