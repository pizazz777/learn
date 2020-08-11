package com.example.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-04-28 16:31
 */
@Data
@Component
@ConfigurationProperties(prefix = "project.elasticsearch")
public class ElasticsearchProperties {

    /**
     * Elasticsearch需要扫描的注解包,用来在项目启动的时候根据类的注解生成index,analyzer等
     */
    private List<String> scanPackageList;
    /**
     * Elasticsearch index自动生成策略, true:存在先删除再重新设置, false:存在跳过
     */
    private Boolean esIndexSchemaUpdate = false;
}
