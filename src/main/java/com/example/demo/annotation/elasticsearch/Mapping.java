package com.example.demo.annotation.elasticsearch;

import com.example.demo.constant.elasticsearch.AnalyzerTypeEnum;
import com.example.demo.constant.elasticsearch.FieldTypeEnum;

import java.lang.annotation.*;

/**
 * @author Administrator
 * @date 2020-07-10 09:51
 * @description: elasticsearch注解
 */
// @Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Mapping {

    /**
     * 类型
     */
    FieldTypeEnum type() default FieldTypeEnum.Auto;

    /**
     * 分词器类型
     */
    AnalyzerTypeEnum analyzer() default AnalyzerTypeEnum.ik_max_word;

    /**
     * 是否储存 todo 暂不实现
     */
    boolean store() default true;

    /**
     * 字段是否索引
     */
    boolean index() default true;

    /**
     * 时间字段日期格式化
     */
    String format() default "";


}
