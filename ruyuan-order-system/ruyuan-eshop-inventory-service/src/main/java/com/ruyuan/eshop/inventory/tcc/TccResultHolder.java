package com.ruyuan.eshop.inventory.tcc;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储TCC第一阶段执行结果，用于解决TCC幂等，空回滚，悬挂问题
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public class TccResultHolder {

    /**
     * 标识TCC try阶段开始执行的标识
     */
    private static final String TRY_START = "TRY_START";

    /**
     * 标识TCC try阶段执行成功的标识
     */
    private static final String TRY_SUCCESS = "TRY_SUCCESS";

    /**
     * 保存TCC事务执行过程的状态
     */
    private static Map<Class<?>, Map<String, String>> map =
            new ConcurrentHashMap<Class<?>, Map<String, String>>();

    /**
     * 标记try阶段开始执行
     *
     * @param tccClass
     * @param bizKey   业务唯一标识
     * @param xid
     */
    public static void tagTryStart(Class<?> tccClass, String bizKey, String xid) {
        setResult(tccClass, bizKey, xid, TRY_START);
    }

    /**
     * 标记try阶段执行成功
     *
     * @param tccClass
     * @param xid
     */
    public static void tagTrySuccess(Class<?> tccClass, String bizKey, String xid) {
        setResult(tccClass, bizKey, xid, TRY_SUCCESS);
    }

    /**
     * 判断标识是否为空
     *
     * @param tccClass
     * @param xid
     * @return
     */
    public static boolean isTagNull(Class<?> tccClass, String bizKey, String xid) {
        String v = getResult(tccClass, bizKey, xid);
        if (StringUtils.isBlank(v)) {
            return true;
        }
        return false;
    }

    /**
     * 判断try阶段是否执行成功
     *
     * @param tccClass
     * @param xid
     * @return
     */
    public static boolean isTrySuccess(Class<?> tccClass, String bizKey, String xid) {
        String v = getResult(tccClass, bizKey, xid);
        if (StringUtils.isNotBlank(v) && TRY_SUCCESS.equals(v)) {
            return true;
        }
        return false;
    }


    public static void setResult(Class<?> tccClass, String bizKey, String xid, String v) {
        Map<String, String> results = map.get(tccClass);

        if (results == null) {
            synchronized (map) {
                if (results == null) {
                    results = new ConcurrentHashMap<>();
                    map.put(tccClass, results);
                }
            }
        }

        results.put(getTccExecution(xid, bizKey), v);//保存当前分布式事务id
    }

    public static String getResult(Class<?> tccClass, String bizKey, String xid) {
        Map<String, String> results = map.get(tccClass);
        if (results != null) {
            return results.get(getTccExecution(xid, bizKey));
        }

        return null;
    }


    public static void removeResult(Class<?> tccClass, String bizKey, String xid) {
        Map<String, String> results = map.get(tccClass);
        if (results != null) {
            results.remove(getTccExecution(xid, bizKey));
        }
    }

    private static String getTccExecution(String xid, String bizKey) {
        return xid + "::" + bizKey;
    }
}