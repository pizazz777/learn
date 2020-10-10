package com.example.demo.controller.test;

import com.example.demo.annotation.log.Action;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResResult;
import com.example.demo.constant.file.FileType;
import com.example.demo.entity.sys.SysUserDO;
import com.example.demo.entity.upload.UploadFileDO;
import com.example.demo.manager.video.VideoRequest;
import com.example.demo.office.excel.example.ExampleImportHandler;
import com.example.demo.office.excel.model.ImportResult;
import com.example.demo.schdule.TimerTest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.demo.constant.log.ActionLogEnum.GET;
import static com.example.demo.constant.log.ActionLogEnum.SAVE;

/**
 * @author Administrator
 * @date 2020-07-16 11:32
 * @description: 用来测试
 */
@Api(tags = "test模块-通用测试")
@RestController
@RequestMapping("/test")
public class TestController {

    private TimerTest timerTest;
    private ExampleImportHandler exampleImportHandler;
    private VideoRequest videoRequest;

    @Autowired
    public TestController(TimerTest timerTest, ExampleImportHandler exampleImportHandler, VideoRequest videoRequest) {
        this.timerTest = timerTest;
        this.exampleImportHandler = exampleImportHandler;
        this.videoRequest = videoRequest;
    }

    private static final String CONVERT_VIDEO = "视频转换";

    @ApiOperation(value = CONVERT_VIDEO)
    @Action(type = SAVE, desc = CONVERT_VIDEO)
    @PostMapping(value = "/convert")
    public String convert(@RequestParam("file") MultipartFile file, @RequestParam("suffix") String suffix) throws ServiceException {
        UploadFileDO convert = videoRequest.convert(file, FileType.getBySuffix(suffix));
        return ResResult.success(convert).getStr(CONVERT_VIDEO);
    }


    @ApiOperation(value = CONVERT_VIDEO)
    @Action(type = SAVE, desc = CONVERT_VIDEO)
    @PostMapping(value = "/convert2")
    public String convert2(@RequestParam("path") String path, @RequestParam("suffix") String suffix) throws ServiceException {
        UploadFileDO convert = videoRequest.convert(path, FileType.getBySuffix(suffix));
        return ResResult.success(convert).getStr(CONVERT_VIDEO);
    }

    private static final String IMPORT_EXCEL_DESC = "测试导入excel";

    @ApiOperation(value = IMPORT_EXCEL_DESC)
    @PostMapping(value = "/import_excel")
    @Action(type = GET, desc = IMPORT_EXCEL_DESC)
    public String importExcel(@RequestParam("file") MultipartFile file) throws ServiceException, IOException {
        ImportResult<SysUserDO> handler = exampleImportHandler.handler(file);
        return ResResult.success(handler).getStr(IMPORT_EXCEL_DESC);
    }

    private static final String CREATE_JOB_DESC = "测试定时任务";

    @ApiOperation(value = CREATE_JOB_DESC)
    @GetMapping(value = "/create_job")
    @Action(type = GET, desc = CREATE_JOB_DESC)
    public String timer() throws ServiceException {
        timerTest.createJob();
        return ResResult.success().getStr(CREATE_JOB_DESC);
    }

    private static final String PAUSE_JOB_DESC = "暂停定时任务";

    @ApiOperation(value = PAUSE_JOB_DESC)
    @GetMapping(value = "/pause_job")
    @Action(type = GET, desc = PAUSE_JOB_DESC)
    public String pauseJob() throws ServiceException {
        timerTest.pauseJob();
        return ResResult.success().getStr(PAUSE_JOB_DESC);
    }

    private static final String RESUME_JOB_DESC = "恢复定时任务";

    @ApiOperation(value = RESUME_JOB_DESC)
    @GetMapping(value = "/resume_job")
    @Action(type = GET, desc = RESUME_JOB_DESC)
    public String resumeJob() throws ServiceException {
        timerTest.resumeJob();
        return ResResult.success().getStr(RESUME_JOB_DESC);
    }

    private static final String REMOVE_TRIGGER_DESC = "删除触发器及任务";

    @ApiOperation(value = REMOVE_TRIGGER_DESC)
    @GetMapping(value = "/remove_trigger")
    @Action(type = GET, desc = REMOVE_TRIGGER_DESC)
    public String removeTrigger() throws ServiceException {
        timerTest.removeTrigger();
        return ResResult.success().getStr(REMOVE_TRIGGER_DESC);
    }

}
