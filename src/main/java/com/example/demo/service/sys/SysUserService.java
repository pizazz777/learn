package com.example.demo.service.sys;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResResult;
import com.example.demo.entity.sys.SysUserDO;
import com.example.demo.service.BaseService;

import javax.servlet.http.HttpServletRequest;

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

}
