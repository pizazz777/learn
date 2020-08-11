package com.example.demo.manager.video.impl;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * @author Administrator
 * @date 2020-08-11 14:12
 * @description: 视频转换时,输出日志类
 */
@Slf4j
public class OutputVideoStreamThread implements Runnable {

    private InputStream inputStream;

    public OutputVideoStreamThread(InputStream inputStream){
        this.inputStream = inputStream;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            while (inputStream != null) {
                int read = inputStream.read();
                if (read != -1) {
                    stringBuilder.append(((char) read));
                } else {
                    break;
                }
            }
            log.debug(stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
