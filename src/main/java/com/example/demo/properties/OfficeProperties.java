package com.example.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2020/07/17
 * @description: 用于获取办公文档配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "project.office")
public class OfficeProperties {


    private OpenOffice openOffice;

    private pdf pdf;

    @Data
    public static class OpenOffice {

        /**
         * Open Office 安装路径
         * Windows 安装目录如: C:/Program Files (x86)/OpenOffice 4
         * Mac 安装目录如: /Applications/OpenOffice.app/Contents
         * Linux 安装目录如:/opt/openoffice.org4
         */
        private String path;
        /**
         * open office端口,默认8100
         */
        private Integer port = 8100;

    }

    @Data
    public static class pdf {

        /**
         * 字体目录
         */
        private String pdfFontsPath;
        /**
         * 模板目录
         */
        private String pdfTemplatePath;
        /**
         * 模板的名称
         */
        private String htmlToPdfTemplateName = "html-pdf.ftl";
    }

}
