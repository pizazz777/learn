package com.example.demo.manager.video.impl;

import com.example.demo.component.exception.VideoException;
import com.example.demo.constant.file.VideoEnum;
import com.example.demo.entity.upload.UploadFileDO;
import com.example.demo.manager.file.UploadFileRequest;
import com.example.demo.manager.video.VideoRequest;
import com.example.demo.properties.VideoProperties;
import com.example.demo.util.file.FileUtil;
import com.google.common.collect.Lists;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @date 2020-08-10 10:38
 * @description: 视频转换操作类
 */
@Component
public class VideoRequestImpl implements VideoRequest {

    private UploadFileRequest uploadFileRequest;
    private VideoProperties videoProperties;
    private final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            2,
            2,
            1000,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(5),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @Autowired
    public VideoRequestImpl(UploadFileRequest uploadFileRequest, VideoProperties videoProperties) {
        this.uploadFileRequest = uploadFileRequest;
        this.videoProperties = videoProperties;
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
    public UploadFileDO convert(MultipartFile file, VideoEnum type) throws VideoException {
        try (InputStream inputStream = file.getInputStream()) {
            UploadFileDO emptyFile = uploadFileRequest.generateEmptyFile(type.getSuffix());
            Optional.ofNullable(emptyFile).orElseThrow(() -> new VideoException("不支持该文件类型"));
            emptyFile.setSize(file.getSize());
            this.convert(inputStream, emptyFile.getPath());
            uploadFileRequest.update(emptyFile);
            return emptyFile;
        } catch (Exception e) {
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
    public UploadFileDO convert(String videoPath, VideoEnum type) throws VideoException {
        checkType(videoPath);
        return convertByFfmpeg(videoPath, type);
    }

    private UploadFileDO convertByFfmpeg(String videoPath, VideoEnum type) throws VideoException {
        List<String> command = Lists.newArrayList();
        command.add(videoProperties.getFfmpegPath());
        command.add("-i");
        command.add(videoPath);
        UploadFileDO emptyFile;
        try {
            emptyFile = uploadFileRequest.generateEmptyFile(type.getSuffix());
            command.add(emptyFile.getPath());
            ProcessBuilder builder = new ProcessBuilder();
            Process process = builder.command(command).redirectErrorStream(true).start();
            pool.execute(new OutputVideoStreamThread(process.getErrorStream()));
            pool.execute(new OutputVideoStreamThread(process.getInputStream()));
            process.waitFor();
            return emptyFile;
        } catch (Exception e) {
            throw new VideoException(e.getMessage());
        }
    }

    private void checkType(String videoPath) throws VideoException {
        boolean flag = Arrays.stream(VideoEnum.values()).map(VideoEnum::getSuffix).anyMatch(type -> Objects.equals(type, FileUtil.getSuffix(videoPath)));
        if (!flag) {
            throw new VideoException("不支持的视频转换类型");
        }
    }

    /**
     * 使用javacv转换视频格式
     *
     * @param inputStream 输入流
     * @param outputPath  输出路径
     * @throws Exception e
     */
    private void convert(InputStream inputStream, String outputPath) throws Exception {
        try (FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inputStream)) {
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
        }
    }

}
