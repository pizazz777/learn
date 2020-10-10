package com.example.demo.annotation.log;

import com.example.demo.constant.log.ActionLogEnum;

import java.lang.annotation.*;

/**
 * @author administrator
 * @date 2020/05/15
 * @description: 类描述: 操作日志注解类,用于注解在需要操作日志的接口方法上
 **/
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {

    /**
     * 类型代码
     *
     * @return 类型代码
     */
    ActionLogEnum type();

    /**
     * 描述
     *
     * @return 描述
     */
    String desc() default "";

}
