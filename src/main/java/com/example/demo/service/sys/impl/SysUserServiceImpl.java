package com.example.demo.service.sys.impl;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.DelResInfo;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResList;
import com.example.demo.component.response.ResResult;
import com.example.demo.dao.sys.SysUserDao;
import com.example.demo.entity.sys.LoginInfo;
import com.example.demo.entity.sys.SysUserDO;
import com.example.demo.manager.sys.SysUserRequest;
import com.example.demo.realm.SysUserRealm;
import com.example.demo.service.sys.SysUserService;
import com.example.demo.util.clz.ClassUtil;
import com.example.demo.util.container.ContainerUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/28
 * @description: 类描述: 系统用户 Service
 **/
@Service
public class SysUserServiceImpl implements SysUserService {

    private SysUserDao sysUserDao;
    private SysUserRequest sysUserRequest;
    private AuthComponent authComponent;
    private SysUserRealm sysUserRealm;

    @Autowired
    public SysUserServiceImpl(SysUserDao sysUserDao,
                              SysUserRequest sysUserRequest,
                              AuthComponent authComponent,
                              SysUserRealm sysUserRealm) {
        this.sysUserDao = sysUserDao;
        this.sysUserRequest = sysUserRequest;
        this.authComponent = authComponent;
        this.sysUserRealm = sysUserRealm;
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

    @Override
    public ResResult list(SysUserDO query) throws ServiceException {
        PageHelper.startPage(query);
        List<SysUserDO> list = sysUserDao.list(query);
        if (ContainerUtil.isNotEmpty(list)) {
            long total = ((Page) list).getTotal();
            ResList<SysUserDO> resList = ResList.page(list, total);
            return ResResult.success(resList);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
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

    @Override
    @Transactional(rollbackFor = Exception.class)
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
}
