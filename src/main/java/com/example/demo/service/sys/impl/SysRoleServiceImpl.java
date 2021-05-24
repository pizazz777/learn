package com.example.demo.service.sys.impl;

import com.example.demo.component.response.DelResInfo;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResList;
import com.example.demo.component.response.ResResult;
import com.example.demo.dao.sys.SysRoleDao;
import com.example.demo.dao.sys.SysRoleDeptMidDao;
import com.example.demo.dao.sys.SysRolePermissionMidDao;
import com.example.demo.dao.sys.SysUserRoleMidDao;
import com.example.demo.entity.sys.SysRoleDO;
import com.example.demo.entity.sys.SysRoleDeptMidDO;
import com.example.demo.entity.sys.SysRolePermissionMidDO;
import com.example.demo.realm.SysUserRealm;
import com.example.demo.service.sys.SysRoleService;
import com.github.pagehelper.PageHelper;
import com.huang.exception.ServiceException;
import com.huang.util.container.ContainerUtil;
import org.apache.commons.lang3.StringUtils;
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
import static com.example.demo.constant.sys.CommonConst.STATUS_NORMAL;

/**
 * @author hxx
 * @version 1.0.0
 * @date 2020/08/24
 * @description 系统角色表
 **/
@CacheConfig(cacheNames = SYS_PERMISSION_TREE)
@Service
public class SysRoleServiceImpl implements SysRoleService {

    private SysUserRoleMidDao sysUserRoleMidDao;
    private SysRoleDao sysRoleDao;
    private SysRolePermissionMidDao sysRolePermissionMidDao;
    private SysRoleDeptMidDao sysRoleDeptMidDao;
    private SysUserRealm sysUserRealm;

    @Autowired
    public SysRoleServiceImpl(SysUserRoleMidDao sysUserRoleMidDao, SysRoleDao sysRoleDao,
                              SysRolePermissionMidDao sysRolePermissionMidDao,
                              SysRoleDeptMidDao sysRoleDeptMidDao, SysUserRealm sysUserRealm) {
        this.sysUserRoleMidDao = sysUserRoleMidDao;
        this.sysRoleDao = sysRoleDao;
        this.sysRolePermissionMidDao = sysRolePermissionMidDao;
        this.sysRoleDeptMidDao = sysRoleDeptMidDao;
        this.sysUserRealm = sysUserRealm;
    }

    @Override
    public ResResult list(SysRoleDO query) throws ServiceException {
        if (Objects.isNull(query.getStatus())) {
            query.setStatus(STATUS_NORMAL);
        }
        PageHelper.startPage(query);
        List<SysRoleDO> list = sysRoleDao.list(query);
        if (ContainerUtil.isNotEmpty(list)) {
            // 设置角色关联的权限和部门id
            setRolePermissionAndDeptId(list);
            return ResResult.success(ResList.page(list));
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }

    @CacheEvict(allEntries = true)
    @Override
    public ResResult save(SysRoleDO object) throws ServiceException {
        init(object);
        return sysRoleDao.save(object) > 0 ? ResResult.success(object) : ResResult.fail();
    }

    @CacheEvict(allEntries = true)
    @Override
    public ResResult update(SysRoleDO object) throws ServiceException {
        init(object);
        boolean flag = sysRoleDao.update(object) > 0;
        if (flag && !Objects.equals(object.getPermissionRange(), SysRoleDO.PERMISSION_RANGE_ASSIGN)) {
            // 修改为不是指定单位,把之前和指定单位关联的删掉(有的情况)
            sysRoleDeptMidDao.deleteByRoleIdAndDeptId(object.getId(), null);
        }
        return flag ? ResResult.success(object) : ResResult.fail();
    }

    /*
     * 注解 -> CacheEvict: 标注在需要清除缓存元素的方法或类上
     *  value(也可以叫cacheNames): 要清除的名称,require=true
     *  key: 缓存的key,可以为空(缺省按照方法的所有参数进行组合),其实就是方法的入参数据,
     *      缓存数据就是先根据cacheNames找到缓存,再通过key(方法传入的参数)找到具体的缓存数据
     *  allEntries: 是否清空所有缓存内容,默认false.如果设置为true,忽略指定的key,方法调用后将立即清空该cacheNames所有缓存
     *  beforeInvocation: 是否在方法执行前就清空,默认false,如果设置为true,则在方法还没有执行的时候就清空缓存,出现异常不会执行清除
     */

    /**
     * 删除角色
     *
     * @param ids 角色id数组
     * @return r
     * @throws ServiceException e
     */
    @CacheEvict(allEntries = true)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult deleteByIds(Serializable[] ids) throws ServiceException {
        DelResInfo delResInfo = new DelResInfo();
        for (Serializable id : ids) {
            // 需要没有用户关联次角色才能删除
            if (!sysUserRoleMidDao.isExistsByRoleId(id) && sysRoleDao.deleteById(id) > 0) {
                // 成功删除的添加到删除集合中
                delResInfo.addDeleted(id);
                // 删除和部门的关联
                sysRoleDeptMidDao.deleteByRoleIdAndDeptId(id, null);
                // 删除和权限的关联
                sysRolePermissionMidDao.deleteByRoleIdAndPermissionId(id, null);
            } else {
                delResInfo.addNotDelete(id);
            }
        }
        if (ContainerUtil.isNotEmpty(delResInfo.getNotDelete())) {
            return ResResult.response(ResCode.OK, "有系统角色对象删除失败", delResInfo);
        }
        return ResResult.success(delResInfo);
    }

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
    @CacheEvict(cacheNames = {SYS_PERMISSION_TREE, SELF_DATA_PERMISSION}, allEntries = true)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult setRolePermissionAndDeptId(Long id, List<Long> permissionIdList, Integer permissionRange, List<Long> deptIdList) throws ServiceException {
        // 修改角色权限
        updateRolePermission(id, permissionIdList);
        // 修改角色权限范围
        updatePermissionRange(id, permissionRange);
        // 修改角色指定的部门
        if (Objects.equals(permissionRange, SysRoleDO.PERMISSION_RANGE_ASSIGN)) {
            updateRoleDept(id, deptIdList);
        }
        // 清除缓存
        sysUserRealm.clearAllCachedAuthorizationInfo();

        return ResResult.success();
    }

    /**
     * 修改角色指定的部门
     *
     * @param roleId     角色id
     * @param deptIdList 部门id集合
     */
    private void updateRoleDept(Long roleId, List<Long> deptIdList) {
        sysRoleDeptMidDao.deleteByRoleIdAndDeptId(roleId, null);
        LocalDateTime now = LocalDateTime.now();
        deptIdList.forEach(deptId -> {
            SysRoleDeptMidDO build = SysRoleDeptMidDO.builder()
                    .roleId(roleId)
                    .deptId(deptId)
                    .createTime(now)
                    .build();
            sysRoleDeptMidDao.save(build);
        });
    }

    /**
     * 修改角色权限范围
     *
     * @param id              角色id
     * @param permissionRange 角色权限范围
     */
    private void updatePermissionRange(Long id, Integer permissionRange) {
        SysRoleDO build = SysRoleDO.builder()
                .id(id)
                .permissionRange(permissionRange)
                .updateTime(LocalDateTime.now())
                .build();
        sysRoleDao.update(build);
    }

    /**
     * 修改角色权限
     *
     * @param roleId           角色id
     * @param permissionIdList 权限id集合
     */
    private void updateRolePermission(Long roleId, List<Long> permissionIdList) {
        sysRolePermissionMidDao.deleteByRoleIdAndPermissionId(roleId, null);
        LocalDateTime now = LocalDateTime.now();
        permissionIdList.forEach(permissionId -> {
            SysRolePermissionMidDO build = SysRolePermissionMidDO.builder()
                    .permissionId(permissionId)
                    .roleId(roleId)
                    .createTime(now)
                    .build();
            sysRolePermissionMidDao.save(build);
        });
    }

    /**
     * 根据角色id获取权限
     *
     * @param id 角色id
     * @return r
     * @throws ServiceException e
     */
    @Override
    public ResResult listPermissionIdById(Long id) throws ServiceException {
        List<SysRolePermissionMidDO> list = sysRolePermissionMidDao.listByRoleIdAndPermissionId(id, null);
        List<Long> permissionIdList = list.stream().map(SysRolePermissionMidDO::getPermissionId).distinct().collect(Collectors.toList());
        return ContainerUtil.isNotEmpty(permissionIdList) ? ResResult.success(permissionIdList) : ResResult.fail(ResCode.NOT_FOUND);
    }


    /**
     * 初始化角色对象
     *
     * @param object 角色对象
     * @throws ServiceException e
     */
    private void init(SysRoleDO object) throws ServiceException {
        checkNameExists(object);
        LocalDateTime now = LocalDateTime.now();
        if (Objects.isNull(object.getId())) {
            object.setCreateTime(now);
            object.setStatus(STATUS_NORMAL);
        }
        object.setUpdateTime(now);
        if (Objects.isNull(object.getPermissionRange())) {
            // 权限范围默认全部
            object.setPermissionRange(SysRoleDO.PERMISSION_RANGE_ALL);
        }
    }

    /**
     * 判断角色名称是否存在
     *
     * @param object 角色对象
     * @throws ServiceException e
     */
    private void checkNameExists(SysRoleDO object) throws ServiceException {
        String name = object.getName();
        if (Objects.nonNull(object.getId())) {
            // 修改时
            SysRoleDO oldSysRole = sysRoleDao.getById(object.getId());
            if (StringUtils.isNotBlank(name) && !Objects.equals(name, oldSysRole.getName()) && sysRoleDao.isExistsByName(name)) {
                throw new ServiceException("角色名称已存在");
            }
        } else {
            // 新增时
            if (StringUtils.isNotBlank(name) && sysRoleDao.isExistsByName(name)) {
                throw new ServiceException("角色名称已存在");
            }
        }
    }

    /**
     * 设置角色关联的权限和部门id
     *
     * @param list 角色集合
     */
    private void setRolePermissionAndDeptId(List<SysRoleDO> list) {
        list.forEach(sysRole -> {
            // 设置权限的
            List<SysRolePermissionMidDO> sysRolePermissionMidList = sysRolePermissionMidDao.listByRoleIdAndPermissionId(sysRole.getId(), null);
            sysRole.setPermissionIdList(sysRolePermissionMidList.stream().map(SysRolePermissionMidDO::getPermissionId).collect(Collectors.toList()));
            // 设置部门的
            if (Objects.equals(sysRole.getPermissionRange(), SysRoleDO.PERMISSION_RANGE_ASSIGN)) {
                List<SysRoleDeptMidDO> sysRoleDeptMidList = sysRoleDeptMidDao.listByRoleIdAndDeptId(sysRole.getId(), null);
                sysRole.setDeptIdList(sysRoleDeptMidList.stream().map(SysRoleDeptMidDO::getDeptId).collect(Collectors.toList()));
            }
        });

    }
}
