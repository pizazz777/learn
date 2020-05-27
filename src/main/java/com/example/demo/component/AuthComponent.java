package com.example.demo.component;

import com.example.demo.component.exception.ServiceException;

/**
 * @author Administrator
 * @date 2020-04-29 11:
 * @description: 权限验证组件
 */
public interface AuthComponent {

    /**
     * shiro 登录认证, 成功返回 token
     *
     * @param username 用户名
     * @param password 密码
     * @return r
     * @throws ServiceException e
     */
    String loginReturnToken(String username, String password) throws ServiceException;


    /**
     * 获取主要验证信息: 用户对象或自增id
     *
     * @param clazz 类型
     * @param <T>   p
     * @return r
     * @throws ServiceException e
     */
    <T> T getPrimaryPrincipal(Class<T> clazz) throws ServiceException;


    /**
     * 获取数据库内密码保存的形式的密码
     *
     * @param password 密码
     * @return 加密后的密码
     */
    String getPasswordInDb(String password);

}
