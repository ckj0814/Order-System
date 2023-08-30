package com.ruyuan.eshop.address.api.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.ruyuan.eshop.address.api.AddressApi;
import com.ruyuan.eshop.address.dao.AreaDAO;
import com.ruyuan.eshop.address.dao.CityDAO;
import com.ruyuan.eshop.address.dao.ProvinceDAO;
import com.ruyuan.eshop.address.dao.StreetDAO;
import com.ruyuan.eshop.address.domain.dto.AddressDTO;
import com.ruyuan.eshop.address.domain.entity.AreaDO;
import com.ruyuan.eshop.address.domain.entity.CityDO;
import com.ruyuan.eshop.address.domain.entity.ProvinceDO;
import com.ruyuan.eshop.address.domain.entity.StreetDO;
import com.ruyuan.eshop.address.domain.query.AddressQuery;
import com.ruyuan.eshop.address.exception.AddressErrorCodeEnum;
import com.ruyuan.eshop.common.core.JsonResult;
import com.ruyuan.eshop.common.exception.BaseBizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
@DubboService(version = "1.0.0", interfaceClass = AddressApi.class)
public class AddressApiImpl implements AddressApi {

    @Autowired
    private StreetDAO streetDAO;

    @Autowired
    private AreaDAO areaDAO;

    @Autowired
    private CityDAO cityDAO;

    @Autowired
    private ProvinceDAO provinceDAO;


    @Override
    public JsonResult<AddressDTO> queryAddress(AddressQuery query) {
        //1、参数校验
        checkParam(query);

        //2、查询街道信息
        List<StreetDO> streets = new ArrayList<>();
        if (streetNotEmpty(query)) {
            streets = streetDAO.listStreets(query.getStreetCode(), query.getStreet());

            if (CollectionUtils.isEmpty(streets)) {
                return JsonResult.buildSuccess();
            }
        }

        //3、查询区信息
        List<AreaDO> areas = new ArrayList<>();
        if (areaNotEmpty(query) || CollectionUtils.isNotEmpty(streets)) {
            Set<String> areaCodes = Optional.ofNullable(streets)
                    .map(street -> street.stream().map(StreetDO::getAreaCode).collect(Collectors.toSet()))
                    .orElse(Collections.EMPTY_SET);
            if (StringUtils.isNotBlank(query.getAreaCode())) {
                areaCodes.add(query.getAreaCode());
            }
            areas = areaDAO.listAreas(areaCodes, query.getArea());

            if (CollectionUtils.isEmpty(areas)) {
                return JsonResult.buildSuccess();
            }
        }

        //4、查询市信息
        List<CityDO> cities = new ArrayList<>();
        if (cityNotEmpty(query) || CollectionUtils.isNotEmpty(areas)) {
            Set<String> cityCodes = Optional.ofNullable(areas)
                    .map(area -> area.stream().map(AreaDO::getCityCode).collect(Collectors.toSet()))
                    .orElse(Collections.EMPTY_SET);
            if (StringUtils.isNotBlank(query.getCityCode())) {
                cityCodes.add(query.getCityCode());
            }
            cities = cityDAO.listCities(cityCodes, query.getCity());

            if (CollectionUtils.isEmpty(cities)) {
                return JsonResult.buildSuccess();
            }
        }

        //4、查询省信息
        List<ProvinceDO> provinces = new ArrayList<>();
        if (provinceNotEmpty(query) || CollectionUtils.isNotEmpty(cities)) {
            Set<String> provinceCodes = Optional.ofNullable(cities)
                    .map(city -> city.stream().map(CityDO::getProvinceCode).collect(Collectors.toSet()))
                    .orElse(Collections.EMPTY_SET);
            if (StringUtils.isNotBlank(query.getProvinceCode())) {
                provinceCodes.add(query.getProvinceCode());
            }
            provinces = provinceDAO.listProvinces(provinceCodes, query.getProvince());

            if (CollectionUtils.isEmpty(provinces)) {
                return JsonResult.buildSuccess();
            }
        }

        //5、组装候选结果集合
        List<AddressDTO> candidates = assembleResult(provinces, cities, areas, streets);


        //6、筛选结果
        for (AddressDTO candidate : candidates) {
            if (isMatch(candidate, query)) {
                //7、组装返参
                return JsonResult.buildSuccess(candidate);
            }
        }

        return JsonResult.buildSuccess();
    }

    private boolean isMatch(AddressDTO candidate, AddressQuery query) {
        boolean provinceCodeMatch = doMatch(candidate.getProvinceCode(), query.getProvinceCode());
        boolean provinceMatch = doMatch(candidate.getProvince(), query.getProvince());
        boolean cityCodeMatch = doMatch(candidate.getCityCode(), query.getCityCode());
        boolean cityMatch = doMatch(candidate.getCity(), query.getCity());
        boolean areaCodeMatch = doMatch(candidate.getAreaCode(), candidate.getAreaCode());
        boolean areaMatch = doMatch(candidate.getArea(), query.getArea());
        boolean streetCodeMatch = doMatch(candidate.getStreetCode(), query.getStreetCode());
        boolean streetMatch = doMatch(candidate.getStreet(), query.getStreet());

        return provinceCodeMatch && provinceMatch && cityCodeMatch && cityMatch
                && areaCodeMatch && areaMatch && streetCodeMatch && streetMatch;
    }

    private boolean doMatch(String candidate, String query) {
        if (Objects.isNull(query) || StringUtils.isBlank(query)) {
            return true;
        }
        if (Objects.isNull(candidate) || StringUtils.isBlank(candidate)) {
            return false;
        }
        return candidate.equals(query);
    }

    /**
     * 组装返参
     *
     * @param provinces
     * @param cities
     * @param areas
     * @param streets
     * @return
     */
    private List<AddressDTO> assembleResult(List<ProvinceDO> provinces, List<CityDO> cities
            , List<AreaDO> areas, List<StreetDO> streets) {

        List<AddressDTO> result = new ArrayList<>();

        for (ProvinceDO province : provinces) {
            result.addAll(assembleResult(province, cities, areas, streets));
        }

        return result;
    }

    private List<AddressDTO> assembleResult(ProvinceDO province, List<CityDO> cities, List<AreaDO> areas
            , List<StreetDO> streets) {

        //过滤出province下的city
        cities = cities.stream().filter(city -> city.getProvinceCode().equals(province.getCode()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(cities)) {
            return Lists.newArrayList(new AddressDTO(province.getCode(), province.getName()));
        }

        List<AddressDTO> result = new ArrayList<>();
        for (CityDO city : cities) {
            result.addAll(assembleResult(province, city, areas, streets));
        }
        return result;
    }

    private List<AddressDTO> assembleResult(ProvinceDO province, CityDO city, List<AreaDO> areas
            , List<StreetDO> streets) {

        //过滤出city下的area
        areas = areas.stream().filter(area -> area.getCityCode().equals(city.getCode()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(areas)) {
            return Lists.newArrayList(new AddressDTO(province.getCode(), province.getName(),
                    city.getCode(), city.getName()));
        }

        List<AddressDTO> result = new ArrayList<>();
        for (AreaDO area : areas) {
            result.addAll(assembleResult(province, city, area, streets));
        }
        return result;
    }

    private List<AddressDTO> assembleResult(ProvinceDO province, CityDO city, AreaDO area
            , List<StreetDO> streets) {

        //过滤出area下的street
        streets = streets.stream().filter(street -> street.getAreaCode().equals(area.getCode()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(streets)) {
            return Lists.newArrayList(new AddressDTO(province.getCode(), province.getName(),
                    city.getCode(), city.getName(), area.getCode(), area.getName()));
        }

        List<AddressDTO> result = new ArrayList<>();
        for (StreetDO street : streets) {
            result.add(new AddressDTO(province.getCode(), province.getName(),
                    city.getCode(), city.getName(), area.getCode(), area.getName(),
                    street.getCode(), street.getName()));
        }
        return result;
    }

    private boolean provinceNotEmpty(AddressQuery query) {
        return StringUtils.isNotBlank(query.getProvince())
                || StringUtils.isNotBlank(query.getProvinceCode());
    }

    private boolean cityNotEmpty(AddressQuery query) {
        return StringUtils.isNotBlank(query.getCity())
                || StringUtils.isNotBlank(query.getCityCode());
    }

    private boolean areaNotEmpty(AddressQuery query) {
        return StringUtils.isNotBlank(query.getArea())
                || StringUtils.isNotBlank(query.getAreaCode());
    }

    private boolean streetNotEmpty(AddressQuery query) {
        return StringUtils.isNotBlank(query.getStreet())
                || StringUtils.isNotBlank(query.getStreetCode());
    }


    private void checkParam(AddressQuery query) {
        if (allEmpty(query)) {
            throw new BaseBizException(AddressErrorCodeEnum.PARAM_CANNOT_ALL_EMPTY);
        }
    }

    private boolean allEmpty(AddressQuery query) {
        return StringUtils.isBlank(query.getProvince())
                && StringUtils.isBlank(query.getProvinceCode())
                && StringUtils.isBlank(query.getCity())
                && StringUtils.isBlank(query.getCityCode())
                && StringUtils.isBlank(query.getArea())
                && StringUtils.isBlank(query.getAreaCode())
                && StringUtils.isBlank(query.getStreet())
                && StringUtils.isBlank(query.getStreetCode());
    }
}
