package com.ruyuan.eshop.address.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruyuan.eshop.address.domain.entity.CityDO;
import com.ruyuan.eshop.address.mapper.CityMapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 市 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class CityDAO extends BaseDAO<CityMapper, CityDO> {

    /**
     * 查询市
     *
     * @param cityCodes
     * @param city
     * @return
     */
    public List<CityDO> listCities(Set<String> cityCodes, String city) {
        LambdaQueryWrapper<CityDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(city), CityDO::getName, city)
                .in(CollectionUtils.isNotEmpty(cityCodes), CityDO::getCode, cityCodes);
        return list(queryWrapper);
    }
}
