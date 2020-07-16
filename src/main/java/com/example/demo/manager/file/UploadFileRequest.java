package com.example.demo.manager.file;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.exception.UploadException;
import com.example.demo.entity.upload.UploadFileDO;
import com.example.demo.manager.BaseRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-16 14:49
 * @description: 文件上传请求
 */
public interface UploadFileRequest extends BaseRequest<UploadFileDO> {

    /**
     * 将 Base64图片转存至文件服务器
     *
     * @param base64String base64
     * @return r
     * @throws UploadException e
     */
    UploadFileDO saveBase64ToFile(String base64String) throws UploadException;

    /**
     * 将 MultipartFile 转存至文件服务器
     *
     * @param multipartFile multipartFile
     * @return r
     * @throws UploadException e
     */
    UploadFileDO saveToFile(MultipartFile multipartFile) throws UploadException;

    /**
     * 将 InputStream 转存至文件服务器
     *
     * @param inputStream inputStream
     * @param fileName    想要保存的文件名
     * @param suffix      文件扩展名
     * @return r
     * @throws UploadException e
     */
    UploadFileDO saveToFile(InputStream inputStream, String fileName, String suffix) throws UploadException;

    /**
     * 在服务器中生成空文件
     *
     * @param suffix 后缀名
     * @return r
     * @throws UploadException e
     */
    UploadFileDO generateEmptyFile(String suffix) throws UploadException;

    /**
     * 删除文件
     *
     * @param ids id数组
     * @return 未删除成功的id
     * @throws ServiceException e
     */
    List<Serializable> delete(Serializable... ids) throws ServiceException;

}
