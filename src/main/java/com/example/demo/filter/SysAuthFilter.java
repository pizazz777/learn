package com.example.demo.filter;

import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResResult;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author administrator
 * @version 2020/05/14
 * @description: 类描述: 登录验证过滤器,返回前端相关 json 信息,不作为 bean 注册,直接在{@link ShiroFilterFactoryBean#setFilters(Map)} 中作为一个对象 new 一个
 **/
public class SysAuthFilter extends FormAuthenticationFilter {

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        addResponseHeader(response);
        return super.onPreHandle(request, response, mappedValue);
    }

    /**
     * 新增跨域的缓存时间的请求头
     *
     * @param response 响应对象
     */
    private void addResponseHeader(ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
    }

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
        // 登录接口不拦截
        if (isLoginRequest(request, response)) {
            return true;
        }
        return isAllowed();
    }

    /**
     * token 是否允许访问
     *
     * @param claims token 解析后的请求
     * @return 是否允许访问
     */
    private boolean isAllowed(Claims claims) {
        if (Objects.nonNull(claims)) {
            Subject subject = SecurityUtils.getSubject();
            if (Objects.nonNull(subject)) {
                PrincipalCollection principals = subject.getPrincipals();
                if (null != principals) {
                    Set principalSet = principals.asSet();
                    // userId
                    String claimSubject = claims.getSubject();
                    for (Object principal : principalSet) {
                        if (Objects.equals(String.valueOf(principal), claimSubject)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 是否允许访问
     *
     * @return b
     */
    private boolean isAllowed() {
        Subject subject = SecurityUtils.getSubject();
        return Objects.nonNull(subject) && subject.isAuthenticated();
    }

    /**
     * 访问被拒绝执行此方法
     *
     * @param request  请求
     * @param response 响应
     * @return 被拒绝
     * @throws Exception 异常
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 默认未登录
        ResResult result = ResResult.fail(ResCode.NOT_LOGIN);
        // 输出 json
        String resultStr = result.getStr("未登录或登录信息失效");
        response.getWriter().write(resultStr);
        // out.flush();
        return false;
    }

}
