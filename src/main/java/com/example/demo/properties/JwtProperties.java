package com.example.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2020-04-30 10:10
 */
@Data
@Component
@ConfigurationProperties(prefix = "project.jwt")
public class JwtProperties {

    /**
     * Jwt接收方:
     */
    private String audience = "defaultAudience";
    /**
     * Jwt签发方:
     */
    private String issuer = "Administrator";
    /**
     * JWT签名过期时间(毫秒)
     */
    private Long jwtTimeToLive = 3600000L;


}
