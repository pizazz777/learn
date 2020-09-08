package com.example.demo.manager.sys;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.entity.sys.SysUserDO;

import java.util.List;

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


    /**
     * 获取当前用户的指定权限的最大权限范围
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
    List<Long> getSelfDeptIdListByPermission(Long userId, String permission) throws ServiceException;

}
