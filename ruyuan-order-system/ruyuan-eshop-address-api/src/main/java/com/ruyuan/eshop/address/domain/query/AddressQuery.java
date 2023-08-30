package com.ruyuan.eshop.address.domain.query;


import lombok.Data;

import java.io.Serializable;

/**
 * 行政地址查询条件
 */
@Data
public class AddressQuery implements Serializable {

    private static final long serialVersionUID = 172807354706383174L;

    /**
     * 省
     */
    private String provinceCode;
    private String province;

    /**
     * 市
     */
    private String cityCode;
    private String city;

    /**
     * 区
     */
    private String areaCode;
    private String area;

    /**
     * 街道
     */
    private String streetCode;
    private String street;
}
