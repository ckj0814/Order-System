package com.ruyuan.eshop.address.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 地区设置
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
@TableName("area")
public class AreaDO implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 区代码
     */
    private String code;

    /**
     * 父级市代码
     */
    private String cityCode;

    /**
     * 市名称
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
