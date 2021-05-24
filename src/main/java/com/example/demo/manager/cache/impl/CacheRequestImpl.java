package com.example.demo.manager.cache.impl;

import com.example.demo.dao.system.SysConfigDao;
import com.example.demo.entity.system.SysConfigDO;
import com.example.demo.manager.cache.CacheRequest;
import com.example.demo.properties.ProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.Random;

/**
 * @author Administrator
 * @date 2020-05-18 11:17
 */
@Component
public class CacheRequestImpl implements CacheRequest {

    private ProjectProperties projectProperties;
    private RedisTemplate<String, Object> redisTemplate;
    private SysConfigDao sysConfigDao;

    @Autowired
    public CacheRequestImpl(ProjectProperties projectProperties,
                            RedisTemplate<String, Object> redisTemplate,
                            SysConfigDao sysConfigDao) {
        this.projectProperties = projectProperties;
        this.redisTemplate = redisTemplate;
        this.sysConfigDao = sysConfigDao;
    }

    /**
     * 获取缓存key
     *
     * @param key key
     * @return cache key
     */
    @Override
    public String getCacheKeyByKey(String key) {
        return projectProperties.getCache().getPrefixCacheName() + ":" + key;
    }


    /**
     * 存放值
     *
     * @param key   key
     * @param value value
     */
    @Override
    public void putValue(String key, Object value) {
        redisTemplate.opsForValue().set(getCacheKeyByKey(key), value, generateCacheTimeOut());
    }


    /**
     * 获取缓存值
     *
     * @param key key
     * @return value
     */
    @Override
    public Object getValueByKey(String key) {
        // 先获取真正的缓存key, 再从缓存获取值
        String cacheKey = this.getCacheKeyByKey(key);
        Object value = redisTemplate.opsForValue().get(cacheKey);
        // 缓存中不存在去数据库中查
        if (Objects.isNull(value)) {
            SysConfigDO sysConfig = sysConfigDao.getByKey(cacheKey);
            if (Objects.nonNull(sysConfig)) {
                // 数据库中存在再设置到缓存中
                redisTemplate.opsForValue().set(cacheKey, sysConfig.getConfigValue(), generateCacheTimeOut());
            }
        }
        return value;
    }


    /**
     * 删除缓存
     *
     * @param key key
     */
    @Override
    public void deleteByKey(String key) {
        redisTemplate.delete(this.getCacheKeyByKey(key));
    }


    /**
     * 生成缓存超时时间
     *
     * @return Duration
     */
    @Override
    public Duration generateCacheTimeOut() {
        return Duration.ofSeconds(new Random().nextInt(projectProperties.getCache().getTimeoutOfSeconds()) + (60 * 60 * 12));
    }

}
