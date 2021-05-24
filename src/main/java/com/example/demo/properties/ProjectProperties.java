package com.example.demo.properties;

import com.example.demo.constant.sys.LoginTypeEnum;
import com.google.common.collect.Lists;
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
@ConfigurationProperties(prefix = "project")
public class ProjectProperties {

    /**
     * 项目包路径
     */
    private String projectPackage;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 是否开启swagger
     */
    private Boolean enableSwagger;
    /**
     * 项目描述
     */
    private String description;
    /**
     * 项目版本
     */
    private String version;
    /**
     * 服务条款
     */
    private String termsOfServiceUrl;
    /**
     * 联系名称
     */
    private String contactName;
    /**
     * 联系地址
     */
    private String contactUrl;
    /**
     * 联系邮箱
     */
    private String contactEmail;
    /**
     * 允许登录方式
     */
    private LoginTypeEnum loginTypeEnum;
    /**
     * 登录失效时间(毫秒),默认1小时
     */
    private long sessionTimeout = 3600000L;
    /**
     * 记住我时间(秒),默认7天
     */
    private int rememberMe = 604800;
    /**
     * 被其他服务器反向代理
     */
    private Boolean reverseProxyByOtherServer = false;
    /**
     * 权限
     */
    private Auth auth;
    /**
     * json web token
     */
    private JWT jwt;
    /**
     * 文件上传
     */
    private File file;
    /**
     * 日志
     */
    private Log log;
    /**
     * 缓存
     */
    private Cache cache;
    /**
     * Elasticsearch全文搜索
     */
    private Elasticsearch elasticsearch;
    /**
     * office
     */
    private Office office;
    /**
     * 音视频
     */
    private Video video;

    @Data
    public static class Video {

        // 1.下载ffmpeg 网址:http://ffmpeg.org/download.html#build-windows
        // 2.解压,将bin目录设置到环境变量path中,cmd执行ffmpeg -version验证安装成功
        /**
         * ffmpeg安装路径(只能操作asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等格式)
         */
        private String ffmpegPath;
        /**
         * MEncoder安装路径(操作wmv9，rm，rmvb等格式转为avi再用ffmpeg操作)
         */
        private String mEncoderPath;

    }

    @Data
    public static class Office {

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

    @Data
    public static class Elasticsearch {

        /**
         * Elasticsearch需要扫描的注解包,用来在项目启动的时候根据类的注解生成index,analyzer等
         */
        private List<String> scanPackageList;
        /**
         * Elasticsearch index自动生成策略, true:存在先删除再重新设置, false:存在跳过
         */
        private Boolean esIndexSchemaUpdate = false;
        /**
         * 是否跳过生成
         */
        private Boolean skip = false;

    }

    @Data
    public static class Cache {

        /**
         * key前缀
         */
        private String prefixCacheName;
        /**
         * 超时时间(单位:秒)
         */
        private int timeoutOfSeconds = 60 * 60 * 24;

    }

    @Data
    public static class Log {

        /**
         * 日志展示时是否获取用户信息
         */
        private Boolean showUserInfo = true;
        /**
         * 是否记录到数据库
         */
        private Boolean writeToDatabase = true;

    }

    @Data
    public static class File {

        /**
         * 上传文件系统目录
         */
        private String uploadFileDir;
        /**
         * 上传文件访问地址前缀
         */
        private String uploadFileUrl;

    }

    @Data
    public static class JWT {

        /**
         * Jwt接收方:
         */
        private String audience = "defaultAudience";
        /**
         * Jwt签发方:
         */
        private String issuer = "Administrator";
        /**
         * JWT签名过期时间(毫秒)
         */
        private Long jwtTimeToLive = 3600000L;

    }

    @Data
    public static class Auth {

        /**
         * 测试模式
         */
        private Boolean testModel = false;
        /**
         * 管理员账号密码
         */
        private String managerName = "admin";
        private String managerPassword = "123456";
        /**
         * 是否是后台管理系统
         */
        private Boolean backgroundManageSystem;
        /**
         * 登录方式: 可选有 -> 账号,邮箱,手机号码
         */
        private List<LoginTypeEnum> loginTypeEnumList = Lists.newArrayList(LoginTypeEnum.ACCOUNT);
        /**
         * 系统中是否需要对手机号码进行验证
         */
        private Boolean checkMobile;
        /**
         * 开启缓存
         */
        private Boolean cachingEnabled = true;
        /**
         * 开启授权缓存
         */
        private Boolean authorizationCachingEnabled = true;
        /**
         * 开启认证缓存
         */
        private Boolean authenticationCachingEnabled = true;

    }


}
