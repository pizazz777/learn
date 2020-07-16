package com.example.demo.controller.test;

import com.example.demo.annotation.Action;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResResult;
import com.example.demo.schdule.TimerTest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.constant.log.ActionLogEnum.GET;

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

    @Autowired
    public TestController(TimerTest timerTest) {
        this.timerTest = timerTest;
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
