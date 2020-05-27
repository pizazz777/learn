package com.example.demo.constant.cache;

/**
 * @author Administrator
 * @date 2020-04-30 14:36
 */
public class CacheConst {

    /**
     * 会话缓存的前缀
     */
    public static final String SESSION_CACHE_PREFIX = "session:cache:";
    /**
     * 权限缓存的前缀
     */
    public static final String AUTH_CACHE_PREFIX = "auth:cache:";
    /**
     * 权限缓存名
     */
    public static final String AUTHORIZATION_CACHE_NAME = "authorizationCache";
    /**
     * 认证缓存名
     */
    public static final String AUTHENTICATION_CACHE_NAME = "authenticationCache";
    /**
     * authorizationCache 权限缓存KEY
     */
    public static final String AUTHORIZATION_CACHE = AUTH_CACHE_PREFIX + AUTHORIZATION_CACHE_NAME;
    /**
     * authenticationCache 认证缓存KEY
     */
    public static final String AUTHENTICATION_CACHE = AUTH_CACHE_PREFIX + AUTHENTICATION_CACHE_NAME;

}
