package com.example.demo.configuration;

import com.example.demo.advise.ActionLogAdvise;
import com.example.demo.advise.AsyncExceptionAdvise;
import com.example.demo.advise.ExceptionLogControllerAdvice;
import com.example.demo.component.AuthComponent;
import com.example.demo.dao.log.ExceptionLogDao;
import com.example.demo.manager.log.ActionLogRequest;
import com.example.demo.manager.log.ExceptionLogRequest;
import com.example.demo.manager.log.impl.ExceptionLogRequestImpl;
import com.example.demo.properties.ProjectProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * @date 2020-05-15 16:56
 * @description: 如果容器中不存在指定bean, 就注入, 存在就不注入
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(ProjectProperties.class)
public class LogConfig {

    @Bean
    @ConditionalOnMissingBean
    public ActionLogAdvise actionLogAdvise(ActionLogRequest actionLogRequest, HttpServletRequest request) {
        return new ActionLogAdvise(actionLogRequest, request);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionLogRequest exceptionLogRequest(ProjectProperties projectProperties, AuthComponent authComponent, ExceptionLogDao exceptionLogDao) {
        return new ExceptionLogRequestImpl(projectProperties, authComponent, exceptionLogDao);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionLogControllerAdvice exceptionControllerAdvice(ExceptionLogRequest exceptionLogRequest, HttpServletRequest request) {
        return new ExceptionLogControllerAdvice(exceptionLogRequest, request);
    }

    @Bean
    @ConditionalOnMissingBean
    public AsyncExceptionAdvise asyncExceptionAdvise(ExceptionLogRequest exceptionLogRequest) {
        return new AsyncExceptionAdvise(exceptionLogRequest);
    }

}
