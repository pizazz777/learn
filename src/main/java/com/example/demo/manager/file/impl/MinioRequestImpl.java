package com.example.demo.manager.file.impl;

import com.example.demo.manager.file.MinioRequest;
import com.example.demo.properties.ProjectProperties;
import com.google.common.collect.Lists;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/08/06
 * @description 文件对象存储操作类
 */
@Component
public class MinioRequestImpl implements MinioRequest {

    private MinioClient client;
    private ProjectProperties projectProperties;

    @Autowired
    public MinioRequestImpl(MinioClient minioClient, ProjectProperties projectProperties) {
        this.client = minioClient;
        this.projectProperties = projectProperties;
    }

    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     * @throws Exception e
     */
    @Override
    public void createBucket(String bucketName) throws Exception {
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 获取全部bucket
     *
     * @return list
     * @throws Exception e
     */
    @Override
    public List<Bucket> getAllBuckets() throws Exception {
        return client.listBuckets();
    }

    /**
     * 获取指定bucket信息
     *
     * @param bucketName bucket名称
     * @return bucket
     * @throws Exception e
     */
    @Override
    public Bucket getBucket(String bucketName) throws Exception {
        return client.listBuckets().stream()
                .filter(bucket -> Objects.equals(bucket.name(), bucketName))
                .findFirst()
                .orElseThrow(() -> new Exception("没有获取到bucket信息"));
    }

    /**
     * 移除指定bucket
     *
     * @param bucketName bucket
     * @throws Exception e
     */
    @Override
    public void removeBucket(String bucketName) throws Exception {
        client.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

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
    @Override
    public String putObject(String bucketName, String objectName, InputStream stream, Long size) throws Exception {
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(stream, size, projectProperties.getMinio().getPartSize())
                .build();
        ObjectWriteResponse objectWriteResponse = client.putObject(putObjectArgs);
        return objectWriteResponse.object();
    }

    /**
     * 根据文件前缀查询文件对象
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return {@link List<Item>}
     * @throws Exception 异常
     */
    @Override
    public List<Item> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) throws Exception {
        List<Item> objectList = Lists.newArrayList();
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .recursive(recursive)
                .build();
        Iterable<Result<Item>> objectsIterator = client.listObjects(listObjectsArgs);
        while (objectsIterator.iterator().hasNext()) {
            objectList.add(objectsIterator.iterator().next().get());
        }
        return objectList;
    }

    /**
     * 获取文件外链
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @return {@link String}
     * @throws Exception 异常
     */
    @Override
    public String getObjectURL(String bucketName, String objectName) throws Exception {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                // 这里的method方法决定最后链接是什么请求获得
                .method(Method.GET)
                // 设置这个链接多久失效
                .expiry(7, TimeUnit.DAYS)
                .object(objectName)
                .build();
        return client.getPresignedObjectUrl(args);
    }

    /**
     * 得到文件对象流
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @return {@link InputStream}
     * @throws Exception 异常
     */
    @Override
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build();
        return client.getObject(getObjectArgs);
    }

    /**
     * 上传文件 base64
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @param base64Str  base64 str
     * @return {@link String}
     * @throws Exception 异常
     */
    @Override
    public String putObject(String bucketName, String objectName, String base64Str) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(base64Str.getBytes());
        // 进行解码
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] bytes = base64Decoder.decodeBuffer(inputStream);
        inputStream = new ByteArrayInputStream(bytes);
        putObject(bucketName, objectName, inputStream, (long) bytes.length);
        return objectName;
    }

    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @param file       文件
     * @return {@link String}
     * @throws Exception 异常
     */
    @Override
    public String putObject(String bucketName, String objectName, MultipartFile file) throws Exception {
        this.putObject(bucketName, objectName, file.getInputStream(), file.getSize());
        return objectName;
    }

    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param file       文件
     * @return {@link String}
     * @throws Exception 异常
     */
    @Override
    public String putObject(String bucketName, MultipartFile file) throws Exception {
        return putObject(bucketName, file.getOriginalFilename(), file.getInputStream(), file.getSize());
    }

    /**
     * 获取文件对象信息
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @return {@link StatObjectResponse}
     * @throws Exception 异常
     */
    @Override
    public StatObjectResponse getObjectInfo(String bucketName, String objectName) throws Exception {
        StatObjectArgs statObjectArgs = StatObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build();
        return client.statObject(statObjectArgs);
    }

    /**
     * 删除对象
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @throws Exception 异常
     */
    @Override
    public void removeObject(String bucketName, String objectName) throws Exception {
        RemoveObjectArgs objectArgs = RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build();
        client.removeObject(objectArgs);
    }

    /**
     * 获取直传链接
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @return {@link String}
     * @throws Exception 异常
     */
    @Override
    public String presignedPutObject(String bucketName, String objectName) throws Exception {
        GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(bucketName)
                .object(objectName)
                .expiry(7, TimeUnit.DAYS)
                .build();
        return client.getPresignedObjectUrl(getPresignedObjectUrlArgs);
    }

    /**
     * 组合对象
     *
     * @param bucketName       bucket名称
     * @param chunkNames       块名称
     * @param targetObjectName 目标对象的名字
     * @return {@link String}
     * @throws Exception 异常
     */
    @Override
    public String composeObject(String bucketName, List<String> chunkNames, String targetObjectName) throws Exception {
        List<ComposeSource> sources = new ArrayList<>(chunkNames.size());
        for (String chunkName : chunkNames) {
            ComposeSource composeSource = ComposeSource.builder()
                    .bucket(bucketName)
                    .object(chunkName)
                    .build();
            sources.add(composeSource);
        }
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
                .bucket(bucketName)
                .sources(sources)
                .object(targetObjectName)
                .build();
        ObjectWriteResponse objectWriteResponse = client.composeObject(composeObjectArgs);
        return objectWriteResponse.object();
    }
}
