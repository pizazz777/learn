package com.example.demo.annotation.elasticsearch;

import java.lang.annotation.*;

import static com.example.demo.constant.elasticsearch.IndexConst.DEFAULT_INDEX_NAME;

/**
 * @author Administrator
 * @date 2020-07-10 09:51
 * @description: elasticsearch注解 配置index
 */
// @Inherited: 被该注解修饰的类,在子类继承的时候可以将注解继承
// @Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Document {

    /**
     * Name of the Elasticsearch index.
     * <ul>
     * <li>Lowercase only</li>
     * <li><Cannot include \, /, *, ?, ", <, >, |, ` ` (space character), ,, #/li>
     * <li>Cannot start with -, _, +</li>
     * <li>Cannot be . or ..</li>
     * <li>Cannot be longer than 255 bytes (note it is bytes, so multi-byte characters will count towards the 255 limit
     * faster)</li>
     * </ul>
     */
    String indexName() default DEFAULT_INDEX_NAME;

}
