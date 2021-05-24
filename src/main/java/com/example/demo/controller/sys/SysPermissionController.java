package com.example.demo.controller.sys;

import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResResult;
import com.example.demo.entity.sys.SysPermissionDO;
import com.example.demo.service.sys.SysPermissionService;
import com.huang.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 权限 Controller
 **/
@Api(tags = "sys模块-权限表")
@RestController
@RequestMapping("/sys/sys_permission")
public class SysPermissionController {

    private SysPermissionService sysPermissionService;

    @Autowired
    public SysPermissionController(SysPermissionService sysPermissionService) {
        this.sysPermissionService = sysPermissionService;
    }

    private static final String LIST_DESC = "获取权限表列表";

    @ApiOperation(value = LIST_DESC)
    @ApiImplicitParam(name = "sysPermissionDO", value = "权限表对象", required = true, dataTypeClass = SysPermissionDO.class)
    @GetMapping(value = "/list")
    public String list(@ModelAttribute SysPermissionDO query) throws ServiceException {
        ResResult result = sysPermissionService.list(query);
        return result.getStr(LIST_DESC);
    }

    private static final String GET_DESC = "获取权限表对象";

    @ApiOperation(value = GET_DESC)
    @ApiImplicitParam(name = "id", value = " 权限表对象ID", required = true, dataType = "Long")
    @GetMapping(value = "/get_by_id")
    public String getById(@RequestParam(value = "id") Long id) throws ServiceException {
        ResResult result = sysPermissionService.getById(id);
        return result.getStr(GET_DESC);
    }


    private static final String SAVE_DESC = "新增权限表对象";

    @ApiOperation(value = SAVE_DESC)
    @ApiImplicitParam(name = "sysPermissionDO", value = "权限表对象", required = true, dataTypeClass = SysPermissionDO.class)
    @PostMapping(value = "/save")
    public String save(@ModelAttribute SysPermissionDO object) throws ServiceException {
        // 接口校验
        String verifyResult = legalParam(object);
        if (Objects.nonNull(verifyResult)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM, verifyResult).getStr(SAVE_DESC);
        }
        ResResult result = sysPermissionService.save(object);
        return result.getStr(SAVE_DESC);
    }

    private static final String UPDATE_DESC = "修改权限表对象";

    @ApiOperation(value = UPDATE_DESC)
    @ApiImplicitParam(name = "sysPermissionDO", value = " 权限表对象", required = true, dataTypeClass = SysPermissionDO.class)
    @PostMapping(value = "/update")
    public String update(@ModelAttribute SysPermissionDO object) throws ServiceException {
        // 接口校验
        String verifyResult = legalParam(object);
        if (Objects.nonNull(verifyResult)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM, verifyResult).getStr(UPDATE_DESC);
        }
        ResResult result = sysPermissionService.update(object);
        return result.getStr(UPDATE_DESC);
    }

    private static final String DELETE_DESC = "删除权限表对象";

    @ApiOperation(value = DELETE_DESC)
    @ApiImplicitParam(name = "ids", value = " 权限表对象ID列表", required = true, dataType = "Long")
    @PostMapping(value = "/delete_by_ids")
    public String deleteByIds(@RequestParam(value = "ids") Long[] ids) throws ServiceException {
        ResResult result = sysPermissionService.deleteByIds(ids);
        return result.getStr(DELETE_DESC);
    }

    /**
     * 参数校验
     */
    private String legalParam(SysPermissionDO object) {

        return null;
    }

}
