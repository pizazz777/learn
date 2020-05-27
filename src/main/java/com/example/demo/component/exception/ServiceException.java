package com.example.demo.component.exception;

/**
 * @author sqm
 * @version 1.0
 * @date 2019/01/30
 * @description: 类描述: 逻辑服务异常类
 **/
public class ServiceException extends Exception {

    private static final long serialVersionUID = -9040267815228660186L;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

}
