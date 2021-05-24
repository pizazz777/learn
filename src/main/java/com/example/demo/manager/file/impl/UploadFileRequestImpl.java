package com.example.demo.manager.file.impl;

import com.example.demo.component.AuthComponent;
import com.example.demo.constant.file.FileType;
import com.example.demo.constant.file.FileTypeEnum;
import com.example.demo.dao.upload.UploadFileDao;
import com.example.demo.entity.upload.Base64StringDO;
import com.example.demo.entity.upload.UploadFileDO;
import com.example.demo.manager.file.UploadFileRequest;
import com.example.demo.properties.ProjectProperties;
import com.google.common.collect.Lists;
import com.huang.exception.ServiceException;
import com.huang.exception.UploadException;
import com.huang.util.container.ContainerUtil;
import com.huang.util.file.FileUtil;
import com.huang.util.time.DateConst;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;

/**
 * @author Administrator
 * @date 2020-07-16 14:52
 * @description: 文件上传处理类
 */
@Component
public class UploadFileRequestImpl implements UploadFileRequest {

    private AuthComponent authComponent;
    private UploadFileDao uploadFileDao;
    private ProjectProperties projectProperties;

    @Autowired
    public UploadFileRequestImpl(AuthComponent authComponent, UploadFileDao uploadFileDao, ProjectProperties projectProperties) {
        this.authComponent = authComponent;
        this.uploadFileDao = uploadFileDao;
        this.projectProperties = projectProperties;
    }

    /**
     * 将 Base64图片转存至文件服务器
     *
     * @param base64String base64
     * @return r
     * @throws UploadException e
     */
    @Override
    public UploadFileDO saveBase64ToFile(String base64String) throws UploadException {
        if (StringUtils.isBlank(base64String)) {
            throw new UploadException("文件不能为空");
        }
        String base64MidSpilt = "base64,";
        if (base64String.contains(base64MidSpilt)) {
            // base64字符串对象
            Base64StringDO base64StringDO = Base64StringDO.getByBase64(base64String);
            // 文件类型
            FileTypeEnum fileTypeEnum = FileType.getByBase64Prefix(base64StringDO.getPrefix());
            // 扩展名
            String suffix = fileTypeEnum.getSuffix();
            // 文件名
            String name = UUID.randomUUID().toString();
            // 文件名(包括扩展名)
            String fullFileName = UUID.randomUUID().toString() + "." + suffix;
            // 中间文件夹目录
            String midDirsString = getMidDirsName();
            // 访问路径
            String url = getUrl(midDirsString, fullFileName);
            // 保存文件夹
            String saveDir = getSaveDir(midDirsString);
            //因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
            byte[] bytes = Base64.getDecoder().decode(base64StringDO.getContent().getBytes(StandardCharsets.UTF_8));
            try {
                Files.createDirectories(Paths.get(saveDir));
                String path = saveDir + File.separator + fullFileName;
                File file = new File(path);
                FileUtils.writeByteArrayToFile(file, bytes);
                // 保存至数据库
                return saveData(url, path, name, fileTypeEnum.getIntType(), (long) bytes.length);
            } catch (IOException | ServiceException e) {
                throw new UploadException("保存文件异常");
            }
        }
        throw new UploadException("文件非 Base64类型 ");
    }

    /**
     * 将 MultipartFile 转存至文件服务器
     *
     * @param multipartFile multipartFile
     * @return r
     * @throws UploadException e
     */
    @Override
    public UploadFileDO saveToFile(MultipartFile multipartFile) throws UploadException {
        Assert.notNull(multipartFile, "文件不能为空");
        // 文件大小
        long size = multipartFile.getSize();
        // 原始名称
        String oriName = multipartFile.getOriginalFilename();
        if (Objects.nonNull(oriName)) {
            // 文件类型
            FileTypeEnum fileTypeEnum = FileType.getByName(oriName);
            // 文件名
            String name = FileUtil.getFileName(oriName);
            // 扩展名
            String suffix = fileTypeEnum.getSuffix();
            // 中间文件夹目录
            String midDirsString = getMidDirsName();
            // 服务器文件名(包括后缀)
            String fullFileName = getServerFileName(name, suffix);
            // 访问路径
            String url = getUrl(midDirsString, fullFileName);
            // 保存文件夹
            String saveDir = getSaveDir(midDirsString);
            try {
                Files.createDirectories(Paths.get(saveDir));
                String path = saveDir + File.separator + fullFileName;
                File file = new File(path);
                multipartFile.transferTo(file);
                return saveData(url, path, name, fileTypeEnum.getIntType(), size);
            } catch (IOException | ServiceException e) {
                throw new UploadException("保存文件异常");
            }
        }
        return null;
    }

    /**
     * 将 InputStream 转存至文件服务器
     *
     * @param inputStream inputStream
     * @param fileName    想要保存的文件名
     * @param suffix      文件扩展名
     * @return r
     * @throws UploadException e
     */
    @Override
    public UploadFileDO saveToFile(InputStream inputStream, String fileName, String suffix) throws UploadException {
        Assert.notNull(inputStream, "输入流不能为空");
        Assert.notNull(suffix, "文件扩展名不能为空");
        try {
            // 文件类型
            FileTypeEnum fileTypeEnum = FileType.getBySuffix(suffix);
            // 文件名
            String name = FileUtil.getFileName(fileName);
            // 中间文件夹目录
            String midDirsString = getMidDirsName();
            // 服务器文件名(包括后缀)
            String fullFileName = getServerFileName(name, suffix);
            // 访问路径
            String url = getUrl(midDirsString, fullFileName);
            // 保存文件夹
            String saveDir = getSaveDir(midDirsString);
            try {
                long size = FileUtil.saveFileByNio(inputStream, saveDir, fullFileName);
                // 服务器文件地址
                String path = saveDir + File.separator + fullFileName;
                return saveData(url, path, name, fileTypeEnum.getIntType(), size);
            } catch (IOException | ServiceException e) {
                throw new UploadException("获取文件流异常");
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 在服务器中生成空文件
     *
     * @param suffix 后缀名
     * @return r
     * @throws UploadException e
     */
    @Override
    public UploadFileDO generateEmptyFile(String suffix) throws UploadException {
        Objects.requireNonNull(suffix);
        // 文件类型
        FileTypeEnum fileTypeEnum = FileType.getBySuffix(suffix);
        // 文件名
        String name = UUID.randomUUID().toString();
        // 中间文件夹目录
        String midDirsString = getMidDirsName();
        // 服务器文件名(包括后缀)
        String fullFileName = getServerFileName(name, suffix);
        // 访问路径
        String url = getUrl(midDirsString, fullFileName);
        // 保存文件夹
        String saveDir = getSaveDir(midDirsString);
        // 创建文件夹
        File dirFile = new File(saveDir);
        if (!dirFile.exists()) {
            boolean flag = dirFile.mkdirs();
        }
        // 服务器文件地址
        String path = saveDir + File.separator + fullFileName;
        try {
            return saveData(url, path, name, fileTypeEnum.getIntType(), null);
        } catch (ServiceException e) {
            throw new UploadException(e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param ids id数组
     * @return 未删除成功的id
     * @throws ServiceException e
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Serializable> delete(Serializable... ids) throws ServiceException {
        List<Serializable> list = Lists.newArrayList();
        if (ContainerUtil.isNotEmpty(ids)) {
            for (Serializable id : ids) {
                // 删除服务器上的文件
                UploadFileDO uploadFile = uploadFileDao.getById(id);
                if (Objects.nonNull(uploadFile)) {
                    File file = new File(uploadFile.getPath());
                    FileUtil.deleteFiles(file);
                    // 删除数据库
                    if (uploadFileDao.deleteById(id) <= 0) {
                        list.add(id);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取服务器访问路径
     *
     * @param midDirsString 文件夹路径
     * @param fullFileName  全文件名
     * @return 访问路径
     */
    private String getUrl(String midDirsString, String fullFileName) {
        String url = projectProperties.getFile().getUploadFileUrl() + "/" + midDirsString + "/" + fullFileName;
        return url.replaceAll("\\\\", "/");
    }

    /**
     * 获取保存的文件夹
     *
     * @param midDirsString 文件夹路径
     * @return r
     */
    private String getSaveDir(String midDirsString) {
        String saveDir = projectProperties.getFile().getUploadFileDir() + File.separator + midDirsString;
        saveDir = saveDir.replaceAll("/", Matcher.quoteReplacement(File.separator));
        // 使用 Matcher.quoteReplacement(File.separator): windows 中处理 \
        saveDir = saveDir.replaceAll("^\\\\$", Matcher.quoteReplacement(File.separator));
        return saveDir;
    }

    /**
     * 保存至数据库
     *
     * @param url  访问路径
     * @param path 服务器路径,包括文件夹+服务器文件名
     * @param name 显示文件名,无后缀
     * @param type 文件类型
     * @param size 文件大小
     * @return 保存的对象
     */
    private UploadFileDO saveData(String url, String path, String name, int type, Long size) throws ServiceException {
        UploadFileDO uploadFileDO = UploadFileDO.builder()
                .url(url)
                .path(path)
                .name(name)
                .type(type)
                .size(size)
                .status(UploadFileDO.STATUS_ACTIVE)
                .createTime(LocalDateTime.now())
                .createUserId(authComponent.getPrimaryPrincipal(Long.class))
                .build();
        if (uploadFileDao.save(uploadFileDO) > 0) {
            return uploadFileDO;
        }
        return null;
    }

    /**
     * 生成服务器上文件名
     *
     * @param originalName 原始文件名(不包括扩展名)
     * @param suffix       扩展名
     * @return 原文件名+当前毫秒时间
     */
    private String getServerFileName(String originalName, String suffix) {
        if (StringUtils.isNotBlank(originalName)) {
            originalName = originalName + "_";
        }
        return originalName + System.currentTimeMillis() + "." + suffix;
    }

    /**
     * 获取中间级级(可以有多个)文件夹路径
     *
     * @return 生成文件夹中间路径, 使用日期
     */
    private static String getMidDirsName() {
        return LocalDate.now().format(DateConst.DEFAULT_DATE);
    }

    @Override
    public Integer update(UploadFileDO object) {
        return uploadFileDao.update(object);
    }

}
