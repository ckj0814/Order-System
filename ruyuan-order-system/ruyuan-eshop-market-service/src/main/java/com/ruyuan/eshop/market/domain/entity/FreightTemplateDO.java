package com.ruyuan.eshop.market.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 运费模板
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("market_freight_template")
public class FreightTemplateDO implements Serializable {

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
     * 模板名称
     */
    private String name;

    /**
     * 区域ID
     */
    private String regionId;

    /**
     * 标准运费
     */
    private Integer shippingAmount;

    /**
     * 订单满多少钱则免运费
     */
    private Integer conditionAmount;

}
