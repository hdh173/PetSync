package com.coursework.petsync.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author HDH
 * @version 1.0
 */
/**
 * Redis 操作工具类
 * 提供常用缓存操作方法
 */
@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置缓存（使用 Duration）
     * @param key 键
     * @param value 值
     * @param minutes 过期时间（分钟）
     */
    public void set(String key, Object value, long minutes) {
        redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(minutes));
    }

    /**
     * 获取缓存值
     * @param key 键
     * @return 缓存中的值，或 null
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     * @param key 键
     * @return 是否删除成功
     */
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 判断 key 是否存在
     * @param key 键
     * @return 是否存在
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置 key 的过期时间（分钟）
     * @param key 键
     * @param minutes 分钟
     */
    public void expire(String key, long minutes) {
        redisTemplate.expire(key, Duration.ofMinutes(minutes));
    }

    /**
     * 自增 key 值并设置过期时间（分钟）
     * @param key 键
     * @param expireMinutes 过期时间（分钟）
     * @return 增加后的值
     */
    public Long incrementWithExpire(String key, long expireMinutes) {
        Long value = redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, Duration.ofMinutes(expireMinutes));
        return value;
    }

}