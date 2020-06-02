package com.example.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2020-05-15 13:37
 */
@Data
@Component
@ConfigurationProperties(prefix = "project.log")
public class LogProperties {

    /**
     * 日志展示时是否获取用户信息
     */
    private Boolean showUserInfo = true;
    /**
     * 是否记录到数据库
     */
    private Boolean writeToDatabase = true;

}
