package com.example.demo.controller.upload;

import com.example.demo.annotation.log.Action;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResResult;
import com.example.demo.constant.log.ActionLogEnum;
import com.example.demo.entity.upload.UploadFileDO;
import com.example.demo.service.upload.UploadFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/07/16
 * @description: 类描述: 文件上传表 Controller
 **/
@Api(tags = "upload模块-文件上传")
@RestController
@RequestMapping("/upload_file")
public class UploadFileController {

    private UploadFileService uploadFileService;

    @Autowired
    public UploadFileController(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

    private static final String UPLOAD_DESC = "上传文件";

    @ApiOperation(value = UPLOAD_DESC)
    @ApiImplicitParam(name = "request", value = "含文件的请求对象", required = true, dataTypeClass = HttpServletRequest.class)
    @PostMapping(value = "/upload")
    @Action(type = ActionLogEnum.SAVE, desc = UPLOAD_DESC)
    public String uploadFile(HttpServletRequest request) throws ServiceException {
        ResResult result = uploadFileService.uploadFile(request);
        return result.getStr(UPLOAD_DESC);
    }

    private static final String UPLOAD_BASE64 = "上传Base64图片文件";

    @ApiOperation(value = UPLOAD_BASE64)
    @ApiImplicitParam(name = "base64String", value = "Base64图片文件", required = true, dataType = "String")
    @PostMapping(value = "/upload_base_64")
    @Action(type = ActionLogEnum.SAVE, desc = UPLOAD_BASE64)
    public String uploadBase64Img(@RequestParam("base64String") String base64String) throws ServiceException {
        ResResult result = uploadFileService.uploadBase64Img(base64String);
        return result.getStr(UPLOAD_BASE64);
    }

    private static final String SAVE_DESC = "文件保存";

    @ApiOperation(value = SAVE_DESC)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "访问路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "path", value = "文件服务器路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "文件名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "文件类型:1.目录, 2-n.各种文件类型", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "文件大小", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "status", value = "文件状态：1.正常, 2.禁用", required = true, dataType = "Integer")
    })
    @PostMapping(value = "/save")
    @Action(type = ActionLogEnum.SAVE, desc = SAVE_DESC)
    public String save(@RequestParam("url") String url,
                       @RequestParam("path") String path,
                       @RequestParam("name") String name,
                       @RequestParam("type") Integer type,
                       @RequestParam("size") Long size,
                       @RequestParam("status") Integer status) throws ServiceException {
        UploadFileDO uploadFileDO = UploadFileDO.builder()
                .url(url)
                .path(path)
                .name(name)
                .type(type)
                .size(size)
                .status(status)
                .build();
        ResResult result = uploadFileService.save(uploadFileDO);
        return result.getStr(SAVE_DESC);
    }

    private static final String GET_DESC = "获取文件上传对象";

    @ApiOperation(value = GET_DESC)
    @ApiImplicitParam(name = "id", value = " 文件上传对象ID", required = true, dataType = "Long")
    @GetMapping(value = "/get_by_id")
    @Action(type = ActionLogEnum.GET, desc = GET_DESC)
    public String getById(@RequestParam(value = "id") Long id) throws ServiceException {
        ResResult result = uploadFileService.getById(id);
        return result.getStr(GET_DESC);
    }


    private static final String DELETE_DESC = "删除文件上传对象";

    @ApiOperation(value = DELETE_DESC)
    @ApiImplicitParam(name = "ids", value = " 文件上传对象ID列表", required = true, dataType = "Long")
    @PostMapping(value = "/delete_by_ids")
    @Action(type = ActionLogEnum.SAVE, desc = DELETE_DESC)
    public String deleteByIds(@RequestParam(value = "ids") Long[] ids) throws ServiceException {
        ResResult result = uploadFileService.deleteByIds(ids);
        return result.getStr(DELETE_DESC);
    }


}
