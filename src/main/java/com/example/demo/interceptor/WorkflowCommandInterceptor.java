package com.example.demo.interceptor;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;
import org.activiti.spring.SpringTransactionInterceptor;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Administrator
 * @date 2020-05-07 13:41
 * @description: 工作流自定义拦截器, 使用责任链模式
 */
public class WorkflowCommandInterceptor extends SpringTransactionInterceptor {

    public WorkflowCommandInterceptor(PlatformTransactionManager transactionManager) {
        super(transactionManager);
    }

    public <T> T execute(final CommandConfig config, final Command<T> command) {
        System.out.println("自定义拦截器 " + command.getClass().getName());
        return super.execute(config, command);
    }

}
