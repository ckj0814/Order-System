package com.ruyuan.eshop.address.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruyuan.eshop.address.domain.entity.ProvinceDO;
import com.ruyuan.eshop.address.mapper.ProvinceMapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 省 DAO
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class ProvinceDAO extends BaseDAO<ProvinceMapper, ProvinceDO> {

    public List<ProvinceDO> listProvinces(Set<String> provinceCodes, String province) {
        LambdaQueryWrapper<ProvinceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(province), ProvinceDO::getName, province)
                .in(CollectionUtils.isNotEmpty(provinceCodes), ProvinceDO::getCode, provinceCodes);
        return list(queryWrapper);
    }
}
