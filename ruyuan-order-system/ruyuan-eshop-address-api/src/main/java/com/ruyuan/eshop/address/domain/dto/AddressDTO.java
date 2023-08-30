package com.ruyuan.eshop.address.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AddressDTO implements Serializable {

    private static final long serialVersionUID = 1715257531729979175L;

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

    public AddressDTO(String provinceCode, String province) {
        this.provinceCode = provinceCode;
        this.province = province;
    }

    public AddressDTO(String provinceCode, String province, String cityCode, String city) {
        this.provinceCode = provinceCode;
        this.province = province;
        this.cityCode = cityCode;
        this.city = city;
    }

    public AddressDTO(String provinceCode, String province, String cityCode, String city, String areaCode, String area) {
        this.provinceCode = provinceCode;
        this.province = province;
        this.cityCode = cityCode;
        this.city = city;
        this.areaCode = areaCode;
        this.area = area;
    }

    public AddressDTO(String provinceCode, String province, String cityCode, String city, String areaCode, String area, String streetCode, String street) {
        this.provinceCode = provinceCode;
        this.province = province;
        this.cityCode = cityCode;
        this.city = city;
        this.areaCode = areaCode;
        this.area = area;
        this.streetCode = streetCode;
        this.street = street;
    }
}
