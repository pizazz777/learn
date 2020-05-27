package com.example.demo.exception;

import com.example.demo.component.exception.ServiceException;

/**
 * @author sqm
 * @version 1.0
 * @date 2019/06/04
 * @description: 类描述: {@link com.example.demo.component.response.ResCode#ILLEGAL_PARAM} 非法参数
 **/
public class IllegalParamException extends ServiceException {

    private static final long serialVersionUID = -4162585140358132636L;

    public IllegalParamException() {
    }

    public IllegalParamException(String message) {
        super(message);
    }
}
