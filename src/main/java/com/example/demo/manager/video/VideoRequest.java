package com.example.demo.manager.video;

import com.example.demo.component.exception.VideoException;
import com.example.demo.constant.file.VideoEnum;
import com.example.demo.entity.upload.UploadFileDO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Administrator
 * @date 2020-08-10 09:49
 * @description: 视频转换操作类
 */
public interface VideoRequest {

    /**
     * 视频转换
     *
     * @param file 上传的视频文件对象
     * @param type 转换之后的类型
     * @return 转换之后的文件对象
     * @throws VideoException e
     */
    UploadFileDO convert(MultipartFile file, VideoEnum type) throws VideoException;

    /**
     * 视频转换
     *
     * @param videoPath 视频路径
     * @param type      转换之后的类型
     * @return 转换之后的文件对象
     * @throws VideoException e
     */
    UploadFileDO convert(String videoPath, VideoEnum type) throws VideoException;


}
