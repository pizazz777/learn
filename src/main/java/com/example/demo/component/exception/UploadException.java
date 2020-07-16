package com.example.demo.component.exception;

/**
 * @author administrator
 * @date 2020/07/16
 * @description: 类描述: 上传异常类
 **/
public class UploadException extends ServiceException {

    private static final long serialVersionUID = -7301941544350999619L;

    public UploadException() {
    }

    public UploadException(String message) {
        super(message);
    }
}
