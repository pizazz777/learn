package com.example.demo.configuration;


import com.example.demo.properties.ProjectProperties;
import lombok.extern.slf4j.Slf4j;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.ExternalOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author administrator
 * @date 2020/07/17
 * @description: open office配置类
 **/
@Configuration
@EnableConfigurationProperties(ProjectProperties.class)
@Slf4j
public class OpenOfficeConfig {

    private ProjectProperties projectProperties;

    public OpenOfficeConfig(ProjectProperties projectProperties) {
        this.projectProperties = projectProperties;
    }

    /**
     * 连接OpenOffice.org 并且启动OpenOffice.org
     *
     * @return OfficeManager
     */
    @Bean
    @ConditionalOnMissingBean
    public OfficeManager officeManager() {
        // 连接已经存在的服务
        try {
            ExternalOfficeManagerConfiguration externalOfficeManagerConfiguration = new ExternalOfficeManagerConfiguration();
            externalOfficeManagerConfiguration.setConnectOnStart(true);
            externalOfficeManagerConfiguration.setPortNumber(projectProperties.getOffice().getOpenOffice().getPort());
            OfficeManager buildOfficeManager = externalOfficeManagerConfiguration.buildOfficeManager();
            buildOfficeManager.start();
            return buildOfficeManager;
        } catch (OfficeException e) {
            log.debug("office没有已经启动的服务!尝试开启openOffice服务");
        }
        // open office 配置类
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        // 设置安装路径
        configuration.setOfficeHome(projectProperties.getOffice().getOpenOffice().getPath());
        // 设置端口号
        configuration.setPortNumber(projectProperties.getOffice().getOpenOffice().getPort());
        // 设置转换超时时间，5分钟
        configuration.setTaskExecutionTimeout(1000 * 60 * 5L);
        // 设置队列超时时间 1天
        configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
        // 开启转换服务
        OfficeManager officeManager = configuration.buildOfficeManager();
        officeManager.start();
        return officeManager;
    }

    /**
     * 获取转换器
     *
     * @param officeManager 管理器
     * @return OfficeDocumentConverter
     */
    @Bean
    @ConditionalOnMissingBean
    public OfficeDocumentConverter officeDocumentConverter(OfficeManager officeManager) {
        return new OfficeDocumentConverter(officeManager);
    }

}


