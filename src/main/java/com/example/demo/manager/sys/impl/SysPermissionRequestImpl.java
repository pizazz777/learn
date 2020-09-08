package com.example.demo.manager.sys.impl;

import com.example.demo.dao.sys.SysPermissionDao;
import com.example.demo.dao.sys.SysResourceDao;
import com.example.demo.entity.sys.SysPermissionDO;
import com.example.demo.manager.sys.SysPermissionRequest;
import com.example.demo.util.container.ContainerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.example.demo.constant.sys.CacheConst.SYS_PERMISSION_TREE;
import static com.example.demo.constant.sys.CommonConst.STATUS_NORMAL;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2020/08/26
 * @description 系统权限通用类
 */
@CacheConfig(cacheNames = SYS_PERMISSION_TREE)
@Component
public class SysPermissionRequestImpl implements SysPermissionRequest {

    private SysPermissionDao sysPermissionDao;
    private SysResourceDao sysResourceDao;

    @Autowired
    public SysPermissionRequestImpl(SysPermissionDao sysPermissionDao, SysResourceDao sysResourceDao) {
        this.sysPermissionDao = sysPermissionDao;
        this.sysResourceDao = sysResourceDao;
    }


    /**
     * 获取权限树 作为缓存暴露给Spring使用
     *
     * @param pid 父级id
     * @return list
     */
    @Cacheable
    @Override
    public List<SysPermissionDO> getTree(Long pid) {
        List<SysPermissionDO> childList = sysPermissionDao.listByPidAndStatus(pid, STATUS_NORMAL);
        if (ContainerUtil.isNotEmpty(childList)) {
            childList.forEach(child -> {
                // 设置权限关联的资源
                child.setResourceList(sysResourceDao.listByPermissionAndStatus(child.getId(), STATUS_NORMAL));
                // 防止id和pid一样,造成死循环
                if (!Objects.equals(child.getId(), child.getPid())) {
                    child.setChildList(getTree(child.getId()));
                }
            });
        }
        return childList;
    }

    /**
     * 获取权限树 作为缓存暴露给Spring使用 (根据自身允许的权限查询)
     *
     * @param pid              父级id
     * @param permissionIdList 允许的权限id列表
     * @return list
     */
    @Cacheable
    @Override
    public List<SysPermissionDO> getTree(Long pid, List<Long> permissionIdList) {
        List<SysPermissionDO> childList = sysPermissionDao.listByPidAndStatus(pid, STATUS_NORMAL);
        if (ContainerUtil.isNotEmpty(childList)) {
            childList.forEach(child -> {
                // 设置权限关联的资源
                child.setResourceList(sysResourceDao.listByPermissionAndStatus(child.getId(), STATUS_NORMAL));
                // 防止id和pid一样,造成死循环
                if (!Objects.equals(child.getId(), child.getPid()) && permissionIdList.contains(child.getId())) {
                    child.setChildList(getTree(child.getId(), permissionIdList));
                }
            });
        }
        return childList;
    }


}
