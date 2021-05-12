package com.example.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2020-05-18 13:37
 * @description: 用于获取系统配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "project.cache")
public class CacheProperties {

    /**
     * key前缀
     */
    private String prefixCacheName;
    /**
     * 超时时间(单位:秒)
     */
    private int timeoutOfSeconds = 60 * 60 * 24;


}
