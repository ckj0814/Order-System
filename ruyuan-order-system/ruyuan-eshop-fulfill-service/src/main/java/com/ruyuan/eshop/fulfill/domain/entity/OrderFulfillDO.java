package com.ruyuan.eshop.fulfill.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单履约表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("order_fulfill")
public class OrderFulfillDO implements Serializable {

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
     * 履约单ID
     */
    private String fulfillId;

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
     * 配送类型，默认是自配送
     */
    private Integer deliveryType;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 省
     */
    private String receiverProvince;

    /**
     * 市
     */
    private String receiverCity;

    /**
     * 区
     */
    private String receiverArea;

    /**
     * 街道地址
     */
    private String receiverStreet;

    /**
     * 详细地址
     */
    private String receiverDetailAddress;

    /**
     * 经度 六位小数点
     */
    private BigDecimal receiverLon;

    /**
     * 纬度 六位小数点
     */
    private BigDecimal receiverLat;

    /**
     * 配送员编号
     */
    private String delivererNo;
    /**
     * 配送员姓名
     */
    private String delivererName;
    /**
     * 配送员手机号
     */
    private String delivererPhone;

    /**
     * 物流单号
     */
    private String logisticsCode;

    /**
     * 用户备注
     */
    private String userRemark;

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
