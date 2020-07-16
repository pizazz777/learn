package com.example.demo.service.upload;

import com.example.demo.component.exception.UploadException;
import com.example.demo.component.response.ResResult;
import com.example.demo.entity.upload.UploadFileDO;
import com.example.demo.service.BaseService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/07/16
 * @description: 类描述: 文件上传表 Service
 **/
public interface UploadFileService extends BaseService<UploadFileDO> {

    /**
     * 上传文件
     *
     * @param request 请求对象
     * @return r
     * @throws UploadException e
     */
    ResResult uploadFile(HttpServletRequest request) throws UploadException;

    /**
     * 上传Base64图片文件
     *
     * @param base64String base64
     * @return r
     * @throws UploadException e
     */
    ResResult uploadBase64Img(String base64String) throws UploadException;

}
