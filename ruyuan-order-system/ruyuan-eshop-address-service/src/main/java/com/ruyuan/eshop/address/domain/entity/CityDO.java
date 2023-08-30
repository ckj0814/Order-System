package com.ruyuan.eshop.address.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 城市设置
 * </p>
 *
 * @author zhonghuashishan
 * @since 2021-11-29
 */
@Data
@TableName("city")
public class CityDO implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 市代码
     */
    private String code;

    /**
     * 市名称
     */
    private String name;

    /**
     * 简称
     */
    private String shortCode;

    /**
     * 省代码
     */
    private String provinceCode;

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
