package com.example.demo.service.sys.impl;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.response.DelResInfo;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResList;
import com.example.demo.component.response.ResResult;
import com.example.demo.dao.sys.SysRoleDao;
import com.example.demo.dao.sys.SysUserDao;
import com.example.demo.dao.sys.SysUserRoleMidDao;
import com.example.demo.entity.sys.LoginInfo;
import com.example.demo.entity.sys.SysUserDO;
import com.example.demo.entity.sys.SysUserRoleMidDO;
import com.example.demo.manager.sys.SysUserRequest;
import com.example.demo.realm.SysUserRealm;
import com.example.demo.service.sys.SysUserService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.huang.exception.ServiceException;
import com.huang.util.container.ContainerUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.example.demo.constant.sys.CacheConst.*;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/28
 * @description: 类描述: 系统用户 Service
 **/
@CacheConfig(cacheNames = SYS_USER_LIST)
@Service
public class SysUserServiceImpl implements SysUserService {

    private SysUserDao sysUserDao;
    private SysUserRequest sysUserRequest;
    private AuthComponent authComponent;
    private SysUserRealm sysUserRealm;
    private SysUserRoleMidDao sysUserRoleMidDao;
    private SysRoleDao sysRoleDao;

    @Autowired
    public SysUserServiceImpl(SysUserDao sysUserDao,
                              SysUserRequest sysUserRequest,
                              AuthComponent authComponent,
                              SysUserRealm sysUserRealm,
                              SysUserRoleMidDao sysUserRoleMidDao,
                              SysRoleDao sysRoleDao) {
        this.sysUserDao = sysUserDao;
        this.sysUserRequest = sysUserRequest;
        this.authComponent = authComponent;
        this.sysUserRealm = sysUserRealm;
        this.sysUserRoleMidDao = sysUserRoleMidDao;
        this.sysRoleDao = sysRoleDao;
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return r
     * @throws ServiceException e
     */
    @Override
    public ResResult login(String username, String password) throws ServiceException {
        String token = authComponent.loginReturnToken(username, password);
        SysUserDO loginUser = sysUserRequest.getLoginedUserInfo();
        if (StringUtils.isNotBlank(token)) {
            return ResResult.success(LoginInfo.builder().token(token).user(loginUser).build());
        }
        return ResResult.fail(ResCode.INCORRECT_LOGIN_INFO);
    }

    /**
     * 登出
     *
     * @param request 请求
     * @return r
     * @throws ServiceException e
     */
    @Override
    public ResResult logout(HttpServletRequest request) throws ServiceException {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        sysUserRealm.clearCached();
        return ResResult.success();
    }

    /**
     * 登录状态
     *
     * @param request 请求对象
     * @return r
     */
    @Override
    public ResResult logined(HttpServletRequest request) {
        return ResResult.success();
    }

    @Override
    public ResResult list(SysUserDO query) throws ServiceException {
        PageHelper.startPage(query);
        List<SysUserDO> list = sysUserDao.list(query);
        if (ContainerUtil.isNotEmpty(list)) {
            return ResResult.success(ResList.page(list));
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }

    @Override
    public ResResult getById(Serializable id) throws ServiceException {
        SysUserDO object = sysUserDao.getById(id);
        if (Objects.nonNull(object)) {
            return ResResult.success(object);
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }

    @CacheEvict(allEntries = true)
    @Override
    public ResResult save(SysUserDO object) throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        object.setCreateTime(now);
        object.setUpdateTime(now);
        int save = sysUserDao.save(object);
        if (save > 0) {
            return ResResult.success(object);
        }
        return ResResult.fail();
    }


    @CacheEvict(allEntries = true)
    @Override
    public ResResult update(SysUserDO object) throws ServiceException {
        object.setUpdateTime(LocalDateTime.now());
        int update = sysUserDao.update(object);
        if (update > 0) {
            return ResResult.success();
        }
        return ResResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult deleteByIds(Serializable[] ids) throws ServiceException {
        DelResInfo delResInfo = new DelResInfo();
        for (Serializable id : ids) {
            if (sysUserDao.deleteById(id) > 0) {
                // 不存在且已删除则将 id 添加到已删除列表
                delResInfo.addDeleted(id);
            } else {
                delResInfo.addNotDelete(id);
            }
        }
        if (ContainerUtil.isNotEmpty(delResInfo.getNotDelete())) {
            return ResResult.response(ResCode.OK, "有用户表对象删除失败", delResInfo);
        }
        return ResResult.success(delResInfo);
    }

    /**
     * 设置用户角色
     *
     * @param userId     用户id
     * @param roleIdList 角色id列表
     * @return r
     * @throws ServiceException e
     */
    @CacheEvict(cacheNames = {SYS_PERMISSION_TREE, SYS_USER_LIST, SELF_DATA_PERMISSION}, allEntries = true)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult setUserRoleList(Long userId, List<Long> roleIdList) throws ServiceException {
        // 删除旧的
        sysUserRoleMidDao.deleteByUserIdAndRoleId(userId, null);
        // 保存新的
        saveUserRoleMid(Lists.newArrayList(userId), roleIdList);
        // 清除缓存
        sysUserRealm.clearAllCachedAuthorizationInfo();
        return ResResult.success();
    }


    /**
     * 设置角色用户
     *
     * @param roleId     角色id
     * @param userIdList 用户id列表
     * @return r
     * @throws ServiceException e
     */
    @CacheEvict(cacheNames = {SYS_PERMISSION_TREE, SYS_USER_LIST, SELF_DATA_PERMISSION}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResResult setRoleUserList(Long roleId, List<Long> userIdList) throws ServiceException {
        // 删除旧的
        sysUserRoleMidDao.deleteByUserIdAndRoleId(null, roleId);
        // 保存新的
        saveUserRoleMid(userIdList, Lists.newArrayList(roleId));
        // 清除缓存
        sysUserRealm.clearAllCachedAuthorizationInfo();
        return ResResult.success();
    }

    /**
     * 根据角色id获取用户
     *
     * @param query  查询对象
     * @param roleId 角色id
     * @return r
     * @throws ServiceException e
     */
    @Cacheable
    @Override
    public ResResult listWithRoleByRoleId(SysUserDO query, Long roleId) throws ServiceException {
        return null;
    }

    /**
     * 根据角色获取用户
     *
     * @param roleId 角色id
     * @return r
     * @throws ServiceException e
     */
    @Override
    public ResResult listByRole(Long roleId) throws ServiceException {
        return null;
    }

    /**
     * 根据权限字符串获取人员列表
     *
     * @param permission 权限字符串
     * @param corpId     单位ID
     * @param name       人员名称
     * @return r
     * @throws ServiceException e
     */
    @Override
    public ResResult listByPermission(String permission, Long corpId, String name) throws ServiceException {
        return null;
    }


    /**
     * 保存用户和角色中间对象
     *
     * @param userIdList 用户id列表
     * @param roleIdList 角色id列表
     */
    private void saveUserRoleMid(List<Long> userIdList, List<Long> roleIdList) {
        if (ContainerUtil.isNotEmpty(userIdList) && ContainerUtil.isNotEmpty(roleIdList)) {
            LocalDateTime now = LocalDateTime.now();
            userIdList.forEach(userId ->
                    roleIdList.forEach(roleId -> {
                        SysUserRoleMidDO build = SysUserRoleMidDO.builder()
                                .userId(userId)
                                .roleId(roleId)
                                .createTime(now)
                                .build();
                        sysUserRoleMidDao.save(build);
                    })
            );
        }
    }


}
