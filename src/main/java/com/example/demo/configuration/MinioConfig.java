package com.example.demo.configuration;

import com.example.demo.properties.ProjectProperties;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/08/06
 * @description 文件对象存储配置类
 */
@Configuration
public class MinioConfig {

    private ProjectProperties projectProperties;

    @Autowired
    public MinioConfig(ProjectProperties projectProperties) {
        this.projectProperties = projectProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(projectProperties.getMinio().getEndPoint())
                .credentials(projectProperties.getMinio().getAccessKey(), projectProperties.getMinio().getSecretKey())
                .build();
    }


}
