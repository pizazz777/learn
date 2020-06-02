package com.example.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2020-05-29 14:06
 */
@Data
@Component
@ConfigurationProperties(prefix = "project.elasticsearch")
public class ElasticSearchProperties {


}
