package com.example.demo.service.sys.impl;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.response.DelResInfo;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResList;
import com.example.demo.component.response.ResResult;
import com.example.demo.dao.sys.SysPermissionDao;
import com.example.demo.dao.sys.SysPermissionResourceMidDao;
import com.example.demo.dao.sys.SysRolePermissionMidDao;
import com.example.demo.entity.sys.SysPermissionDO;
import com.example.demo.entity.sys.SysPermissionResourceMidDO;
import com.example.demo.manager.sys.SysPermissionRequest;
import com.example.demo.realm.SysUserRealm;
import com.example.demo.service.sys.SysPermissionService;
import com.github.pagehelper.PageHelper;
import com.huang.exception.ServiceException;
import com.huang.util.container.ContainerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.demo.constant.sys.CacheConst.SELF_DATA_PERMISSION;
import static com.example.demo.constant.sys.CacheConst.SYS_PERMISSION_TREE;
import static com.example.demo.constant.sys.CommonConst.ADMIN_ID;
import static com.example.demo.constant.sys.CommonConst.STATUS_NORMAL;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 权限 Service
 **/
/*
 * CacheConfig(cacheNames="name"): 用于标注在类上,可以存放该类中所有缓存的公有属性,比如设置缓存的名字
 */
@CacheConfig(cacheNames = SYS_PERMISSION_TREE)
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    private SysPermissionDao sysPermissionDao;
    private SysPermissionRequest sysPermissionRequest;
    private SysRolePermissionMidDao sysRolePermissionMidDao;
    private AuthComponent authComponent;
    private SysPermissionResourceMidDao sysPermissionResourceMidDao;
    private SysUserRealm sysUserRealm;

    @Autowired
    public SysPermissionServiceImpl(SysPermissionDao sysPermissionDao, SysPermissionRequest sysPermissionRequest,
                                    SysRolePermissionMidDao sysRolePermissionMidDao, AuthComponent authComponent,
                                    SysPermissionResourceMidDao sysPermissionResourceMidDao, SysUserRealm sysUserRealm) {
        this.sysPermissionDao = sysPermissionDao;
        this.sysPermissionRequest = sysPermissionRequest;
        this.sysRolePermissionMidDao = sysRolePermissionMidDao;
        this.authComponent = authComponent;
        this.sysPermissionResourceMidDao = sysPermissionResourceMidDao;
        this.sysUserRealm = sysUserRealm;
    }

    @Override
    public ResResult list(SysPermissionDO query) throws ServiceException {
        PageHelper.startPage(query);
        List<SysPermissionDO> list = sysPermissionDao.list(query);
        if (ContainerUtil.isNotEmpty(list)) {
            return ResResult.success(ResList.page(list));
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }


    @CacheEvict(allEntries = true)
    @Override
    public ResResult save(SysPermissionDO object) throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        object.setCreateTime(now);
        object.setUpdateTime(now);
        if (Objects.isNull(object.getPid())) {
            object.setPid(0L);
        }
        return sysPermissionDao.save(object) > 0 ? ResResult.success() : ResResult.fail();
    }

    @CacheEvict(allEntries = true)
    @Override
    public ResResult update(SysPermissionDO object) throws ServiceException {
        if (Objects.equals(object.getId(), object.getPid())) {
            return ResResult.fail(ResCode.ILLEGAL_PARAM, "父级权限ID错误");
        }
        object.setUpdateTime(LocalDateTime.now());
        int update = sysPermissionDao.update(object);
        if (update > 0) {
            SysPermissionDO oldSysPermission = sysPermissionDao.getById(object.getId());
            if (!Objects.equals(oldSysPermission.getTitle(), object.getTitle())) {
                sysPermissionDao.updatePTitleByPid(object.getTitle(), object.getId());
            }
            return ResResult.success();
        }
        return ResResult.fail();
    }

    @CacheEvict(allEntries = true)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult deleteByIds(Serializable[] ids) throws ServiceException {
        /*
         * 1.没有角色使用此权限
         * 2.没有子级权限
         * 3.将删除成功和未成功的id返回给前端
         */
        DelResInfo delResInfo = new DelResInfo();
        for (Serializable id : ids) {
            if (!sysRolePermissionMidDao.isExistByPermissionId(id)
                    && !sysPermissionDao.isExistsByPid(id)
                    && sysPermissionDao.deleteById(id) > 0) {
                // 不存在且已删除则将 id 添加到已删除列表
                delResInfo.addDeleted(id);
                // 删除和资源关联
                sysPermissionResourceMidDao.deleteByPermissionIdAndResourceId(id, null);
            } else {
                delResInfo.addNotDelete(id);
            }
        }
        if (ContainerUtil.isNotEmpty(delResInfo.getNotDelete())) {
            return ResResult.response(ResCode.OK, "有权限表对象删除失败", delResInfo);
        }
        return ResResult.success(delResInfo);
    }

    /**
     * 根据登录用户和pid筛选用户权限下的权限
     *
     * @param pid 父级id
     * @return r
     * @throws ServiceException e
     */
    @Override
    public ResResult tree(Long pid) throws ServiceException {
        Long principal = authComponent.getPrimaryPrincipal(Long.class);
        List<SysPermissionDO> tree;
        if (Objects.equals(principal, ADMIN_ID)) {
            tree = sysPermissionRequest.getTree(pid);
        } else {
            // 此用户有权限的权限id集合
            List<Long> permissionIdList = sysRolePermissionMidDao.listPermissionIdByUserIdAndStatus(principal, STATUS_NORMAL);
            tree = sysPermissionRequest.getTree(pid, permissionIdList);
        }
        return ContainerUtil.isNotEmpty(tree) ? ResResult.success(tree) : ResResult.fail(ResCode.NOT_FOUND);
    }

    /**
     * 设置权限资源
     *
     * @param id             权限id
     * @param resourceIdList 资源id集合
     * @return r
     * @throws ServiceException e
     */
    @CacheEvict(cacheNames = {SYS_PERMISSION_TREE, SELF_DATA_PERMISSION}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResResult setPermissionResource(Long id, List<Long> resourceIdList) throws ServiceException {
        // 先删除之前关联的
        sysPermissionResourceMidDao.deleteByPermissionIdAndResourceId(id, null);
        // 再重新关联
        LocalDateTime now = LocalDateTime.now();
        resourceIdList.forEach(resourceId -> {
            SysPermissionResourceMidDO build = SysPermissionResourceMidDO.builder()
                    .permissionId(id)
                    .resourceId(resourceId)
                    .createTime(now)
                    .build();
            sysPermissionResourceMidDao.save(build);
        });
        // 清除缓存
        sysUserRealm.clearAllCachedAuthorizationInfo();
        return ResResult.success();
    }

    /**
     * 获取权限对应的资源id集合
     *
     * @param id 权限id
     * @return r
     * @throws ServiceException e
     */
    @Override
    public ResResult listResourceId(Long id) throws ServiceException {
        List<SysPermissionResourceMidDO> list = sysPermissionResourceMidDao.listByPermissionIdAndResourceId(id, null);
        if (ContainerUtil.isNotEmpty(list)) {
            List<Long> resourceIdList = list.stream().map(SysPermissionResourceMidDO::getResourceId).collect(Collectors.toList());
            return ResResult.success(resourceIdList);
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }
}
