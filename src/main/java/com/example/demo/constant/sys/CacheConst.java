package com.example.demo.constant.sys;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2020/08/24
 * @description 缓存KEY
 **/
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
    /**
     * 部门树缓存名称
     */
    public static final String SYS_DEPT_TREE = "sys:dept:tree";
    /**
     * 部门名称缓存名称
     */
    public static final String SYS_DEPT_NAME = "sys:dept:name";
    /**
     * 权限树缓存名称
     */
    public static final String SYS_PERMISSION_TREE = "sys:permission:tree";
    /**
     * 用户列表缓存名称
     */
    public static final String SYS_USER_LIST = "sys:user:list";
    /**
     * 自身权限范围缓存名称
     */
    public static final String SELF_DATA_PERMISSION = "self-data-permission";

}
