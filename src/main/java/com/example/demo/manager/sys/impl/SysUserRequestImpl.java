package com.example.demo.manager.sys.impl;

import com.example.demo.component.AuthComponent;
import com.example.demo.dao.sys.*;
import com.example.demo.entity.sys.*;
import com.example.demo.manager.sys.SysUserRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.huang.exception.ServiceException;
import com.huang.util.container.ContainerUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.constant.sys.CacheConst.SELF_DATA_PERMISSION;
import static com.example.demo.constant.sys.CommonConst.STATUS_NORMAL;

/**
 * @author Administrator
 * @date 2020-04-29 15:47
 */
@Service
public class SysUserRequestImpl implements SysUserRequest {

    private SysUserDao sysUserDao;
    private AuthComponent authComponent;
    private SysResourceDao sysResourceDao;
    private SysRoleDao sysRoleDao;
    private SysRoleDeptMidDao sysRoleDeptMidDao;
    private SysDeptDao sysDeptDao;
    private SysUserDeptMidDao sysUserDeptMidDao;

    @Autowired
    public SysUserRequestImpl(SysUserDao sysUserDao, AuthComponent authComponent,
                              SysResourceDao sysResourceDao, SysRoleDao sysRoleDao,
                              SysRoleDeptMidDao sysRoleDeptMidDao, SysDeptDao sysDeptDao,
                              SysUserDeptMidDao sysUserDeptMidDao) {
        this.sysUserDao = sysUserDao;
        this.authComponent = authComponent;
        this.sysResourceDao = sysResourceDao;
        this.sysRoleDao = sysRoleDao;
        this.sysRoleDeptMidDao = sysRoleDeptMidDao;
        this.sysDeptDao = sysDeptDao;
        this.sysUserDeptMidDao = sysUserDeptMidDao;
    }

    /**
     * 根据id获取
     *
     * @param id 主键
     * @return r
     */
    @Override
    public SysUserDO getById(Long id) {
        return sysUserDao.getByUnique(id, null, null, null);
    }

    /**
     * 根据唯一字段获取(会返回密码)
     *
     * @param account 账号
     * @param email   邮箱
     * @param mobile  手机号码
     * @return 用户
     */
    @Override
    public SysUserDO getByUniqueWithSecret(String account, String email, String mobile) {
        return queryVerify(account, email, mobile) ? null : sysUserDao.getByUniqueWithSecret(null, account, email, mobile);
    }

    /**
     * 根据唯一字段获取
     *
     * @param account 账号
     * @param email   邮箱
     * @param mobile  手机号码
     * @return 用户
     */
    @Override
    public SysUserDO getByUnique(String account, String email, String mobile) {
        return queryVerify(account, email, mobile) ? null : sysUserDao.getByUnique(null, account, email, mobile);
    }


    /**
     * 参数校验
     *
     * @param account 账号
     * @param email   邮箱
     * @param mobile  手机号码
     * @return r
     */
    private boolean queryVerify(String account, String email, String mobile) {
        return StringUtils.isBlank(account) && StringUtils.isBlank(email) && StringUtils.isBlank(mobile);
    }


    /**
     * 获取当前登录者的用户信息,若未登录则返回一个空对象
     *
     * @return r
     * @throws ServiceException e
     */
    @Override
    public SysUserDO getLoginedUserInfo() throws ServiceException {
        SysUserDO sysUserDO = authComponent.getPrimaryPrincipal(SysUserDO.class);
        return Objects.nonNull(sysUserDO) ? sysUserDO : new SysUserDO();
    }


    /**
     * 获取当前用户的资源权限符的部门最大权限范围
     * 若角色有ALL,则返回全部部门ID
     * 若角色只有SUB,则返回下级部门ID
     * 若角色只有ASSIGN,则返回指定部门ID
     * 若角色同时SUB和ASSIGN,则返回两者并集ID
     * 若角色只有SELF,则返回自己所在部门ID
     *
     * @param userId     用户id
     * @param permission 资源权限符
     * @return r
     * @throws ServiceException e
     */
    @Cacheable(cacheNames = SELF_DATA_PERMISSION)
    @Override
    public List<Long> getSelfDeptIdListByPermission(Long userId, String permission) throws ServiceException {
        Set<Long> set = Sets.newLinkedHashSet();
        // 查找用户具有该权限资源符的角色
        List<SysRoleDO> sysRoleList = sysRoleDao.listByUserIdAndPermission(userId, permission, STATUS_NORMAL);
        if (ContainerUtil.isNotEmpty(sysRoleList)) {
            boolean hasAllPermission = false;
            boolean hasSubPermission = false;
            // 关联的单位
            List<SysUserDeptMidDO> sysUserDeptList = sysUserDeptMidDao.listByUserIdAndDeptId(userId, null);
            List<Long> selfDeptIdList = sysUserDeptList.stream().map(SysUserDeptMidDO::getDeptId).distinct().collect(Collectors.toList());

            for (SysRoleDO sysRole : sysRoleList) {
                if (Objects.equals(sysRole.getPermissionRange(), SysRoleDO.PERMISSION_RANGE_ALL)) {
                    // 全部权限
                    hasAllPermission = true;
                    break;
                } else if (Objects.equals(sysRole.getPermissionRange(), SysRoleDO.PERMISSION_RANGE_SUB)) {
                    // 下级权限
                    hasSubPermission = true;
                } else if (Objects.equals(sysRole.getPermissionRange(), SysRoleDO.PERMISSION_RANGE_ASSIGN)) {
                    // 指定权限
                    List<SysRoleDeptMidDO> midList = sysRoleDeptMidDao.listByRoleIdAndDeptId(sysRole.getId(), null);
                    List<Long> deptIdList = midList.stream().map(SysRoleDeptMidDO::getDeptId).distinct().collect(Collectors.toList());
                    set.addAll(deptIdList);
                } else {
                    // 自己单位权限
                    set.addAll(selfDeptIdList);
                }
            }
            if (hasAllPermission) {
                // 有全部权限
                set.addAll(sysDeptDao.listAllId(STATUS_NORMAL));
                return Lists.newArrayList(set);
            } else if (hasSubPermission) {
                // 有下级权限
                set.addAll(getChild(selfDeptIdList));
            }
        }
        return Lists.newArrayList(set);
    }


    /**
     * 获取子级Id
     *
     * @param deptIdList 父级部门id集合
     * @return r
     */
    private List<Long> getChild(List<Long> deptIdList) {
        List<Long> list = Lists.newArrayList(deptIdList);
        if (ContainerUtil.isNotEmpty(deptIdList)) {
            deptIdList.forEach(deptId -> getChildId(deptId, list));
        }
        return list.stream().distinct().collect(Collectors.toList());
    }


    /**
     * 获取子级Id
     *
     * @param pid        父id
     * @param deptIdList 部门id集合
     */
    private void getChildId(Long pid, List<Long> deptIdList) {
        List<SysDeptDO> childList = sysDeptDao.listByPit(pid, STATUS_NORMAL);
        if (ContainerUtil.isNotEmpty(childList)) {
            deptIdList.addAll(childList.stream().map(SysDeptDO::getId).collect(Collectors.toList()));
            childList.forEach(sysDept -> getChildId(sysDept.getId(), deptIdList));
        }
    }


}
