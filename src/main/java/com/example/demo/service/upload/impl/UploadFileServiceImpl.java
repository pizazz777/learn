package com.example.demo.service.upload.impl;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.exception.UploadException;
import com.example.demo.component.response.DelResInfo;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResResult;
import com.example.demo.dao.upload.UploadFileDao;
import com.example.demo.entity.upload.UploadFileDO;
import com.example.demo.manager.file.UploadFileRequest;
import com.example.demo.service.upload.UploadFileService;
import com.example.demo.util.container.ContainerUtil;
import com.example.demo.util.file.FileUtil;
import com.google.common.collect.Lists;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/07/16
 * @description: 类描述: 文件上传表 Service
 **/
@Service
public class UploadFileServiceImpl implements UploadFileService {

    private UploadFileDao uploadFileDao;
    private AuthComponent authComponent;
    private UploadFileRequest uploadFileRequest;

    @Autowired
    public UploadFileServiceImpl(UploadFileDao uploadFileDao, AuthComponent authComponent, UploadFileRequest uploadFileRequest) {
        this.uploadFileDao = uploadFileDao;
        this.authComponent = authComponent;
        this.uploadFileRequest = uploadFileRequest;
    }

    /**
     * 上传文件
     *
     * @param request 请求对象
     * @return r
     * @throws UploadException e
     */
    @Override
    public ResResult uploadFile(HttpServletRequest request) throws UploadException {
        // 判断是不是 multipart 上传文件请求
        if (ServletFileUpload.isMultipartContent(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
            List<UploadFileDO> uploadFileDOList = Lists.newArrayList();
            while (iterator.hasNext()) {
                String name = iterator.next();
                List<MultipartFile> multipartFileList = multipartHttpServletRequest.getFiles(name);
                for (MultipartFile multipartFile : multipartFileList) {
                    UploadFileDO uploadFileDO = uploadFileRequest.saveToFile(multipartFile);
                    if (Objects.nonNull(uploadFileDO)) {
                        uploadFileDOList.add(uploadFileDO);
                    }
                }
            }
            return ResResult.success(uploadFileDOList);
        }
        return ResResult.fail("不是文件上传请求");
    }

    /**
     * 上传Base64图片文件
     *
     * @param base64String base64
     * @return r
     * @throws UploadException e
     */
    @Override
    public ResResult uploadBase64Img(String base64String) throws UploadException {
        UploadFileDO uploadFileDO = uploadFileRequest.saveBase64ToFile(base64String);
        if (Objects.nonNull(uploadFileDO)) {
            return ResResult.success(uploadFileDO);
        }
        return ResResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult save(UploadFileDO object) throws ServiceException {
        object.setCreateTime(LocalDateTime.now());
        object.setCreateUserId(authComponent.getPrimaryPrincipal(Long.class));
        return uploadFileDao.save(object) > 0 ? ResResult.success(object) : ResResult.fail();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult deleteByIds(Serializable[] ids) throws ServiceException {
        DelResInfo delResInfo = new DelResInfo();
        for (Serializable id : ids) {
            if (uploadFileDao.deleteById(id) > 0) {
                // 删除服务器上的文件
                UploadFileDO uploadFile = uploadFileDao.getById(id);
                File file = new File(uploadFile.getPath());
                FileUtil.deleteFiles(file);
                // 不存在且已删除则将 id 添加到已删除列表
                delResInfo.addDeleted(id);
            } else {
                delResInfo.addNotDelete(id);
            }
        }
        if (ContainerUtil.isNotEmpty(delResInfo.getNotDelete())) {
            return ResResult.response(ResCode.OK, "有权限表对象删除失败", delResInfo);
        }
        return ResResult.success(delResInfo);
    }
}
