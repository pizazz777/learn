package com.example.demo.manager.file;

import io.minio.StatObjectResponse;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @author Administrator
 * @date 2021-08-06 09:56
 * @description: 文件上传请求
 */
public interface MinioRequest {

    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     * @throws Exception e
     */
    void createBucket(String bucketName) throws Exception;


    /**
     * 获取全部bucket
     *
     * @return list
     * @throws Exception e
     */
    List<Bucket> getAllBuckets() throws Exception;


    /**
     * 获取指定bucket信息
     *
     * @param bucketName bucket名称
     * @return bucket
     * @throws Exception e
     */
    Bucket getBucket(String bucketName) throws Exception;


    /**
     * 移除指定bucket
     *
     * @param bucketName bucket
     * @throws Exception e
     */
    void removeBucket(String bucketName) throws Exception;


    /**
     * 分区上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @param stream     流
     * @param size       大小
     * @return {@link String}
     * @throws Exception 异常
     */
    String putObject(String bucketName, String objectName, InputStream stream, Long size) throws Exception;


    /**
     * 根据文件前缀查询文件对象
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return {@link List<Item>}
     * @throws Exception 异常
     */
    List<Item> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) throws Exception;


    /**
     * 获取文件外链
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @return {@link String}
     * @throws Exception 异常
     */
    String getObjectURL(String bucketName, String objectName) throws Exception;


    /**
     * 得到文件对象流
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @return {@link InputStream}
     * @throws Exception 异常
     */
    InputStream getObject(String bucketName, String objectName) throws Exception;


    /**
     * 上传文件 base64
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @param base64Str  base64 str
     * @return {@link String}
     * @throws Exception 异常
     */
    String putObject(String bucketName, String objectName, String base64Str) throws Exception;


    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @param file       文件
     * @return {@link String}
     * @throws Exception 异常
     */
    String putObject(String bucketName, String objectName, MultipartFile file) throws Exception;


    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param file       文件
     * @return {@link String}
     * @throws Exception 异常
     */
    String putObject(String bucketName, MultipartFile file) throws Exception;


    /**
     * 获取文件对象信息
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @return {@link StatObjectResponse}
     * @throws Exception 异常
     */
    StatObjectResponse getObjectInfo(String bucketName, String objectName) throws Exception;


    /**
     * 删除对象
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @throws Exception 异常
     */
    void removeObject(String bucketName, String objectName) throws Exception;


    /**
     * 获取直传链接
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @return {@link String}
     * @throws Exception 异常
     */
    String presignedPutObject(String bucketName, String objectName) throws Exception;


    /**
     * 组合对象
     *
     * @param bucketName       bucket名称
     * @param chunkNames       块名称
     * @param targetObjectName 目标对象的名字
     * @return {@link String}
     * @throws Exception 异常
     */
    String composeObject(String bucketName, List<String> chunkNames, String targetObjectName) throws Exception;

}
