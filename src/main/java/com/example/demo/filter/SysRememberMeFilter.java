package com.example.demo.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author sqm
 * @version 2019/02/21
 * @description: 类描述: 登录验证过滤器 加入 rememberMe
 **/
public class SysRememberMeFilter extends SysAuthFilter {
    /**
     * 验证 token 等相关验证信息,判定请求是否能够通过
     *
     * @param request     请求
     * @param response    响应
     * @param mappedValue 请求数据
     * @return <code>true</code> if request should be allowed access
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = SecurityUtils.getSubject();
        if (null != subject) {
            PrincipalCollection principals = subject.getPrincipals();
            boolean flag = principals != null && !principals.isEmpty();
            return flag || super.isAccessAllowed(request, response, mappedValue);
        }
        return false;
    }
}
