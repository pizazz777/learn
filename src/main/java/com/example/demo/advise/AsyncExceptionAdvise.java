package com.example.demo.advise;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.manager.log.ExceptionLogRequest;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 * @author Administrator
 * @version 1.0
 * @date 2020/05/15
 * @description: 异步异常无发返回值的捕获
 */
@Component
public class AsyncExceptionAdvise implements AsyncConfigurer, AsyncUncaughtExceptionHandler {

    private ExceptionLogRequest exceptionLogRequest;

    @Autowired
    public AsyncExceptionAdvise(ExceptionLogRequest exceptionLogRequest) {
        this.exceptionLogRequest = exceptionLogRequest;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionAdvise(exceptionLogRequest);
    }

    @Override
    public void handleUncaughtException(@Nonnull Throwable throwable, @Nonnull Method method, @Nonnull Object... objects) {
        try {
            exceptionLogRequest.saveAsyncExceptionLog(throwable, method, objects);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
