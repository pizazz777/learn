package com.example.demo.util.exception;

import com.example.demo.component.exception.ServiceException;
import org.apache.commons.lang3.ObjectUtils;

/**
 * @author Administrator
 * @date 2020-07-22 14:08
 * @description: 异常处理类, 处理自己指定的msg
 */
public class ExceptionUtil {

    /**
     * 非空异常判断
     *
     * @param obj 数据
     * @throws ServiceException e
     */
    public static void requireNonNull(Object obj) throws ServiceException {
        requireNonNull(obj, new ServiceException("数据为空"));
    }

    /**
     * 非空异常判断
     *
     * @param obj 数据
     * @param msg 异常信息
     * @throws ServiceException e
     */
    public static void requireNonNull(Object obj, String msg) throws ServiceException {
        requireNonNull(obj, new ServiceException(msg));
    }

    /**
     * 非空异常判断
     *
     * @param obj        数据
     * @param exceptions 指定异常
     * @throws T e
     */
    public static <T extends ServiceException> void requireNonNull(Object obj, T exceptions) throws T {
        if (ObjectUtils.isEmpty(obj)) {
            throw exceptions;
        }
    }


}


