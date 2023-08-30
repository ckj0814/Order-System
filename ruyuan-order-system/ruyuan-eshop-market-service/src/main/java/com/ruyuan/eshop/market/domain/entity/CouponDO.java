package com.ruyuan.eshop.market.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户优惠券记录表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("market_coupon")
public class CouponDO implements Serializable {

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
     * 优惠券ID
     */
    private String couponId;

    /**
     * 优惠券配置ID
     */
    private String couponConfigId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 是否使用过这个优惠券，1：使用了，0：未使用
     */
    @TableField(value = "is_used")
    private Integer used;

    /**
     * 使用优惠券的时间
     */
    private Date usedTime;

    /**
     * 抵扣金额
     */
    private Integer amount;
}
