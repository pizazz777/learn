package com.example.demo.manager.sys.impl;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.dao.sys.SysUserDao;
import com.example.demo.entity.sys.SysUserDO;
import com.example.demo.manager.sys.SysUserRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-04-29 15:47
 */
@Service
public class SysUserRequestImpl implements SysUserRequest {

    private SysUserDao sysUserDao;
    private AuthComponent authComponent;

    @Autowired
    public SysUserRequestImpl(SysUserDao sysUserDao, AuthComponent authComponent) {
        this.sysUserDao = sysUserDao;
        this.authComponent = authComponent;
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
}
