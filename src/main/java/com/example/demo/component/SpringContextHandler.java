package com.example.demo.component;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * @author administrator
 * @date 2020/05/15
 * @description: 类描述: 用于没有注册 bean 的类获取 spring 中的 bean {@link ApplicationContext#getBean(String)}
 **/
public class SpringContextHandler implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     *
     * @param applicationContext 上下文内容
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        context = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return context;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) context.getBean(name);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return context.getBean(clazz);
    }

    /**
     * 清除applicationContext静态变量.
     */
    public static void cleanApplicationContext() {
        context = null;
    }

    private static void checkApplicationContext() {
        if (Objects.isNull(context)) {
            throw new IllegalStateException("applicationContext 未注入,请注入 SpringContextHolder");
        }
    }
}
