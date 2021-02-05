package com.example.demo.util.file;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.constant.file.FileTypeEnum;
import com.example.demo.util.container.ContainerUtil;
import com.example.demo.util.exception.ExceptionUtil;
import com.google.common.collect.Lists;
import org.apache.poi.util.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2020/09/15
 * @description zip压缩工具类
 */
public class ZipUtil {

    private ZipUtil() {
    }

    private static final Charset GBK = Charset.forName("GBK");
    private static final String SEPARATOR_ZIP = "/";

    /**
     * 读取压缩包文件
     *
     * @param file zip文件
     * @return 压缩文件结构包
     * @throws ServiceException e
     * @throws IOException      e
     */
    public static ZipDirectory read(File file) throws ServiceException, IOException {

        // 校验文件
        checkFile(file);

        ZipDirectory zipDirectory = new ZipDirectory();
        try (java.util.zip.ZipFile zipFile = new ZipFile(file, GBK)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String path = entry.getName();
                // 目录
                if (entry.isDirectory()) {
                    setDirectory(zipDirectory, path);
                }
                // 文件
                else {
                    setZipFile(path, zipFile.getInputStream(entry), zipDirectory);
                }
            }
            return zipDirectory;
        }
    }


    /**
     * 设置压缩文件
     *
     * @param path         压缩文件的路径
     * @param inputStream  文件的流
     * @param zipDirectory zip层级目录
     * @throws IOException e
     */
    private static void setZipFile(String path, InputStream inputStream, ZipDirectory zipDirectory) throws IOException {
        String dirParentPath = getParentPath(path, false);
        ZipDirectory parentDirectory = findZipDirectoryByDirPath(zipDirectory, dirParentPath);
        if (Objects.isNull(parentDirectory)) {
            parentDirectory = new ZipDirectory();
        }
        byte[] bytes = IOUtils.toByteArray(inputStream);
        List<com.example.demo.util.file.ZipFile> zipFileList = parentDirectory.getZipFileList();
        if (ContainerUtil.isEmpty(zipFileList)) {
            zipFileList = Lists.newArrayList();
        }
        com.example.demo.util.file.ZipFile zipFile = com.example.demo.util.file.ZipFile.builder()
                .fileBytes(bytes)
                .fileName(getFileName(path))
                .size((long) bytes.length)
                .suffix(getFileSuffix(path))
                .build();
        zipFileList.add(zipFile);
        parentDirectory.setZipFileList(zipFileList);
    }

    /**
     * 设置目录
     *
     * @param zipDirectory zip层级目录
     * @param path         路径
     */
    private static void setDirectory(ZipDirectory zipDirectory, String path) {
        // 获取父级路径
        String dirParentPath = getParentPath(path, true);
        // 从层级结构中找到指定的父级目录
        ZipDirectory parentDirectory = findZipDirectoryByDirPath(zipDirectory, dirParentPath);
        // 没找到,自己就是顶级目录
        if (Objects.isNull(parentDirectory)) {
            parentDirectory = new ZipDirectory();
            // 设置目录名称+路径
            parentDirectory.setName(getDirNamePath(path));
            parentDirectory.setDirPath(path);
            // 设置到子级目录
            setChildDirectory(zipDirectory, parentDirectory);
        } else {
            ZipDirectory childDirectory = new ZipDirectory();
            childDirectory.setName(getDirNamePath(path));
            childDirectory.setDirPath(path);
            // 设置到子级目录
            setChildDirectory(parentDirectory, childDirectory);
        }
    }

    /**
     * 设置子级目录
     *
     * @param parentDirectory 父目录
     * @param childDirectory  子目录
     */
    private static void setChildDirectory(ZipDirectory parentDirectory, ZipDirectory childDirectory) {
        // 设置到子级目录
        List<ZipDirectory> childZipDirectory = parentDirectory.getChildZipDirectory();
        if (ContainerUtil.isEmpty(childZipDirectory)) {
            childZipDirectory = Lists.newArrayList();
        }
        childZipDirectory.add(childDirectory);
        parentDirectory.setChildZipDirectory(childZipDirectory);
    }


    /**
     * 递归查找路径相同的文件夹
     *
     * @param zipDirectory zip目录
     * @param dirPath      目录路径
     * @return r
     */
    private static ZipDirectory findZipDirectoryByDirPath(ZipDirectory zipDirectory, String dirPath) {
        List<ZipDirectory> list = Lists.newArrayList();
        getAllZipDirectory(zipDirectory, list);
        return list.stream().filter(e -> Objects.equals(e.getDirPath(), dirPath)).findFirst().orElse(null);
    }


    /**
     * 获取所有文件夹
     *
     * @param zipDirectory zip文件夹层级目录
     * @param list         list
     */
    private static void getAllZipDirectory(ZipDirectory zipDirectory, List<ZipDirectory> list) {
        list.add(zipDirectory);
        List<ZipDirectory> childZipDirectoryList = zipDirectory.getChildZipDirectory();
        if (ContainerUtil.isNotEmpty(childZipDirectoryList)) {
            childZipDirectoryList.forEach(child -> getAllZipDirectory(child, list));
        }
    }


    /**
     * 获取父级路径
     *
     * @param path        路径
     * @param isDirectory 是否为文件夹
     * @return path
     */
    private static String getParentPath(String path, boolean isDirectory) {
        if (isDirectory) {
            path = path.substring(0, path.length() - 1);
        }
        path = path.substring(0, path.lastIndexOf(SEPARATOR_ZIP) + 1);
        return path;
    }

    /**
     * 获取目录名称
     *
     * @param path 目录路径
     * @return r
     */
    private static String getDirNamePath(String path) {
        path = path.substring(0, path.length() - 1);
        path = path.substring(path.lastIndexOf(SEPARATOR_ZIP) + 1);
        return path;
    }

    /**
     * 校验上传的文件
     *
     * @param file 文件对象
     * @throws ServiceException e
     */
    private static void checkFile(File file) throws ServiceException {
        ExceptionUtil.requireNonNull(file, "上传的文件为空!");
        String fileSuffix = FileUtil.getSuffix(file);
        if (!Objects.equals(fileSuffix, FileTypeEnum.ZIP.getSuffix())) {
            throw new ServiceException("请上传zip后缀的压缩文件!");
        }
    }

    /**
     * 获取文件名
     *
     * @param path 文件全路径
     * @return fileName
     */
    private static String getFileName(String path) {
        return path.substring(path.lastIndexOf(SEPARATOR_ZIP) + 1);
    }

    /**
     * 获取文件后缀名
     *
     * @param path 文件全路径
     * @return 后缀名
     */
    private static String getFileSuffix(String path) {
        path = getFileName(path);
        return FileUtil.getSuffix(path);
    }


}
