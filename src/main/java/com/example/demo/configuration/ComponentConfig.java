package com.example.demo.configuration;

import com.example.demo.component.converter.LocalDateTimeFormatter;
import com.example.demo.properties.FileProperties;
import com.example.demo.properties.ProjectProperties;
import com.example.demo.util.secret.JwtConst;
import com.example.demo.util.time.DateConst;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-04-30 15:13
 */
@Configuration
@EnableConfigurationProperties(ProjectProperties.class)
public class ComponentConfig implements WebMvcConfigurer {

    private ProjectProperties projectProperties;
    private FileProperties fileProperties;

    @Autowired
    public ComponentConfig(ProjectProperties projectProperties, FileProperties fileProperties) {
        this.projectProperties = projectProperties;
        this.fileProperties = fileProperties;
    }

    /**
     * String → LocalDateTime  符合 yyyy-MM-dd 或者 yyyy-MM-dd HH:mm:ss
     * Formatter 与 Converter 可直接注册, 注册方式: ConversionService
     */
    @Bean
    public LocalDateTimeFormatter localDateTimeFormatter() {
        return new LocalDateTimeFormatter();
    }

    /**
     * 跨域设置(配置 shiro 后需单独配置跨域过滤器{@link ShiroConfig#corsFilter()})
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(CorsConfiguration.ALL)
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.OPTIONS.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name())
                .allowedHeaders("Content-Type", "x-requested-with", "X-Custom-Header", JwtConst.AUTHORIZATION)
                .exposedHeaders("Content-Disposition")
                // 响应头表示是否可以将对请求的响应暴露给页面。返回true则可以，其他值均不可以。
                // Credentials可以是 cookies, authorization headers 或 TLS client certificates。
                .allowCredentials(true)
                // 最大缓存时长 默认1800-30分钟
                .maxAge(3600L);
    }

    /**
     * String 类型 http 信息转换器
     * 此处配置响应类型为 json
     */
    @Bean
    public HttpMessageConverter<String> stringResponseBodyToJsonConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        converter.setWriteAcceptCharset(false);
        return converter;
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(stringResponseBodyToJsonConverter());
    }

    /**
     * springboot 的 json 序列化与反序列化的 ObjectMapper 配置
     *
     * @return ObjectMapper
     */
    @Bean
    public ObjectMapper serializedObjectMapper() {
        List<Module> moduleList = Lists.newArrayList();
        // 配置 jdk8 的对象(如 Optional,BaseStream)序列化格式
        moduleList.add(new Jdk8Module());
        JavaTimeModule module = new JavaTimeModule();
        // 配置 LocalDateTime 的反序列化格式
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateConst.DEFAULT_DATE_TIME);
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        // 配置 LocalDateTime 的序列化格式
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DateConst.DEFAULT_DATE_TIME);
        module.addSerializer(LocalDateTime.class, localDateTimeSerializer);
        moduleList.add(module);
        return Jackson2ObjectMapperBuilder.json()
                .modules(moduleList)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    /**
     * 设定资源处理器
     *
     * @param registry 注册
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 如果没有被其他服务器反向代理,则自身映射资源文件
        if (projectProperties.getReverseProxyByOtherServer()) {
            File uploadDir = new File(fileProperties.getUploadFileDir());
            String uploadDirPath = uploadDir.getAbsolutePath();
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String uploadFileUrl = fileProperties.getUploadFileUrl();
            if (Objects.nonNull(uploadFileUrl)) {
                // 去除最后的结尾分隔符
                if (uploadFileUrl.endsWith("/")) {
                    uploadFileUrl = uploadFileUrl.substring(0, uploadFileUrl.length() - 1);
                }
                // 如果还包含分隔符,则是获取最后一段 url
                if (uploadFileUrl.contains("/")) {
                    uploadFileUrl = uploadFileUrl.substring(uploadFileUrl.lastIndexOf("/"));
                    if (!uploadDirPath.endsWith("/")) {
                        uploadDirPath = uploadDirPath + File.separator;
                    }
                    uploadDirPath = uploadDirPath.replace("/", File.separator);
                }
                registry.addResourceHandler(uploadFileUrl + "/**").addResourceLocations("file:" + uploadDirPath);
            }
        }
    }

}
