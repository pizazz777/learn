package com.example.demo.component.exception;

import org.apache.shiro.authz.AuthorizationException;

/**
 * @author administrator
 * @date 2020-04-29 17:18
 * @description: 无后台登录权限异常类
 */
public class NoBackgroundAuthException extends AuthorizationException {

    private static final long serialVersionUID = 4334020236005855562L;

    public NoBackgroundAuthException() {
        super();
    }

    public NoBackgroundAuthException(String message) {
        super(message);
    }
}
