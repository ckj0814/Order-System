package com.ruyuan.eshop.address.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruyuan.eshop.address.domain.entity.StreetDO;
import com.ruyuan.eshop.address.mapper.StreetMapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 街道 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class StreetDAO extends BaseDAO<StreetMapper, StreetDO> {

    /**
     * 查询街道信息
     *
     * @param streetCode
     * @param street
     * @return
     */
    public List<StreetDO> listStreets(String streetCode, String street) {
        LambdaQueryWrapper<StreetDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(streetCode), StreetDO::getCode, streetCode)
                .eq(StringUtils.isNotBlank(street), StreetDO::getName, street);
        return list(queryWrapper);
    }
}
