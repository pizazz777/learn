package com.example.demo.manager.system.impl;

import com.example.demo.dao.system.SysConfigDao;
import com.example.demo.entity.system.SysConfigDO;
import com.example.demo.manager.system.SysConfigRequest;
import com.example.demo.properties.SystemProperties;
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
public class SysConfigRequestImpl implements SysConfigRequest {

    private SystemProperties systemProperties;
    private RedisTemplate<String, Object> redisTemplate;
    private SysConfigDao sysConfigDao;

    @Autowired
    public SysConfigRequestImpl(SystemProperties systemProperties,
                                RedisTemplate<String, Object> redisTemplate,
                                SysConfigDao sysConfigDao) {
        this.systemProperties = systemProperties;
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
        return systemProperties.getPrefixCacheName() + ":" + key;
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
        return Duration.ofSeconds(new Random().nextInt(systemProperties.getTimeoutOfSeconds()) + (60 * 60 * 12));
    }

}
