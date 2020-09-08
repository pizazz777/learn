package com.example.demo.realm;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.exception.NoBackgroundAuthException;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.constant.sys.LoginTypeEnum;
import com.example.demo.dao.sys.SysResourceDao;
import com.example.demo.dao.sys.SysRoleDao;
import com.example.demo.entity.sys.SysUserDO;
import com.example.demo.manager.sys.SysUserRequest;
import com.example.demo.properties.AuthProperties;
import com.example.demo.util.container.ContainerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.constant.cache.CacheConst.AUTHENTICATION_CACHE_NAME;
import static com.example.demo.constant.cache.CacheConst.AUTHORIZATION_CACHE_NAME;
import static com.example.demo.constant.sys.CommonConst.*;
import static com.example.demo.constant.sys.PermissionConst.SYS_LOGIN;

/**
 * @author Administrator
 * @date 2020-04-29 11:46
 * @description: 权限认证及授权
 */
@Component
@Slf4j
public class SysUserRealm extends AuthorizingRealm {


    private AuthProperties authProperties;
    private AuthComponent authComponent;
    private SysUserRequest sysUserRequest;
    private SysResourceDao sysResourceDao;
    private SysRoleDao sysRoleDao;

    @Autowired
    public SysUserRealm(AuthProperties authProperties, AuthComponent authComponent,
                        SysUserRequest sysUserRequest, SysResourceDao sysResourceDao,
                        SysRoleDao sysRoleDao) {
        this.authProperties = authProperties;
        this.authComponent = authComponent;
        this.sysUserRequest = sysUserRequest;
        this.sysResourceDao = sysResourceDao;
        this.sysRoleDao = sysRoleDao;
        //开启授权缓存
        this.setAuthorizationCachingEnabled(true);
        this.setAuthorizationCacheName(AUTHORIZATION_CACHE_NAME);
        //开启认证缓存
        this.setAuthenticationCachingEnabled(true);
        this.setAuthenticationCacheName(AUTHENTICATION_CACHE_NAME);
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 登录的用户id
        Long id = null;
        try {
            id = authComponent.getPrimaryPrincipal(Long.class);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        if (Objects.equals(ADMIN_ID, id)) {
            // 超级管理员添加所有权限
            authorizationInfo.addStringPermission("*");
        } else {
            // 添加角色
            List<String> roleNameList = sysRoleDao.listRoleNameByUserId(id);
            authorizationInfo.addRoles(roleNameList.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList()));
            // 添加权限资源符
            List<String> permissionNameList = sysResourceDao.listResourcePermissionStringByUserId(id);
            authorizationInfo.addStringPermissions(permissionNameList.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList()));
        }
        return authorizationInfo;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());
        SysUserDO loginUser;
        boolean flag = false;
        if (Objects.equals(username, authProperties.getManagerName()) && Objects.equals(password, authProperties.getManagerPassword())) {
            // 超级管理员
            loginUser = SysUserDO.builder()
                    .id(ADMIN_ID)
                    .account(ADMIN_NAME)
                    .manager(SysUserDO.MANAGER_YES)
                    .build();
            flag = true;
        } else {
            // 查询登录用户信息
            loginUser = loginByKeyWithSecret(username);
            if (Objects.nonNull(loginUser)) {
                // 启用状态
                if (Objects.equals(loginUser.getStatus(), STATUS_NORMAL)) {
                    // 密码匹配
                    if (Objects.equals(loginUser.getPassword(), authComponent.getPasswordInDb(password))) {
                        flag = true;
                    }
                } else {
                    // 禁用的帐号
                    throw new DisabledAccountException();
                }
                // 清空密码
                loginUser.setPassword(null);
            }
        }
        // 判断此用户是否有登录后台的权限
        if (authProperties.getBackgroundManageSystem() && Objects.nonNull(loginUser) && Objects.equals(loginUser.getManager(), SysUserDO.MANAGER_NO)) {
            //判断是否有指定权限
            if (!sysResourceDao.isExistByUserIdAndPermission(loginUser.getId(), SYS_LOGIN)) {
                throw new NoBackgroundAuthException();
            }
        }
        if (flag) {
            // 认证
            SimplePrincipalCollection principals = new SimplePrincipalCollection();
            // 认证主体subject,设定为user的自增id(Long类型)与 user对象(SysUserDO)类型
            principals.add(loginUser, getName());
            principals.add(loginUser.getId(), getName());
            principals.add(username, getName());
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo();
            info.setPrincipals(principals);
            // 证书
            info.setCredentials(password);
            return info;
        }
        // 未知的帐号
        throw new UnknownAccountException();
    }

    /**
     * 清除登陆者缓存,包括权限和认证缓存
     */
    public void clearCached() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }

    /**
     * 清楚全部权限缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        // 获取缓存对象
        Cache<Object, AuthorizationInfo> cache = getAvailableAuthorizationCache();
        if (Objects.nonNull(cache)) {
            Set<Object> keys = cache.keys();
            if (ContainerUtil.isNotEmpty(keys)) {
                keys.forEach(key -> cache.remove(String.valueOf(key)));
            }
        }
    }

    /**
     * 清除权限缓存
     */
    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        if (Objects.nonNull(principals) && !principals.isEmpty()) {
            Cache<Object, AuthorizationInfo> cache = getAvailableAuthorizationCache();
            if (Objects.nonNull(cache)) {
                for (Object principal : principals) {
                    cache.remove(String.valueOf(principal));
                }
            }
        }
    }

    /**
     * 清楚认证缓存
     */
    @Override
    protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        if (Objects.nonNull(principals) && !principals.isEmpty()) {
            Cache<Object, AuthenticationInfo> cache = getAvailableAuthenticationCache();
            if (Objects.nonNull(cache)) {
                for (Object principal : principals) {
                    cache.remove(String.valueOf(principal));
                }
            }
        }
    }

    private Cache<Object, AuthenticationInfo> getAvailableAuthenticationCache() {
        Cache<Object, AuthenticationInfo> cache = getAuthenticationCache();
        boolean authCachingEnabled = isAuthenticationCachingEnabled();
        if (cache == null && authCachingEnabled) {
            cache = getAuthenticationCacheLazy();
        }
        return cache;
    }

    private Cache<Object, AuthenticationInfo> getAuthenticationCacheLazy() {
        if (this.getAuthenticationCache() == null) {
            log.trace("No authenticationCache instance set.  Checking for a cacheManager...");
            CacheManager cacheManager = getCacheManager();
            if (cacheManager != null) {
                String cacheName = getAuthenticationCacheName();
                log.debug("CacheManager [{}] configured.  Building authentication cache '{}'", cacheManager, cacheName);
                this.setAuthenticationCache(cacheManager.getCache(cacheName));
            }
        }
        return this.getAuthenticationCache();
    }

    private Cache<Object, AuthorizationInfo> getAvailableAuthorizationCache() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache == null && isAuthorizationCachingEnabled()) {
            cache = getAuthorizationCacheLazy();
        }
        return cache;
    }

    private Cache<Object, AuthorizationInfo> getAuthorizationCacheLazy() {
        if (this.getAuthorizationCache() == null) {
            if (log.isDebugEnabled()) {
                log.debug("No authorizationCache instance set. Checking for a cacheManager...");
            }
            CacheManager cacheManager = getCacheManager();
            if (cacheManager != null) {
                String cacheName = getAuthorizationCacheName();
                if (log.isDebugEnabled()) {
                    log.debug("CacheManager [" + cacheManager + "] has been configured.  Building " +
                            "authorization cache named [" + cacheName + "]");
                }
                this.setAuthorizationCache(cacheManager.getCache(cacheName));
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("No cache or cacheManager properties have been set.  Authorization cache cannot " +
                            "be obtained.");
                }
            }
        }
        return this.getAuthorizationCache();
    }

    /**
     * 查询登录用户信息
     *
     * @param key key
     * @return r
     */
    private SysUserDO loginByKeyWithSecret(String key) {
        SysUserDO loginUser = null;
        List<LoginTypeEnum> loginTypeEnumList = authProperties.getLoginTypeEnumList();
        if (loginTypeEnumList.contains(LoginTypeEnum.ACCOUNT)) {
            loginUser = sysUserRequest.getByUniqueWithSecret(key, null, null);
        }
        if (Objects.isNull(loginUser) && loginTypeEnumList.contains(LoginTypeEnum.EMAIL)) {
            loginUser = sysUserRequest.getByUniqueWithSecret(null, key, null);
        }
        if (Objects.isNull(loginUser) && loginTypeEnumList.contains(LoginTypeEnum.MOBILE)) {
            loginUser = sysUserRequest.getByUniqueWithSecret(null, null, key);
        }
        return loginUser;
    }


}
















