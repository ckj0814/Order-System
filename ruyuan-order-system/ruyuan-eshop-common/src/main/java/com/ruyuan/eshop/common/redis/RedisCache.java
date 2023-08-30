package com.ruyuan.eshop.common.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author zhonghuashishan
 */
public class RedisCache {

    private RedisTemplate redisTemplate;

    public RedisCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 缓存存储
     *
     * @param key
     * @param value
     * @param seconds 自动失效时间
     */
    public void set(String key, String value, int seconds) {

        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        if (seconds > 0) {
            vo.set(key, value, seconds, TimeUnit.SECONDS);
        } else {
            vo.set(key, value);
        }
    }

    /**
     * 缓存获取
     *
     * @param key
     * @return
     */
    public String get(String key) {
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        return vo.get(key);
    }

    /**
     * 缓存手动失效
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 判断hash key是否存在
     *
     * @param key
     * @return
     */
    public boolean hExists(String key) {
        return hGetAll(key).isEmpty();
    }

    /**
     * 获取hash变量中的键值对
     * 对应redis hgetall 命令
     *
     * @param key
     * @return
     */
    public Map<String, String> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 以map集合的形式添加hash键值对
     *
     * @param key
     * @param map
     */
    public void hPutAll(String key, Map<String, String> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 执行lua脚本
     *
     * @param script
     * @param keys
     * @param args
     * @param <T>
     * @return
     */
    public <T> T execute(RedisScript<T> script, List<String> keys, String... args) {
        return (T) redisTemplate.execute(script, keys, args);
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}
