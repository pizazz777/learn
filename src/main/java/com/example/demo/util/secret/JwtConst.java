package com.example.demo.util.secret;

/**
 * @author sqm
 * @date 2019/02/12
 * @description 类说明：Json Web Token 常量
 */
public class JwtConst {

    private JwtConst() {
    }

    /**
     * token key
     */
    public static final String AUTHORIZATION = "Authorization";

    /**
     * jwt
     */
    public static final String JWT_ID = "jwt";
    /**
     * 密钥
     */
    public static final String JWT_SECRET = "7786df7fc3a34e26a61c034d5ec8245d";
    /**
     * 存活时间,毫秒值
     */
    public static final int JWT_TTL = 60 * 60 * 1000;
    /**
     * 内部刷新时间,毫秒值
     */
    public static final int JWT_REFRESH_INTERVAL = 55 * 60 * 1000;
    /**
     * 刷新存活时间,毫秒值
     */
    public static final int JWT_REFRESH_TTL = 12 * 60 * 60 * 1000;
}
