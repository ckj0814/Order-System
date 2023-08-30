package com.ruyuan.eshop.tms.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 物流单
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@TableName("logistic_order")
public class LogisticOrderDO implements Serializable {

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
     * 订单号
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
     * 物流单号
     */
    private String logisticCode;
    /**
     * 物流单内容
     */
    private String content;
}
