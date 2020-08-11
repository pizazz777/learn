package com.example.demo.component.exception;

/**
 * @author administrator
 * @date 2020/08/10
 * @description: 类描述: 视频转换异常类
 **/
public class VideoException extends ServiceException {

    private static final long serialVersionUID = 6477549663130578787L;

    public VideoException() {
    }

    public VideoException(String message) {
        super(message);
    }
}
