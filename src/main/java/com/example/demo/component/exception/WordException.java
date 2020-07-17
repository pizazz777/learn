package com.example.demo.component.exception;

/**
 * @author administrator
 * @date 2020/07/16
 * @description: 类描述: word文档异常类
 **/
public class WordException extends ServiceException {

    private static final long serialVersionUID = 6477549663130578787L;

    public WordException() {
    }

    public WordException(String message) {
        super(message);
    }
}
