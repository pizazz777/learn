package com.example.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2020/08/10
 * @description: 视频转换操作类
 */
@Data
@Component
@ConfigurationProperties(prefix = "project.video")
public class VideoProperties {

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
