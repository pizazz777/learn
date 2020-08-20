package com.example.demo.controller.sys;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResResult;
import java.util.Objects;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.sys.SysRoleDO;
import com.example.demo.service.sys.SysRoleService;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 角色 Controller
 **/
@Api(tags = "sys模块-角色表")
@RestController
@RequestMapping("/sys/sys_role")
public class SysRoleController {

    private SysRoleService sysRoleService;

    @Autowired
    public SysRoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    private static final String LIST_DESC = "获取角色表列表";

    @ApiOperation(value = LIST_DESC)
    @ApiImplicitParam(name = "sysRoleDO", value = "角色表对象", required = true, dataTypeClass = SysRoleDO.class)
    @GetMapping(value = "/list")
    public String list(@ModelAttribute SysRoleDO query) throws ServiceException {
        ResResult result = sysRoleService.list(query);
        return result.getStr(LIST_DESC);
    }

    private static final String GET_DESC = "获取角色表对象";

    @ApiOperation(value = GET_DESC)
    @ApiImplicitParam(name = "id", value = " 角色表对象ID", required = true, dataType = "Long")
    @GetMapping(value = "/get_by_id")
    public String getById(@RequestParam(value = "id") Long id) throws ServiceException {
        ResResult result = sysRoleService.getById(id);
        return result.getStr(GET_DESC);
    }


    private static final String SAVE_DESC = "新增角色表对象";

    @ApiOperation(value = SAVE_DESC)
    @ApiImplicitParam(name = "sysRoleDO", value = "角色表对象", required = true, dataTypeClass = SysRoleDO.class)
    @PostMapping(value = "/save")
    public String save(@ModelAttribute SysRoleDO object) throws ServiceException {
        // 接口校验
        String verifyResult = legalParam(object);
        if (Objects.nonNull(verifyResult)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM, verifyResult).getStr(SAVE_DESC);
        }
        ResResult result = sysRoleService.save(object);
        return result.getStr(SAVE_DESC);
    }

    private static final String UPDATE_DESC = "修改角色表对象";

    @ApiOperation(value = UPDATE_DESC)
    @ApiImplicitParam(name = "sysRoleDO", value = " 角色表对象", required = true, dataTypeClass = SysRoleDO.class)
    @PostMapping(value = "/update")
    public String update(@ModelAttribute SysRoleDO object) throws ServiceException {
        // 接口校验
        String verifyResult = legalParam(object);
        if (Objects.nonNull(verifyResult)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM, verifyResult).getStr(UPDATE_DESC);
        }
        ResResult result = sysRoleService.update(object);
        return result.getStr(UPDATE_DESC);
    }

    private static final String DELETE_DESC = "删除角色表对象";

    @ApiOperation(value = DELETE_DESC)
    @ApiImplicitParam(name = "ids", value = " 角色表对象ID列表", required = true, dataType = "Long")
    @PostMapping(value = "/delete_by_ids")
    public String deleteByIds(@RequestParam(value = "ids") Long[] ids) throws ServiceException {
        ResResult result = sysRoleService.deleteByIds(ids);
        return result.getStr(DELETE_DESC);
    }

    /**
    * 参数校验
    */
    private String legalParam(SysRoleDO object) {

        return null;
    }

}
