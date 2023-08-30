package com.ruyuan.eshop.order.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单价格表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("order_amount")
public class OrderAmountDO implements Serializable {

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
     * 收费类型
     */
    private Integer amountType;

    /**
     * 收费金额
     */
    private Integer amount;
}
