package com.example.demo.manager.log;

import com.example.demo.entity.log.ExceptionLogDO;
import com.example.demo.manager.BaseRequest;
import com.huang.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author Administrator
 * @version 1.0.0
 * @date 2020/05/15
 * @description: 异常日志保存
 */
public interface ExceptionLogRequest extends BaseRequest<ExceptionLogDO> {

    /**
     * 保存错误信息
     *
     * @param request 请求
     * @param e       异常
     * @throws ServiceException e
     */
    void saveExceptionLog(HttpServletRequest request, Throwable e) throws ServiceException;

    /**
     * 保存错误信息 自定义message
     *
     * @param request 请求
     * @param msg     自定义message
     * @param e       异常
     * @throws ServiceException e
     */
    void saveExceptionLog(HttpServletRequest request, String msg, Throwable e) throws ServiceException;

    /**
     * 保存错误信息 自定义message
     *
     * @param request 请求
     * @param msg     自定义message
     * @throws ServiceException e
     */
    void saveExceptionLog(HttpServletRequest request, String msg) throws ServiceException;

    /**
     * 异步处理异常记录
     *
     * @param throwable 抛出的异常
     * @param method    方法
     * @param objects   参数
     * @throws ServiceException e
     */
    void saveAsyncExceptionLog(Throwable throwable, Method method, Object... objects) throws ServiceException;

}
