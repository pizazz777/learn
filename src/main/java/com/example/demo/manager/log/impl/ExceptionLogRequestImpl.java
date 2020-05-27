package com.example.demo.manager.log.impl;

import com.alibaba.fastjson.JSON;
import com.example.demo.component.AuthComponent;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.dao.log.ExceptionLogDao;
import com.example.demo.entity.log.ExceptionLogDO;
import com.example.demo.manager.log.ExceptionLogRequest;
import com.example.demo.properties.LogProperties;
import com.example.demo.util.ip.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author Administrator
 * @date 2020-05-15 15:50
 */
@Service
@Slf4j
public class ExceptionLogRequestImpl implements ExceptionLogRequest {

    private LogProperties logProperties;
    private AuthComponent authComponent;
    private ExceptionLogDao exceptionLogDao;

    @Autowired
    public ExceptionLogRequestImpl(LogProperties logProperties,
                                   AuthComponent authComponent,
                                   ExceptionLogDao exceptionLogDao) {
        this.logProperties = logProperties;
        this.authComponent = authComponent;
        this.exceptionLogDao = exceptionLogDao;
    }


    @Override
    public Integer save(ExceptionLogDO exceptionLog) throws ServiceException {
        return exceptionLogDao.save(exceptionLog);
    }

    /**
     * 保存错误信息
     *
     * @param request 请求
     * @param e       异常
     * @throws ServiceException e
     */
    @Override
    public void saveExceptionLog(HttpServletRequest request, Throwable e) throws ServiceException {
        saveExceptionLog(request, ExceptionUtils.getMessage(e), e);
    }

    /**
     * 保存错误信息 自定义message
     *
     * @param request 请求
     * @param msg     自定义message
     * @param e       异常
     * @throws ServiceException e
     */
    @Override
    public void saveExceptionLog(HttpServletRequest request, String msg, Throwable e) throws ServiceException {
        Long principal = authComponent.getPrimaryPrincipal(Long.class);
        ExceptionLogDO exceptionLog = ExceptionLogDO.builder()
                .message(msg)
                .identify(String.valueOf(principal))
                .className(e.getClass().getName())
                .stackTrace(ExceptionUtils.getStackTrace(e))
                .param(JSON.toJSONString(request.getParameterMap()))
                .ip(IpUtil.getIp(request))
                .uri(request.getRequestURI())
                .createTime(LocalDateTime.now())
                .build();
        if (logProperties.getWriteToDatabase()) {
            exceptionLogDao.save(exceptionLog);
        } else {
            log.debug(exceptionLog.toString());
        }
    }

    /**
     * 保存错误信息 自定义message
     *
     * @param request 请求
     * @param msg     自定义message
     * @throws ServiceException e
     */
    @Override
    public void saveExceptionLog(HttpServletRequest request, String msg) throws ServiceException {
        Long principal = authComponent.getPrimaryPrincipal(Long.class);
        ExceptionLogDO exceptionLog = ExceptionLogDO.builder()
                .message(msg)
                .identify(String.valueOf(principal))
                .className("自定义异常")
                .stackTrace("自定义异常")
                .param(JSON.toJSONString(request.getParameterMap()))
                .ip(IpUtil.getIp(request))
                .uri(request.getRequestURI())
                .createTime(LocalDateTime.now())
                .build();
        if (logProperties.getWriteToDatabase()) {
            exceptionLogDao.save(exceptionLog);
        } else {
            log.debug(exceptionLog.toString());
        }
    }

    /**
     * 异步处理异常记录
     *
     * @param throwable 抛出的异常
     * @param method    方法
     * @param objects   参数
     * @throws ServiceException e
     */
    @Override
    public void saveAsyncExceptionLog(Throwable throwable, Method method, Object... objects) throws ServiceException {
        ExceptionLogDO exceptionLog = ExceptionLogDO.builder()
                .message("异常请求异常" + throwable.getMessage())
                .className(method.getDeclaringClass() + ":" + method.getName())
                .stackTrace(ExceptionUtils.getStackTrace(throwable))
                .param(JSON.toJSONString(objects))
                .createTime(LocalDateTime.now())
                .build();
        if (logProperties.getWriteToDatabase()) {
            exceptionLogDao.save(exceptionLog);
        } else {
            log.debug(exceptionLog.toString());
        }
    }
}
