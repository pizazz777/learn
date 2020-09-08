package com.example.demo.service.sys.impl;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.DelResInfo;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResList;
import com.example.demo.component.response.ResResult;
import com.example.demo.dao.sys.SysPermissionResourceMidDao;
import com.example.demo.dao.sys.SysResourceDao;
import com.example.demo.entity.sys.SysResourceDO;
import com.example.demo.service.sys.SysResourceService;
import com.example.demo.util.container.ContainerUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.constant.sys.CacheConst.SYS_PERMISSION_TREE;
import static com.example.demo.constant.sys.CommonConst.STATUS_NORMAL;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 资源 Service
 **/
@CacheConfig(cacheNames = SYS_PERMISSION_TREE)
@Service
public class SysResourceServiceImpl implements SysResourceService {

    private SysResourceDao sysResourceDao;
    private SysPermissionResourceMidDao sysPermissionResourceMidDao;
    private AuthComponent authComponent;

    @Autowired
    public SysResourceServiceImpl(SysResourceDao sysResourceDao,
                                  SysPermissionResourceMidDao sysPermissionResourceMidDao,
                                  AuthComponent authComponent) {
        this.sysResourceDao = sysResourceDao;
        this.sysPermissionResourceMidDao = sysPermissionResourceMidDao;
        this.authComponent = authComponent;
    }

    @Override
    public ResResult list(SysResourceDO query) throws ServiceException {
        PageHelper.startPage(query);
        List<SysResourceDO> list = sysResourceDao.list(query);
        if (ContainerUtil.isNotEmpty(list)) {
            long total = ((Page) list).getTotal();
            ResList<SysResourceDO> resList = ResList.page(list, total);
            return ResResult.success(resList);
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }


    @CacheEvict(allEntries = true)
    @Override
    public ResResult save(SysResourceDO object) throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        object.setCreateTime(now);
        object.setUpdateTime(now);
        return sysResourceDao.save(object) > 0 ? ResResult.success() : ResResult.fail();
    }

    @CacheEvict(allEntries = true)
    @Override
    public ResResult update(SysResourceDO object) throws ServiceException {
        object.setUpdateTime(LocalDateTime.now());
        return sysResourceDao.update(object) > 0 ? ResResult.success() : ResResult.fail();
    }

    @CacheEvict(allEntries = true)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult deleteByIds(Serializable[] ids) throws ServiceException {
        DelResInfo delResInfo = new DelResInfo();
        for (Serializable id : ids) {
            if (!sysPermissionResourceMidDao.isExistByResourceId(id) && sysResourceDao.deleteById(id) > 0) {
                // 不存在且已删除则将 id 添加到已删除列表
                delResInfo.addDeleted(id);
            } else {
                delResInfo.addNotDelete(id);
            }
        }
        if (ContainerUtil.isNotEmpty(delResInfo.getNotDelete())) {
            return ResResult.response(ResCode.OK, "有资源表对象删除失败", delResInfo);
        }
        return ResResult.success(delResInfo);
    }

    /**
     * 获取资源树
     *
     * @param pid 父id
     * @return r
     * @throws ServiceException e
     */
    @Override
    public ResResult tree(Long pid) throws ServiceException {
        // 获取当前登录人允许的资源
        List<Long> selfResourceIdList = sysResourceDao.listByUserId(authComponent.getPrimaryPrincipal(Long.class));
        List<SysResourceDO> sysResourceList = tree(pid, selfResourceIdList);
        return ContainerUtil.isNotEmpty(sysResourceList) ? ResResult.success(sysResourceList) : ResResult.fail(ResCode.NOT_FOUND);
    }


    /**
     * 设置资源树
     *
     * @param pid                父id
     * @param selfResourceIdList 当前用户拥有的资源权限id列表
     * @return r
     */
    private List<SysResourceDO> tree(Long pid, List<Long> selfResourceIdList) {
        List<SysResourceDO> childList = sysResourceDao.listByPidAndStatus(pid, STATUS_NORMAL);
        if (ContainerUtil.isNotEmpty(childList)) {
            childList.forEach(sysResource -> {
                // 设置用户是否拥有此资源权限
                sysResource.setHasPermission(selfResourceIdList.contains(sysResource.getId()));
                // 设置子级列表(递归)
                sysResource.setChildList(tree(sysResource.getId(), selfResourceIdList));
            });
        }
        return childList;
    }
}
