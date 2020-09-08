package com.example.demo.service.sys;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResResult;
import com.example.demo.entity.sys.SysRoleDO;
import com.example.demo.service.BaseService;

import java.util.List;

/**
 * @author hxx
 * @version 1.0.0
 * @date 2020/08/24
 * @description 系统角色表
 **/
public interface SysRoleService extends BaseService<SysRoleDO> {


    /**
     * 设置角色权限和部门
     *
     * @param id               角色id
     * @param permissionIdList 权限id集合
     * @param permissionRange  角色权限范围
     * @param deptIdList       指定部门id集合
     * @return r
     * @throws ServiceException e
     */
    ResResult setRolePermissionAndDeptId(Long id, List<Long> permissionIdList, Integer permissionRange, List<Long> deptIdList) throws ServiceException;


    /**
     * 根据角色id获取权限
     *
     * @param id 角色id
     * @return r
     * @throws ServiceException e
     */
    ResResult listPermissionIdById(Long id) throws ServiceException;


}
