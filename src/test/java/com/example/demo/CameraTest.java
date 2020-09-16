package com.example.demo;

import com.example.demo.util.thread.ThreadUtil;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.*;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2020/09/16
 * @description 使用javaCV做视频推流/拉流
 */
public class CameraTest {

    private static final String FILE_ROOT_PATH = "D:/camera/";
    private static final String DEFAULT_IMAGE_TYPE = "jpg";
    private static final String DEFAULT_VIDEO_TYPE = "mp4";


    private Boolean flag = true;

    // 测试javaCV操作摄像头
    @Test
    public void javaCVTest() throws Exception {

        // 调用摄像头
        // camera();

        // 调用摄像头,录制视频
        video(FILE_ROOT_PATH + System.currentTimeMillis() + "." + DEFAULT_VIDEO_TYPE);
    }


    /**
     * 调用摄像头 使用OpenCV
     */
    private void camera() throws Exception {
        // 抓取视频帧
        // OpenCVFrameGrabber: 使用OpenCV方式打开抓取器
        // OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        // FrameGrabber: 会自己去找可以打开的摄像头的抓取器
        VideoInputFrameGrabber grabber = VideoInputFrameGrabber.createDefault(0);
        // 开始获取摄像头数据
        grabber.start();

        // 新建一个窗口
        CanvasFrame canvasFrame = new CanvasFrame("摄像头");
        // canvasFrame.setCanvasSize(1200, 800);
        // 设置窗口关闭时的动作
        canvasFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // 窗口永远在其他窗口上方
        canvasFrame.setAlwaysOnTop(true);

        // 设置事件
        // setEvent(grabber, canvasFrame);

        while (true) {

            // 如果窗口关闭的话
            if (!canvasFrame.isDisplayable()) {
                // 停止采集
                grabber.stop();
                System.exit(-1);
            }
            // 抓取页面,这里的frame是一帧视频图像
            Frame frame = grabber.grab();
            // 获取摄像头图像并放到窗口上显示
            canvasFrame.showImage(frame);
            // 切换刷新率可以决定视频是否连贯 单位毫秒
            // Thread.sleep(1);
        }
    }

    /**
     * 调用摄像头,录制视频
     *
     * @param filePath 视频保存路径,也可以是推流地址
     * @throws Exception e
     */
    private void video(String filePath) throws Exception {
        // 抓取视频帧
        // OpenCVFrameGrabber: 使用OpenCV方式打开抓取器
        // OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        // FrameGrabber: 会自己去找可以打开的摄像头的抓取器
        VideoInputFrameGrabber grabber = VideoInputFrameGrabber.createDefault(0);
        // 开始获取摄像头数据
        grabber.start();

        // 转换器 长时间运行该代码会导致内存溢出的原因是没有及时释放IplImage资源
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        // 抓取一帧视频并将其转换为图像
        opencv_core.IplImage grabbedImage = converter.convert(grabber.grab());
        int width = grabbedImage.width();
        int height = grabbedImage.height();

        FrameRecorder recorder = FrameRecorder.createDefault(filePath, width, height);
        // avcodec.AV_CODEC_ID_H264,编码
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        // 封装格式,如果是推送到rtmp就必须是flv封装格式
        recorder.setFormat("flv");
        // 视频帧率
        recorder.setFrameRate(25);

        // 开启录制器
        recorder.start();

        // 新建一个窗口
        CanvasFrame canvasFrame = new CanvasFrame("摄像头", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        // canvasFrame.setCanvasSize(1200, 800);
        // 设置窗口关闭时的动作
        canvasFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // 窗口永远在其他窗口上方
        canvasFrame.setAlwaysOnTop(true);

        long startTime = System.currentTimeMillis();
        // 视频时间戳
        long videoTS;

        Frame rotatedFrame;

        while (canvasFrame.isVisible() && (grabbedImage = converter.convert(grabber.grab())) != null) {

            rotatedFrame = converter.convert(grabbedImage);
            canvasFrame.showImage(rotatedFrame);
            // 设置视频时间戳 单位s
            videoTS = 1000 * (System.currentTimeMillis() - startTime);
            recorder.setTimestamp(videoTS);

            recorder.record(rotatedFrame);
            // 设置刷新频率
            Thread.sleep(10);
        }
        // 关闭窗口
        canvasFrame.dispose();
        // 关闭推流录制器,close包含release和stop操作
        recorder.close();
        // 关闭抓取器
        grabber.close();
    }


    /**
     * 按帧录制视频
     *
     * @param inputFile  该地址可以是网络直播/录播地址,也可以是远程/本地文件路径
     * @param outputFile 该地址只能是文件地址,如果使用该方法推送流媒体服务器会报错,原因是没有设置编码格式
     * @throws Exception e
     */
    private void record(String inputFile, String outputFile) throws Exception {
        // 获取视频源
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
        // 流媒体输出地址 分辨率(宽,高),是否录制音频(0:不录制,1:录制)
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, 1280, 720, 1);
        ExecutorService pool = ThreadUtil.defaultThreadPool();
        pool.execute(() -> recordByFrame(grabber, recorder));
        ThreadUtil.exit(pool);
    }


    private static void recordByFrame(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder) {
        // 建议在线程中使用该方法
        try {
            grabber.start();
            recorder.start();
            Frame frame;
            while ((frame = grabber.grabFrame()) != null) {
                recorder.record(frame);
            }
            recorder.stop();
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (grabber != null) {
                try {
                    grabber.stop();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 截图 可以用来做水印,人脸识别等
     *
     * @param frame 帧
     */
    private static void cutPicture(Frame frame) {
        if (null == frame || null == frame.image) {
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        File output = new File(FILE_ROOT_PATH + System.currentTimeMillis() + "." + DEFAULT_IMAGE_TYPE);
        if (!output.getAbsoluteFile().exists()) {
            boolean mkdirs = output.mkdirs();
        }
        try {
            ImageIO.write(bufferedImage, DEFAULT_IMAGE_TYPE, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给抓帧时添加个鼠标事件
     *
     * @param grabber     抓帧器
     * @param canvasFrame 画布窗口
     */
    private void setEvent(VideoInputFrameGrabber grabber, CanvasFrame canvasFrame) {
        Canvas canvas = canvasFrame.getCanvas();
        canvas.addMouseListener(new MouseListener() {
            // 鼠标点击事件(单或双)
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // 截张图
                    cutPicture(grabber.grab());
                } catch (FrameGrabber.Exception ex) {
                    ex.printStackTrace();
                }
            }

            // 鼠标按下事件
            @Override
            public void mousePressed(MouseEvent e) {
            }

            // 鼠标松开事件
            @Override
            public void mouseReleased(MouseEvent e) {
            }

            // 鼠标进入事件
            @Override
            public void mouseEntered(MouseEvent e) {
            }

            // 鼠标离开事件
            @Override
            public void mouseExited(MouseEvent e) {
            }

        });
    }


}
