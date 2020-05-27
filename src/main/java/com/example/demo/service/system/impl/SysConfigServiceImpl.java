package com.example.demo.service.system.impl;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResResult;
import com.example.demo.configuration.SystemConfig;
import com.example.demo.dao.system.SysConfigDao;
import com.example.demo.entity.system.SysConfigDO;
import com.example.demo.manager.system.SysConfigRequest;
import com.example.demo.service.system.SysConfigService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/05/18
 * @description: 类描述: 系统配置 Service
 **/
@Service
public class SysConfigServiceImpl implements SysConfigService, ApplicationRunner {

    private SysConfigRequest systemConfigRequest;
    private SysConfigDao sysConfigDao;
    private RedisTemplate<String, Object> redisTemplate;
    private Set<String> configNameSet = SystemConfig.get();

    @Autowired
    public SysConfigServiceImpl(SysConfigRequest systemConfigRequest,
                                SysConfigDao sysConfigDao,
                                RedisTemplate<String, Object> redisTemplate) {
        this.systemConfigRequest = systemConfigRequest;
        this.sysConfigDao = sysConfigDao;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取系统全部的配置
     *
     * @return r
     * @throws ServiceException e
     */
    @Override
    public ResResult get() throws ServiceException {
        Map<String, Object> configMap = Maps.newHashMap();
        SystemConfig.get().forEach(key -> configMap.put(key, systemConfigRequest.getValueByKey(key)));
        return ResResult.success(configMap);
    }

    /**
     * 获取配置
     *
     * @param key 键
     * @return r
     * @throws ServiceException e
     */
    @Override
    public ResResult getByKey(String key) throws ServiceException {
        return ResResult.success(systemConfigRequest.getValueByKey(key));
    }

    /**
     * 更改配置
     *
     * @param configMap 配置信息
     * @return r
     * @throws ServiceException e
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult updateConfig(Map<String, Object> configMap) throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        configMap.forEach((key, value) -> {
            SysConfigDO sysConfig = SysConfigDO.builder()
                    .configKey(key)
                    .configValue(value)
                    .updateTime(now)
                    .build();
            // 这里有可能会出现缓存一致性问题
            // 并发不高采用先更新数据库,再删除缓存足矣
            // 如果并发高可以采取订阅数据库的操作日志binlog,然后启用一段非业务代码通过消息队列操作
            if (sysConfigDao.update(sysConfig) > 0) {
                systemConfigRequest.deleteByKey(key);
            }
        });
        return ResResult.success();
    }


    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (String key : configNameSet) {
            SysConfigDO config = sysConfigDao.getByKey(key);
            if (Objects.isNull(config)) {
                // 数据库中无数据则保存至数据库
                LocalDateTime now = LocalDateTime.now();
                config = SysConfigDO.builder()
                        .configKey(key)
                        .configValue("")
                        .createTime(now)
                        .updateTime(now)
                        .build();
                sysConfigDao.save(config);
            }
            redisTemplate.opsForValue().set(systemConfigRequest.getCacheKeyByKey(key), config.getConfigValue(), systemConfigRequest.generateCacheTimeOut());
        }
    }

}
