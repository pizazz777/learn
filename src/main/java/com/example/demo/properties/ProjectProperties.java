package com.example.demo.properties;

import com.example.demo.constant.sys.LoginTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2020-04-28 16:31
 */
@Data
@Component
@ConfigurationProperties(prefix = "project")
public class ProjectProperties {

    /**
     * 项目包路径
     */
    private String projectPackage;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 是否开启swagger
     */
    private Boolean enableSwagger;
    /**
     * 项目描述
     */
    private String description;
    /**
     * 项目版本
     */
    private String version;
    /**
     * 服务条款
     */
    private String termsOfServiceUrl;
    /**
     * 联系名称
     */
    private String contactName;
    /**
     * 联系地址
     */
    private String contactUrl;
    /**
     * 联系邮箱
     */
    private String contactEmail;
    /**
     * 允许登录方式
     */
    private LoginTypeEnum loginTypeEnum;
    /**
     * 登录失效时间(毫秒),默认1小时
     */
    private long sessionTimeout = 3600000L;
    /**
     * 记住我时间(秒),默认7天
     */
    private int rememberMe = 604800;
    /**
     * 被其他服务器反向代理
     */
    private Boolean reverseProxyByOtherServer = false;
}
