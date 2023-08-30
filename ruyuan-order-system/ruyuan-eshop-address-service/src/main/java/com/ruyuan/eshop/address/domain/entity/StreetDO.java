package com.ruyuan.eshop.address.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 街道设置
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("street")
public class StreetDO implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 街道代码
     */
    private String code;

    /**
     * 父级区代码
     */
    private String areaCode;

    /**
     * 街道名称
     */
    private String name;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 排序
     */
    private Integer sort;


}
