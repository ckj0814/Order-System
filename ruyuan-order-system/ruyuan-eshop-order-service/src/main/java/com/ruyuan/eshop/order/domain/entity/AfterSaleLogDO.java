package com.ruyuan.eshop.order.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 售后单变更表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("after_sale_log")
public class AfterSaleLogDO implements Serializable {
    private static final long serialVersionUID = -594243757641531958L;
    /**
     * 售后单号
     */
    private String afterSaleId;

    /**
     * 前一个状态
     */
    private Integer preStatus;

    /**
     * 当前状态
     */
    private Integer currentStatus;

    /**
     * 备注
     */
    private String remark;

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

}
