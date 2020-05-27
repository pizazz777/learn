package com.example.demo.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2020-04-30 15:24
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "project.file")
public class FileProperties {

    /**
     * 上传文件系统目录
     */
    private String uploadFileDir;
    /**
     * 上传文件访问地址前缀
     */
    private String uploadFileUrl;

}
