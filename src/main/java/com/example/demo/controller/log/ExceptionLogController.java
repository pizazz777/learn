package com.example.demo.controller.log;

import com.example.demo.component.response.ResResult;
import com.example.demo.entity.log.ExceptionLogDO;
import com.example.demo.service.log.ExceptionLogService;
import com.huang.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/05/15
 * @description: 类描述: 异常日志 Controller
 **/
@Api(tags = "log模块-异常日志表")
@RestController
@RequestMapping("/exception_log")
public class ExceptionLogController {

    private ExceptionLogService exceptionLogService;

    @Autowired
    public ExceptionLogController(ExceptionLogService exceptionLogService) {
        this.exceptionLogService = exceptionLogService;
    }

    private static final String LIST_DESC = "获取异常日志表列表";

    @ApiOperation(value = LIST_DESC)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identify", value = "用户ID", dataType = "Long"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "分页容量", required = true, dataType = "Integer")
    })
    @GetMapping(value = "/list")
    public String list(@RequestParam("identify") Long identify,
                       @RequestParam("startTime") LocalDateTime startTime,
                       @RequestParam("endTime") LocalDateTime endTime,
                       @RequestParam("page") Integer page,
                       @RequestParam("pageSize") Integer pageSize) throws ServiceException {
        ExceptionLogDO query = ExceptionLogDO.builder()
                .identify(Objects.nonNull(identify) ? String.valueOf(identify) : null)
                .startTime(startTime)
                .endTime(endTime)
                .build();
        query.setPageBean(page, pageSize);
        ResResult result = exceptionLogService.list(query);
        return result.getStr(LIST_DESC);
    }

}
