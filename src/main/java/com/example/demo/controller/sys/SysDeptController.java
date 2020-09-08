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
import com.example.demo.entity.sys.SysDeptDO;
import com.example.demo.service.sys.SysDeptService;

/**
 * @author hxx
 * @version 1.0.0
 * @date 2020/08/24
 * @description: 系统部门表
 **/
@Api(tags = "sys模块-系统部门")
@RestController
@RequestMapping("/sys_dept")
public class SysDeptController {

    private SysDeptService sysDeptService;

    @Autowired
    public SysDeptController(SysDeptService sysDeptService) {
        this.sysDeptService = sysDeptService;
    }

    private static final String LIST_DESC = "获取系统部门列表";

    @ApiOperation(value = LIST_DESC)
    @ApiImplicitParam(name = "sysDeptDO", value = "系统部门对象", required = true, dataTypeClass = SysDeptDO.class)
    @GetMapping(value = "/list")
    public String list(@ModelAttribute SysDeptDO query) throws ServiceException {
        ResResult result = sysDeptService.list(query);
        return result.getStr(LIST_DESC);
    }

    private static final String GET_DESC = "获取系统部门对象";

    @ApiOperation(value = GET_DESC)
    @ApiImplicitParam(name = "id", value = " 系统部门对象ID", required = true, dataType = "Long")
    @GetMapping(value = "/get_by_id")
    public String getById(@RequestParam(value = "id") Long id) throws ServiceException {
        ResResult result = sysDeptService.getById(id);
        return result.getStr(GET_DESC);
    }


    private static final String SAVE_DESC = "新增系统部门对象";

    @ApiOperation(value = SAVE_DESC)
    @ApiImplicitParam(name = "sysDeptDO", value = "系统部门对象", required = true, dataTypeClass = SysDeptDO.class)
    @PostMapping(value = "/save")
    public String save(@ModelAttribute SysDeptDO object) throws ServiceException {
        // 接口校验
        String verifyResult = legalParam(object);
        if (Objects.nonNull(verifyResult)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM, verifyResult).getStr(SAVE_DESC);
        }
        ResResult result = sysDeptService.save(object);
        return result.getStr(SAVE_DESC);
    }

    private static final String UPDATE_DESC = "修改系统部门对象";

    @ApiOperation(value = UPDATE_DESC)
    @ApiImplicitParam(name = "sysDeptDO", value = " 系统部门对象", required = true, dataTypeClass = SysDeptDO.class)
    @PostMapping(value = "/update")
    public String update(@ModelAttribute SysDeptDO object) throws ServiceException {
        // 接口校验
        String verifyResult = legalParam(object);
        if (Objects.nonNull(verifyResult)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM, verifyResult).getStr(UPDATE_DESC);
        }
        ResResult result = sysDeptService.update(object);
        return result.getStr(UPDATE_DESC);
    }

    private static final String DELETE_DESC = "删除系统部门对象";

    @ApiOperation(value = DELETE_DESC)
    @ApiImplicitParam(name = "ids", value = " 系统部门对象ID列表", required = true, dataType = "Long")
    @PostMapping(value = "/delete_by_ids")
    public String deleteByIds(@RequestParam(value = "ids") Long[] ids) throws ServiceException {
        ResResult result = sysDeptService.deleteByIds(ids);
        return result.getStr(DELETE_DESC);
    }

    /**
    * 参数校验
    */
    private String legalParam(SysDeptDO object) {

        return null;
    }

}
