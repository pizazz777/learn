package com.example.demo.configuration;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author Administrator
 * @date 2020-05-18 11:12
 * @description: 系统缓存配置类
 */
public class SystemConfig {

    private SystemConfig() {
    }

    /**
     * 系统名称
     */
    public static final String SYSTEM_NAME = "systemName";
    /**
     * 系统图标
     */
    public static final String SYSTEM_ICON_URL = "systemIconUrl";

    /**
     * 内部类维护单例
     */
    private static class SystemConfigFactory {
        private static final Set<String> INSTANCE = Sets.newHashSet();

        static {
            INSTANCE.add(SYSTEM_NAME);
            INSTANCE.add(SYSTEM_ICON_URL);
        }
    }

    /**
     * 获取系统配置Key
     *
     * @return set
     */
    public static Set<String> get() {
        return SystemConfigFactory.INSTANCE;
    }

}
