package com.example.demo.manager.cache;

import java.time.Duration;

/**
 * @author Administrator
 * @date 2020-05-18 11:16
 */
public interface CacheRequest {

    /**
     * 获取缓存全限定名key
     *
     * @param key key
     * @return cache key
     */
    String getCacheKeyByKey(String key);

    /**
     * 存放值
     *
     * @param key   key
     * @param value value
     */
    void putValue(String key, Object value);

    /**
     * 获取缓存值
     *
     * @param key key
     * @return value
     */
    Object getValueByKey(String key);

    /**
     * 删除缓存
     *
     * @param key key
     */
    void deleteByKey(String key);


    /**
     * 生成缓存超时时间
     *
     * @return Duration
     */
    Duration generateCacheTimeOut();

}
