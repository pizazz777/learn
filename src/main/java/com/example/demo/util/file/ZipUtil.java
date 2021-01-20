package com.example.demo.util.file;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.constant.file.FileTypeEnum;
import com.example.demo.util.container.ContainerUtil;
import com.example.demo.util.exception.ExceptionUtil;
import com.google.common.collect.Lists;
import org.apache.poi.util.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

        // 目录容器
        Map<String, ZipDirectory> directoryContainerMap = new ConcurrentHashMap<>(32);
        try (java.util.zip.ZipFile zipFile = new ZipFile(file, GBK)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String path = entry.getName();
                // 是目录
                if (entry.isDirectory()) {
                    setDirectory(path, directoryContainerMap);
                }
                // 是文件
                else {
                    setZipFile(path, zipFile.getInputStream(entry), directoryContainerMap);
                }
            }
            return getTopDir(directoryContainerMap);
        }
    }

    /**
     * 获取顶级目录
     *
     * @param directoryContainerMap 目录容器
     * @return r
     * @throws FileNotFoundException e
     */
    private static ZipDirectory getTopDir(Map<String, ZipDirectory> directoryContainerMap) throws FileNotFoundException {
        Set<String> ketList = directoryContainerMap.keySet();
        String topPath = ketList.stream().min(String::compareTo).orElseThrow(FileNotFoundException::new);
        return directoryContainerMap.get(topPath);
    }


    /**
     * 设置压缩文件
     *
     * @param path                  压缩文件路径
     * @param inputStream           文件的流
     * @param directoryContainerMap 容器
     * @throws IOException e
     */
    private static void setZipFile(String path, InputStream inputStream, Map<String, ZipDirectory> directoryContainerMap) throws IOException {
        ZipDirectory parentDirectory = getParentDirectory(path, false, directoryContainerMap);
        byte[] bytes = IOUtils.toByteArray(inputStream);
        List<com.example.demo.util.file.ZipFile> zipFileList = parentDirectory.getZipFileList();
        if (!ContainerUtil.isNotEmpty(zipFileList)) {
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

    /**
     * 设置目录
     *
     * @param path                  路径
     * @param directoryContainerMap 目录容器
     */
    private static void setDirectory(String path, Map<String, ZipDirectory> directoryContainerMap) {
        // 获取目录名称
        ZipDirectory zipDirectory = ZipDirectory.builder()
                .name(getDirNamePath(path))
                .build();
        if (ContainerUtil.isNotEmpty(directoryContainerMap)) {
            // 获取父级目录
            ZipDirectory parentDirectory = getParentDirectory(path, true, directoryContainerMap);
            // 将该目录存到父级的目录中
            setDirToParentDir(parentDirectory, zipDirectory);
        }
        directoryContainerMap.put(path, zipDirectory);
    }

    /**
     * 设置目录层级关系
     *
     * @param parentDirectory 父级目录
     * @param zipDirectory    子目录
     */
    private static void setDirToParentDir(ZipDirectory parentDirectory, ZipDirectory zipDirectory) {
        List<ZipDirectory> childZipDirectory = parentDirectory.getChildZipDirectory();
        if (ContainerUtil.isEmpty(childZipDirectory)) {
            childZipDirectory = Lists.newArrayList();
        }
        childZipDirectory.add(zipDirectory);
        parentDirectory.setChildZipDirectory(childZipDirectory);
    }

    /**
     * 获取父级目录
     *
     * @param path                  目录
     * @param isDirectory           是否为文件夹
     * @param directoryContainerMap 容器
     * @return zip目录信息
     */
    private static ZipDirectory getParentDirectory(String path, boolean isDirectory, Map<String, ZipDirectory> directoryContainerMap) {
        String dirParentPath = getParentPath(path, isDirectory);
        return directoryContainerMap.get(dirParentPath);
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
            path = path.substring(0, path.lastIndexOf(SEPARATOR_ZIP) + 1);
        } else {
            path = path.substring(0, path.lastIndexOf(SEPARATOR_ZIP) + 1);
        }
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


}
