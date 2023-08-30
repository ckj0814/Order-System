package com.ruyuan.eshop.address.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruyuan.eshop.address.domain.entity.AreaDO;
import com.ruyuan.eshop.address.mapper.AreaMapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 区域 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class AreaDAO extends BaseDAO<AreaMapper, AreaDO> {


    /**
     * 查询地区
     *
     * @param areaCodes
     * @param area
     * @return
     */
    public List<AreaDO> listAreas(Set<String> areaCodes, String area) {
        LambdaQueryWrapper<AreaDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(area), AreaDO::getName, area)
                .in(CollectionUtils.isNotEmpty(areaCodes), AreaDO::getCode, areaCodes);
        return list(queryWrapper);
    }
}
