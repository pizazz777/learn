package com.example.demo.advise;

import com.alibaba.fastjson.JSON;
import com.example.demo.annotation.log.Action;
import com.example.demo.constant.log.ActionLogEnum;
import com.example.demo.manager.log.ActionLogRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-05-15 11:40
 * @description: 操作日志切面类
 */
@Aspect
@Component
@Slf4j
public class ActionLogAdvise {

    private ActionLogRequest actionLogRequest;
    private HttpServletRequest request;

    @Autowired
    public ActionLogAdvise(ActionLogRequest actionLogRequest, HttpServletRequest request) {
        this.actionLogRequest = actionLogRequest;
        this.request = request;
    }

    /**
     * 定义切点
     * 使用@Action注解,作为切点表达式,切带指定注解的方法
     */
    @Pointcut("@annotation(com.example.demo.annotation.log.Action)")
    public void actionAspect() {
    }

    /**
     * 环绕通知  处理日志
     *
     * @param point point
     * @return r
     * @throws Throwable e
     */
    @Around(value = "actionAspect()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取方法上指定的注解
        Action action = method.getDeclaredAnnotation(Action.class);
        // 登录操作直接放行
        if (Objects.equals(action.type(), ActionLogEnum.LOGIN)) {
            return point.proceed();
        }
        LocalDateTime startTime = LocalDateTime.now();
        // 调用切入点方法
        Object result = point.proceed();
        LocalDateTime endTime = LocalDateTime.now();
        String param = JSON.toJSONString(request.getParameterMap());
        String type = action.type().name();
        // 保存操作日志
        actionLogRequest.saveActionLog(type, action.desc(), param, startTime, endTime);
        return result;
    }


}
