package com.example.demo.controller.sys;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResResult;
import com.example.demo.entity.sys.SysUserDO;
import com.example.demo.service.sys.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/28
 * @description: 类描述: 系统用户 Controller
 **/
@Api(tags = "sys模块-用户表")
@RestController
@RequestMapping("/sys/sys_user")
public class SysUserController {

    private SysUserService sysUserService;

    @Autowired
    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    private static final String LOGIN_DESC = "用户登录";

    @ApiOperation(value = LOGIN_DESC)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
    })
    @PostMapping(value = "/login")
    public String login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password) throws ServiceException {
        String verifyResult = legalParam(username);
        if (Objects.nonNull(verifyResult)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM, verifyResult).getStr(LOGIN_DESC);
        }
        ResResult result = sysUserService.login(username, password);
        return result.getStr(LOGIN_DESC);
    }

    private static final String LOGOUT_DESC = "用户登出";

    @ApiOperation(value = LOGOUT_DESC)
    @PostMapping(value = "/logout")
    public String logout(HttpServletRequest request) throws ServiceException {
        ResResult result = sysUserService.logout(request);
        return result.getStr(LOGOUT_DESC);
    }

    private static final String LIST_DESC = "获取用户表列表";

    @ApiOperation(value = LIST_DESC)
    @ApiImplicitParam(name = "sysUserDO", value = "用户表对象", required = true, dataTypeClass = SysUserDO.class)
    @GetMapping(value = "/list")
    public String list(@ModelAttribute SysUserDO query) throws ServiceException {
        ResResult result = sysUserService.list(query);
        return result.getStr(LIST_DESC);
    }

    private static final String GET_DESC = "获取用户表对象";

    @ApiOperation(value = GET_DESC)
    @ApiImplicitParam(name = "id", value = " 用户表对象ID", required = true, dataType = "Long")
    @GetMapping(value = "/get_by_id")
    public String getById(@RequestParam(value = "id") Long id) throws ServiceException {
        ResResult result = sysUserService.getById(id);
        return result.getStr(GET_DESC);
    }


    private static final String SAVE_DESC = "新增用户表对象";

    @ApiOperation(value = SAVE_DESC)
    @ApiImplicitParam(name = "sysUserDO", value = "用户表对象", required = true, dataTypeClass = SysUserDO.class)
    @PostMapping(value = "/save")
    public String save(@ModelAttribute SysUserDO object) throws ServiceException {
        // 接口校验
        String verifyResult = legalParam(object);
        if (Objects.nonNull(verifyResult)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM, verifyResult).getStr(SAVE_DESC);
        }
        ResResult result = sysUserService.save(object);
        return result.getStr(SAVE_DESC);
    }

    private static final String UPDATE_DESC = "修改用户表对象";

    @ApiOperation(value = UPDATE_DESC)
    @ApiImplicitParam(name = "sysUserDO", value = " 用户表对象", required = true, dataTypeClass = SysUserDO.class)
    @PostMapping(value = "/update")
    public String update(@ModelAttribute SysUserDO object) throws ServiceException {
        // 接口校验
        String verifyResult = legalParam(object);
        if (Objects.nonNull(verifyResult)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM, verifyResult).getStr(UPDATE_DESC);
        }
        ResResult result = sysUserService.update(object);
        return result.getStr(UPDATE_DESC);
    }

    private static final String DELETE_DESC = "删除用户表对象";

    @ApiOperation(value = DELETE_DESC)
    @ApiImplicitParam(name = "ids", value = " 用户表对象ID列表", required = true, dataType = "Long")
    @PostMapping(value = "/delete_by_ids")
    public String deleteByIds(@RequestParam(value = "ids") Long[] ids) throws ServiceException {
        ResResult result = sysUserService.deleteByIds(ids);
        return result.getStr(DELETE_DESC);
    }

    /**
     * 参数校验
     */
    private String legalParam(String username) {
        if (StringUtils.isBlank(username)) {
            return "未填写用户名";
        }
        if (Objects.equals(username.toLowerCase(), "null")) {
            return "未填写用户名";
        }
        return null;
    }

    private String legalParam(SysUserDO object) {

        return null;
    }

}
