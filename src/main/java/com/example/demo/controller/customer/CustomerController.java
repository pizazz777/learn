package com.example.demo.controller.customer;

import com.example.demo.annotation.log.Action;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResResult;
import com.example.demo.constant.log.ActionLogEnum;
import com.example.demo.entity.customer.CustomerDO;
import com.example.demo.service.customer.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/28
 * @description: 类描述: 客户 Controller
 **/
@Api(tags = "customer模块-客户表")
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    private static final String LIST_DESC = "获取客户表列表";

    @RequiresPermissions("customer:get")
    @ApiOperation(value = LIST_DESC)
    @ApiImplicitParam(name = "customerDO", value = "客户表对象", required = true, dataTypeClass = CustomerDO.class)
    @GetMapping(value = "/list")
    @Action(type = ActionLogEnum.GET, desc = LIST_DESC)
    public String list(@ModelAttribute CustomerDO query) throws ServiceException {
        ResResult result = customerService.list(query);
        return result.getStr(LIST_DESC);
    }

    private static final String GET_DESC = "获取客户表对象";

    @RequiresPermissions("customer:get")
    @ApiOperation(value = GET_DESC)
    @ApiImplicitParam(name = "id", value = " 客户表对象ID", required = true, dataType = "Long")
    @GetMapping(value = "/get_by_id")
    @Action(type = ActionLogEnum.GET, desc = GET_DESC)
    public String getById(@RequestParam(value = "id") Long id) throws ServiceException {
        ResResult result = customerService.getById(id);
        return result.getStr(GET_DESC);
    }


    private static final String SAVE_DESC = "新增客户表对象";

    @RequiresPermissions("customer:save")
    @ApiOperation(value = SAVE_DESC)
    @PostMapping(value = "/save")
    @Action(type = ActionLogEnum.SAVE, desc = SAVE_DESC)
    public String save(@RequestBody CustomerDO object) throws ServiceException {
        // 接口校验
        String verifyResult = legalParam(object);
        if (Objects.nonNull(verifyResult)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM, verifyResult).getStr(SAVE_DESC);
        }
        ResResult result = customerService.save(object);
        return result.getStr(SAVE_DESC);
    }

    private static final String UPDATE_DESC = "修改客户表对象";

    @RequiresPermissions("customer:update")
    @ApiOperation(value = UPDATE_DESC)
    @PostMapping(value = "/update")
    @Action(type = ActionLogEnum.UPDATE, desc = UPDATE_DESC)
    public String update(@RequestBody CustomerDO object) throws ServiceException {
        // 接口校验
        String verifyResult = legalParam(object);
        if (Objects.nonNull(verifyResult)) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM, verifyResult).getStr(UPDATE_DESC);
        }
        ResResult result = customerService.update(object);
        return result.getStr(UPDATE_DESC);
    }

    private static final String DELETE_DESC = "删除客户表对象";

    @RequiresPermissions("customer:delete")
    @ApiOperation(value = DELETE_DESC)
    @ApiImplicitParam(name = "ids", value = " 客户表对象ID列表", required = true, dataType = "Long")
    @PostMapping(value = "/delete_by_ids")
    @Action(type = ActionLogEnum.DELETE, desc = DELETE_DESC)
    public String deleteByIds(@RequestParam(value = "ids") Long[] ids) throws ServiceException {
        ResResult result = customerService.deleteByIds(ids);
        return result.getStr(DELETE_DESC);
    }

    /**
     * 参数校验
     */
    private String legalParam(CustomerDO object) {

        return null;
    }

}
