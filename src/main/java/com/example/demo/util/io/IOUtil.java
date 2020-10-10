package com.example.demo.util.io;

import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2020/09/17
 * @description io工具类
 */
public class IOUtil {

    private IOUtil() {
    }


    /**
     * 输入流转化为字节数组字符串,并对其进行Base64编码处理
     *
     * @param inputStream 图片流
     * @return r
     */
    public static String getBase64FromInputStream(InputStream inputStream) throws IOException {
        byte[] data;
        byte[] bytes = new byte[1024];
        int length;
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            while ((length = bufferedInputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, length);
            }
            // 缓冲区的内容写入到文件
            byteArrayOutputStream.flush();
            data = byteArrayOutputStream.toByteArray();
        }
        byte[] base64 = Base64.encodeBase64(data);
        if (Objects.nonNull(base64)) {
            return new String(base64, Charsets.UTF_8);
        }
        return null;
    }


    /**
     * 获取网络文件流
     *
     * @param internetUrl 网络地址
     * @return r
     */
    public static InputStream getInputStream(String internetUrl) throws IOException {
        URL url = new URL(internetUrl);
        URLConnection urlConnection = url.openConnection();
        return urlConnection.getInputStream();
    }


    /**
     * 读取InputStream流中的内容
     *
     * @param inputStream 流
     * @return r
     * @throws IOException e
     */
    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            while ((length = inputStream.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
            return stream.toByteArray();
        }
    }


    /**
     * 字节数组转输入流
     *
     * @param bytes 字节数组
     * @return InputStream
     */
    public static InputStream toInputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }


    /**
     * 将字符串写入到文件中
     *
     * @param source   要写入的字符串
     * @param fileName 文件名,相对路径,绝对路径均可
     * @throws IOException e
     */
    public static void writeFile(String source, String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.write(source);
        }
    }


}
