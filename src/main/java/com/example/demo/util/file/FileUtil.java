package com.example.demo.util.file;

import com.example.demo.util.container.ContainerUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-07-16 15:08
 * @description: 文件工具类
 */
public class FileUtil {

    private FileUtil() {
    }

    /**
     * 使用NIO下载文件
     *
     * @param url      网络资源的 url
     * @param saveDir  保存的文件夹
     * @param fileName 指定的文件名
     * @return 下载的文件字节数
     */
    public static long downloadByNio(String url, String saveDir, String fileName) throws IOException {
        url = formatUrlString(url);
        try (InputStream inputStream = new URL(url).openStream()) {
            Path target = Paths.get(saveDir, fileName);
            if (Objects.nonNull(target)) {
                return Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        throw new IOException("文件不存在");
    }

    /**
     * Nio保存文件
     *
     * @param inputStream 文件输入流
     * @param saveDir     保存的文件夹
     * @param fileName    指定的文件名
     * @return 下载的文件字节数
     */
    public static long saveFileByNio(InputStream inputStream, String saveDir, String fileName) throws IOException {
        try {
            Path target = Paths.get(saveDir, fileName);
            Path parentPath = target.getParent();
            if (Objects.nonNull(parentPath)) {
                Files.createDirectories(parentPath);
            } else {
                throw new IOException("目标地址为空");
            }
            return Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件扩展名
     *
     * @param file 文件对象
     * @return 扩展名
     */
    public static String getSuffix(File file) {
        String fileName = file.getName();
        return getSuffix(fileName);
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String getSuffix(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > -1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    /**
     * 获取文件名称(不包括扩展名)
     *
     * @param file 文件对象
     * @return 文件名(不含扩展名)
     */
    public static String getFileName(File file) {
        String fileName = file.getName();
        return getFileName(fileName);
    }

    /**
     * 获取文件名称(不包括扩展名)
     *
     * @param fileName 文件名(含扩展名)
     * @return 文件名(不含扩展名)
     */
    public static String getFileName(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > -1) {
            return fileName.substring(0, dotIndex);
        }
        return fileName;
    }

    /**
     * 删除文件
     *
     * @param files 文件数组对象
     */
    public static void deleteFiles(File... files) {
        if (ContainerUtil.isNotEmpty(files)) {
            for (File file : files) {
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    /**
     * 判断 urlString 是否是 http:// 或 https://开头的,如果不是则加上前缀
     *
     * @param urlString urlString 字符串
     * @return urlString 字符串
     */
    private static String formatUrlString(String urlString) {
        if (StringUtils.isNotBlank(urlString)) {
            if (!urlString.toLowerCase().startsWith("http://") && !urlString.toLowerCase().startsWith("https://")) {
                urlString = "http://" + urlString;
            }
        }
        return urlString;
    }


}
