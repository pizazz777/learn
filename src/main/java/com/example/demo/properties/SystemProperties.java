package com.example.demo.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2020-05-18 13:37
 * @description: 用于获取系统配置
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "project.system")
public class SystemProperties {

    /**
     * key前缀
     */
    private String prefixCacheName = "sys:conf";
    /**
     * 超时时间(单位:秒)
     */
    private int timeoutOfSeconds = 60 * 60 * 24;


}
