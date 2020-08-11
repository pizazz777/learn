package com.example.demo.constant.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Administrator
 * @date 2020-08-10 10:22
 * @description: 支持使用ffmpeg转换的视频格式,不支持的先用别的工具(mencoder)转换为avi再操作
 */
@Getter
@AllArgsConstructor
public enum VideoEnum {

    ASX("asx"),
    ASF("asf"),
    MPG("mpg"),
    WMV("wmv"),
    _3GP("3gp"),
    MP4("mp4"),
    MOV("mov"),
    AVI("avi"),
    FLV("flv");

    private String suffix;

}
