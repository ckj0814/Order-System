package com.ruyuan.eshop.market.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruyuan.eshop.common.dao.BaseDAO;
import com.ruyuan.eshop.market.domain.entity.FreightTemplateDO;
import com.ruyuan.eshop.market.mapper.FreightTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 运费模板 DAO 接口
 * </p>
 *
 * @author zhonghuashishan
 */
@Repository
public class FreightTemplateDAO extends BaseDAO<FreightTemplateMapper, FreightTemplateDO> {

    @Autowired
    private FreightTemplateMapper freightTemplateMapper;

    /**
     * 通过区域ID查找运费模板
     */
    public FreightTemplateDO getByRegionId(String regionId) {
        QueryWrapper<FreightTemplateDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("region_id", regionId);
        return freightTemplateMapper.selectOne(queryWrapper);
    }

}
