package com.example.demo.properties;

import com.example.demo.constant.sys.LoginTypeEnum;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-04-29 15:19
 */
@Data
@Component
@ConfigurationProperties(prefix = "project.auth")
public class AuthProperties {

    /**
     * 测试模式
     */
    private Boolean testModel = false;
    /**
     * 管理员账号密码
     */
    private String managerName = "admin";
    private String managerPassword = "123456";
    /**
     * 是否是后台管理系统
     */
    private Boolean backgroundManageSystem;
    /**
     * 登录方式: 可选有 -> 账号,邮箱,手机号码
     */
    private List<LoginTypeEnum> loginTypeEnumList = Lists.newArrayList(LoginTypeEnum.ACCOUNT);
    /**
     * 系统中是否需要对手机号码进行验证
     */
    private Boolean checkMobile;
    /**
     * 开启缓存
     */
    private Boolean cachingEnabled = true;
    /**
     * 开启授权缓存
     */
    private Boolean authorizationCachingEnabled = true;
    /**
     * 开启认证缓存
     */
    private Boolean authenticationCachingEnabled = true;

}
