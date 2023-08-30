package com.ruyuan.eshop.common.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;

/**
 * 基础DAO
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public class BaseDAO<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    public List<T> queryByConditions(Map<String, Object> conditions) {
        List<T> list = new ArrayList<>();
        if (null == conditions) {
            return list;
        }

        Map<String, Object> where = new HashMap<>();

        Set<String> keys = conditions.keySet();
        for (String key : keys) {
            if (null == conditions.get(key)) {
                continue;
            }
            where.put(key, conditions.get(key));
        }

        return listByMap(where);
    }
}
