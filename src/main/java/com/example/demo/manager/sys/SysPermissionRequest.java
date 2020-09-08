package com.example.demo.manager.sys;

import com.example.demo.entity.sys.SysPermissionDO;

import java.util.List;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2020/08/26
 * @description 系统权限通用类
 */
public interface SysPermissionRequest {


    /**
     * 获取权限树 作为缓存暴露给Spring使用
     *
     * @param pid 父级id
     * @return list
     */
    List<SysPermissionDO> getTree(Long pid);


    /**
     * 获取权限树 作为缓存暴露给Spring使用
     *
     * @param pid              父级id
     * @param permissionIdList 允许的权限id列表
     * @return list
     */
    List<SysPermissionDO> getTree(Long pid, List<Long> permissionIdList);


}
