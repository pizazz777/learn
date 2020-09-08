package com.example.demo.service.sys;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResResult;
import com.example.demo.entity.sys.SysPermissionDO;
import com.example.demo.service.BaseService;

import java.util.List;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 权限 Service
 **/
public interface SysPermissionService extends BaseService<SysPermissionDO> {


    /**
     * 根据登录用户和pid筛选用户权限下的权限
     *
     * @param pid 父级id
     * @return r
     * @throws ServiceException e
     */
    ResResult tree(Long pid) throws ServiceException;


    /**
     * 设置权限资源
     *
     * @param id             权限id
     * @param resourceIdList 资源id集合
     * @return r
     * @throws ServiceException e
     */
    ResResult setPermissionResource(Long id, List<Long> resourceIdList) throws ServiceException;


    /**
     * 获取权限对应的资源id集合
     *
     * @param id 权限id
     * @return r
     * @throws ServiceException e
     */
    ResResult listResourceId(Long id) throws ServiceException;


}
