package com.example.demo.service.sys;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResResult;
import com.example.demo.entity.sys.SysUserDO;
import com.example.demo.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/28
 * @description: 类描述: 系统用户 Service
 **/
public interface SysUserService extends BaseService<SysUserDO> {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return r
     * @throws ServiceException e
     */
    ResResult login(String username, String password) throws ServiceException;


    /**
     * 登出
     *
     * @param request 请求
     * @return r
     * @throws ServiceException e
     */
    ResResult logout(HttpServletRequest request) throws ServiceException;


    /**
     * 登录状态
     *
     * @param request 请求对象
     * @return r
     */
    ResResult logined(HttpServletRequest request);


    /**
     * 设置用户角色
     *
     * @param userId     用户id
     * @param roleIdList 角色id列表
     * @return r
     * @throws ServiceException e
     */
    ResResult setUserRoleList(Long userId, List<Long> roleIdList) throws ServiceException;


    /**
     * 设置角色用户
     *
     * @param roleId     角色id
     * @param userIdList 用户id列表
     * @return r
     * @throws ServiceException e
     */
    ResResult setRoleUserList(Long roleId, List<Long> userIdList) throws ServiceException;


    /**
     * 根据角色id获取用户
     *
     * @param query  查询对象
     * @param roleId 角色id
     * @return r
     * @throws ServiceException e
     */
    ResResult listWithRoleByRoleId(SysUserDO query, Long roleId) throws ServiceException;


    /**
     * 根据角色获取用户
     *
     * @param roleId 角色id
     * @return r
     * @throws ServiceException e
     */
    ResResult listByRole(Long roleId) throws ServiceException;


    /**
     * 根据权限字符串获取人员列表
     *
     * @param permission 权限字符串
     * @param corpId     单位ID
     * @param name       人员名称
     * @return r
     * @throws ServiceException e
     */
    ResResult listByPermission(String permission, Long corpId, String name) throws ServiceException;

}
