package com.example.demo.manager.video.impl;

import com.example.demo.constant.file.FileTypeEnum;
import com.example.demo.entity.upload.UploadFileDO;
import com.example.demo.manager.file.UploadFileRequest;
import com.example.demo.manager.video.VideoRequest;
import com.example.demo.properties.ProjectProperties;
import com.google.common.collect.Lists;
import com.huang.exception.VideoException;
import com.huang.util.file.FileUtil;
import com.huang.util.io.IOUtil;
import com.huang.util.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Administrator
 * @date 2020-08-10 10:38
 * @description: 视频转换操作类
 */
@Component
@Slf4j
public class VideoRequestImpl implements VideoRequest {

    private UploadFileRequest uploadFileRequest;
    private ProjectProperties projectProperties;

    @Autowired
    public VideoRequestImpl(UploadFileRequest uploadFileRequest, ProjectProperties projectProperties) {
        this.uploadFileRequest = uploadFileRequest;
        this.projectProperties = projectProperties;
    }

    /**
     * 视频转换
     *
     * @param file 上传的视频文件对象
     * @param type 转换之后的类型
     * @return 转换之后的文件对象
     * @throws VideoException e
     */
    @Override
    public UploadFileDO convert(MultipartFile file, FileTypeEnum type) throws VideoException {
        try (InputStream inputStream = file.getInputStream()) {
            return convert(inputStream, type);
        } catch (IOException e) {
            throw new VideoException(e.getMessage());
        }
    }

    /**
     * 视频转换
     *
     * @param videoPath 视频路径
     * @param type      转换之后的类型
     * @return 转换之后的文件对象
     * @throws VideoException e
     */
    @Override
    public UploadFileDO convert(String videoPath, FileTypeEnum type) throws VideoException {
        checkType(videoPath);
        return convertByFfmpeg(videoPath, type);
    }


    /**
     * 视频转换  使用zip批量转
     *
     * @param file zip包
     * @param type 转换之后的类型
     * @return 转换之后的文件对象
     * @throws VideoException e
     */
    @Override
    public List<UploadFileDO> convert(File file, FileTypeEnum type) throws VideoException {
        List<UploadFileDO> list = Lists.newArrayList();
        // 使用GBK防止中文文件名乱码
        try (ZipFile zipFile = new ZipFile(file, Charset.forName("GBK"))) {
            Enumeration<? extends ZipEntry> zipEntryEnum = zipFile.entries();
            ZipEntry zipEntry;
            while (zipEntryEnum.hasMoreElements()) {
                zipEntry = zipEntryEnum.nextElement();
                if (!zipEntry.isDirectory()) {
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    list.add(convert(inputStream, type));
                }
            }
        } catch (IOException e) {
            throw new VideoException(e.getMessage());
        }
        return list;
    }

    /**
     * 使用javacv转换视频格式
     *
     * @param inputStream 输入流
     * @param type        转换后的类型
     * @return 服务器的文件对象
     * @throws VideoException e
     */
    private UploadFileDO convert(InputStream inputStream, FileTypeEnum type) throws VideoException {
        try (FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inputStream)) {
            UploadFileDO emptyFile = uploadFileRequest.generateEmptyFile(type.getSuffix());
            Optional.ofNullable(emptyFile).orElseThrow(() -> new VideoException("不支持该文件类型"));
            String outputPath = emptyFile.getPath();
            FFmpegFrameRecorder recorder;
            frameGrabber.start();
            recorder = new FFmpegFrameRecorder(outputPath, frameGrabber.getImageWidth(), frameGrabber.getImageHeight(), frameGrabber.getAudioChannels());
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFormat(FileUtil.getSuffix(outputPath));
            recorder.setFrameRate(frameGrabber.getFrameRate());
            recorder.setSampleRate(frameGrabber.getSampleRate());
            recorder.setAudioChannels(frameGrabber.getAudioChannels());
            recorder.setFrameRate(frameGrabber.getFrameRate());
            recorder.start();
            Frame capturedFrame;
            while ((capturedFrame = frameGrabber.grabFrame()) != null) {
                recorder.setTimestamp(frameGrabber.getTimestamp());
                recorder.record(capturedFrame);
            }
            recorder.stop();
            recorder.release();
            frameGrabber.stop();

            emptyFile.setSize(emptyFile.getSize());
            uploadFileRequest.update(emptyFile);
            return emptyFile;
        } catch (Exception e) {
            throw new VideoException(e.getMessage());
        }
    }

    private UploadFileDO convertByFfmpeg(String videoPath, FileTypeEnum type) throws VideoException {
        List<String> command = Lists.newArrayList();
        command.add(projectProperties.getVideo().getFfmpegPath());
        command.add("-i");
        command.add(videoPath);
        UploadFileDO emptyFile;
        try {
            emptyFile = uploadFileRequest.generateEmptyFile(type.getSuffix());
            command.add(emptyFile.getPath());
            ProcessBuilder builder = new ProcessBuilder();
            Process process = builder.command(command).redirectErrorStream(true).start();
            ExecutorService pool = ThreadUtil.defaultThreadPool();
            pool.execute(() -> print(process.getInputStream()));
            pool.execute(() -> print(process.getErrorStream()));
            ThreadUtil.exit(pool);
            process.waitFor();
            return emptyFile;
        } catch (Exception e) {
            throw new VideoException(e.getMessage());
        }
    }

    private void checkType(String videoPath) throws VideoException {
        boolean flag = Arrays.stream(FileTypeEnum.values()).map(FileTypeEnum::getSuffix).anyMatch(type -> Objects.equals(type, FileUtil.getSuffix(videoPath)));
        if (!flag) {
            throw new VideoException("不支持的视频转换类型");
        }
    }

    private void print(InputStream inputStream) {
        try {
            byte[] bytes = IOUtil.toByteArray(inputStream);
            log.debug(new String(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
