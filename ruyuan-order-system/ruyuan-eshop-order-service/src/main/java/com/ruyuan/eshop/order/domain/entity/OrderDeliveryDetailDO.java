package com.ruyuan.eshop.order.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单配送信息表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("order_delivery_detail")
public class OrderDeliveryDetailDO implements Serializable {

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
     * 订单编号
     */
    private String orderId;

    /**
     * 配送类型
     */
    private Integer deliveryType;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 街道
     */
    private String street;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 经度
     */
    private BigDecimal lon;

    /**
     * 维度
     */
    private BigDecimal lat;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 调整地址次数
     */
    private Integer modifyAddressCount;

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
     * 出库时间
     */
    private Date outStockTime;

    /**
     * 签收时间
     */
    private Date signedTime;
}
