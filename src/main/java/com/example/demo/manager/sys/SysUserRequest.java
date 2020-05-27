package com.example.demo.manager.sys;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.entity.sys.SysUserDO;

/**
 * @author Administrator
 * @date 2020-04-29 15:48
 */
public interface SysUserRequest {

    /**
     * 根据id获取
     *
     * @param id 主键
     * @return r
     */
    SysUserDO getById(Long id);

    /**
     * 根据唯一字段获取(会返回密码)
     *
     * @param account 账号
     * @param email   邮箱
     * @param mobile  手机号码
     * @return 用户
     */
    SysUserDO getByUniqueWithSecret(String account, String email, String mobile);

    /**
     * 根据唯一字段获取
     *
     * @param account 账号
     * @param email   邮箱
     * @param mobile  手机号码
     * @return 用户
     */
    SysUserDO getByUnique(String account, String email, String mobile);

    /**
     * 获取当前登录者的用户信息,若未登录则返回一个空对象
     *
     * @return r
     * @throws ServiceException e
     */
    SysUserDO getLoginedUserInfo() throws ServiceException;

}
