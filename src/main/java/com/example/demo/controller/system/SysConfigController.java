package com.example.demo.controller.system;

import com.example.demo.annotation.log.Action;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResResult;
import com.example.demo.service.system.SysConfigService;
import com.example.demo.util.container.ContainerUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.demo.constant.log.ActionLogEnum.GET;
import static com.example.demo.constant.log.ActionLogEnum.UPDATE;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/05/18
 * @description: 类描述: 系统配置 Controller
 **/
@Api(tags = "system模块-系统配置表")
@RestController
@RequestMapping("/sys_config")
public class SysConfigController {

    private SysConfigService sysConfigService;

    @Autowired
    public SysConfigController(SysConfigService sysConfigService) {
        this.sysConfigService = sysConfigService;
    }


    private static final String GET_DESC = "获取系统配置";

    @ApiOperation(value = GET_DESC)
    @GetMapping(value = "/get")
    @Action(type = GET, desc = GET_DESC)
    public String get() throws ServiceException {
        ResResult result = sysConfigService.get();
        return result.getStr(GET_DESC);
    }


    private static final String GET_BY_KEY_DESC = "获取指定的系统配置";

    @ApiOperation(value = GET_BY_KEY_DESC)
    @ApiImplicitParam(name = "key", value = "指定的系统配置项", required = true, dataType = "String")
    @GetMapping(value = "/get_by_key")
    @Action(type = GET, desc = GET_BY_KEY_DESC)
    public String getByKey(@RequestParam("key") String key) throws ServiceException {
        ResResult result = sysConfigService.getByKey(key);
        return result.getStr(GET_BY_KEY_DESC);
    }


    private static final String UPDATE_DESC = "修改系统配置表对象";

    @ApiOperation(value = UPDATE_DESC)
    @ApiImplicitParam(name = "map", value = "key-value格式", required = true, dataType = "map")
    @PostMapping(value = "/update")
    @Action(type = UPDATE, desc = UPDATE_DESC)
    public String update(@RequestParam Map<String, Object> map) throws ServiceException {
        if (ContainerUtil.isEmpty(map)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM).getStr(UPDATE_DESC);
        }
        ResResult result = sysConfigService.updateConfig(map);
        return result.getStr(UPDATE_DESC);
    }

}
