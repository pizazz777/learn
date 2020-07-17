package com.example.demo.component.exception;

/**
 * @author administrator
 * @date 2020/07/16
 * @description: 类描述: Excel 表格异常类
 **/
public class ExcelException extends ServiceException {

    private static final long serialVersionUID = 8283846637421602065L;

    public ExcelException() {
    }

    public ExcelException(String message) {
        super(message);
    }
}
