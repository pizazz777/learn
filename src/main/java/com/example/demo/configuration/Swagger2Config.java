package com.example.demo.configuration;

import com.example.demo.properties.ProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Administrator
 * @date 2020-04-28 16:27
 * @description: swagger2配置类
 */
@Configuration
@EnableSwagger2
public class Swagger2Config implements WebMvcConfigurer {

    private ProjectProperties projectProperties;

    @Autowired
    public Swagger2Config(ProjectProperties projectProperties) {
        this.projectProperties = projectProperties;
    }

    /**
     * 将 swagger-ui 的页面指向具体位置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @Bean("projectApi")
    public Docket projectApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 分组
                .groupName("project_api")
                .pathMapping("/")
                // api的基本信息
                .apiInfo(apiInfo())
                // select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现
                .select()
                .apis(RequestHandlerSelectors.basePackage(projectProperties.getProjectPackage()))
//                .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller.sys"))
                .paths(PathSelectors.any())
                .build()
                .enable(projectProperties.getEnableSwagger());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(projectProperties.getProjectName())
                .description(projectProperties.getDescription())
                .version(projectProperties.getVersion())
                .termsOfServiceUrl(projectProperties.getTermsOfServiceUrl())
                .contact(new Contact(projectProperties.getContactName(), projectProperties.getContactUrl(), projectProperties.getContactEmail()))
                .build();
    }

}
