package com.example.demo.controller.sys;

import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResResult;
import com.example.demo.entity.sys.SysResourceDO;
import com.example.demo.service.sys.SysResourceService;
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
 * @description: 类描述: 资源 Controller
 **/
@Api(tags = "sys模块-资源表")
@RestController
@RequestMapping("/sys/sys_resource")
public class SysResourceController {

    private SysResourceService sysResourceService;

    @Autowired
    public SysResourceController(SysResourceService sysResourceService) {
        this.sysResourceService = sysResourceService;
    }

    private static final String LIST_DESC = "获取资源表列表";

    @ApiOperation(value = LIST_DESC)
    @ApiImplicitParam(name = "sysResourceDO", value = "资源表对象", required = true, dataTypeClass = SysResourceDO.class)
    @GetMapping(value = "/list")
    public String list(@ModelAttribute SysResourceDO query) throws ServiceException {
        ResResult result = sysResourceService.list(query);
        return result.getStr(LIST_DESC);
    }

    private static final String GET_DESC = "获取资源表对象";

    @ApiOperation(value = GET_DESC)
    @ApiImplicitParam(name = "id", value = " 资源表对象ID", required = true, dataType = "Long")
    @GetMapping(value = "/get_by_id")
    public String getById(@RequestParam(value = "id") Long id) throws ServiceException {
        ResResult result = sysResourceService.getById(id);
        return result.getStr(GET_DESC);
    }


    private static final String SAVE_DESC = "新增资源表对象";

    @ApiOperation(value = SAVE_DESC)
    @ApiImplicitParam(name = "sysResourceDO", value = "资源表对象", required = true, dataTypeClass = SysResourceDO.class)
    @PostMapping(value = "/save")
    public String save(@ModelAttribute SysResourceDO object) throws ServiceException {
        // 接口校验
        String verifyResult = legalParam(object);
        if (Objects.nonNull(verifyResult)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM, verifyResult).getStr(SAVE_DESC);
        }
        ResResult result = sysResourceService.save(object);
        return result.getStr(SAVE_DESC);
    }

    private static final String UPDATE_DESC = "修改资源表对象";

    @ApiOperation(value = UPDATE_DESC)
    @ApiImplicitParam(name = "sysResourceDO", value = " 资源表对象", required = true, dataTypeClass = SysResourceDO.class)
    @PostMapping(value = "/update")
    public String update(@ModelAttribute SysResourceDO object) throws ServiceException {
        // 接口校验
        String verifyResult = legalParam(object);
        if (Objects.nonNull(verifyResult)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM, verifyResult).getStr(UPDATE_DESC);
        }
        ResResult result = sysResourceService.update(object);
        return result.getStr(UPDATE_DESC);
    }

    private static final String DELETE_DESC = "删除资源表对象";

    @ApiOperation(value = DELETE_DESC)
    @ApiImplicitParam(name = "ids", value = " 资源表对象ID列表", required = true, dataType = "Long")
    @PostMapping(value = "/delete_by_ids")
    public String deleteByIds(@RequestParam(value = "ids") Long[] ids) throws ServiceException {
        ResResult result = sysResourceService.deleteByIds(ids);
        return result.getStr(DELETE_DESC);
    }

    /**
     * 参数校验
     */
    private String legalParam(SysResourceDO object) {

        return null;
    }

}
