package com.example.demo.configuration;

import com.example.demo.filter.SysAuthFilter;
import com.example.demo.properties.ProjectProperties;
import com.example.demo.realm.SysUserRealm;
import com.example.demo.util.secret.JwtConst;
import com.google.common.collect.Maps;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.Filter;
import java.util.Map;

import static com.example.demo.constant.cache.CacheConst.AUTH_CACHE_PREFIX;
import static com.example.demo.constant.cache.CacheConst.SESSION_CACHE_PREFIX;

/**
 * @author Administrator
 * @date 2020-04-30 14:16
 */
@Configuration
public class ShiroConfig {

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 登录接口 url
        shiroFilterFactoryBean.setLoginUrl("/sys/sys_user/login");
        // 登录成功返回接口 url
        shiroFilterFactoryBean.setSuccessUrl("/sys/sys_user/logined");
        // 无权限接口 url
        shiroFilterFactoryBean.setUnauthorizedUrl("/sys/sys_user/logined");

        Map<String, Filter> filters = Maps.newHashMap();
        final String sysAuthKey = "token";
        // final String rememberMeKey = "rememberMe";
        filters.put(sysAuthKey, new SysAuthFilter());
        // filters.put(rememberMeKey, new SysRememberMeFilter());
        shiroFilterFactoryBean.setFilters(filters);

        // 注意此处使用的是LinkedHashMap，是有顺序的，shiro会按从上到下的顺序匹配验证，匹配了就不再继续验证
        // 所以上面的url要苛刻，宽松的url要放在下面，尤其是"/**"要放到最下面，如果放前面的话其后的验证规则就没作用了。
        Map<String, String> filterChainDefinitionMap = Maps.newLinkedHashMap();
//        filterChainDefinitionMap.put("/test/show", "anon");
        // swagger
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v2/api-docs/**", "anon");

        // druid
        filterChainDefinitionMap.put("/druid/**", "anon");

        // static
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/f/**", "anon");
        filterChainDefinitionMap.put("/", "anon");

        // filterChainDefinitionMap.put("/**", "anon");
        filterChainDefinitionMap.put("/**", sysAuthKey);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * shiro缓存 使用 redisManager 管理
     */
    @Bean
    public CacheManager shiroCacheManager(IRedisManager redisManager, ProjectProperties projectProperties) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        redisCacheManager.setKeyPrefix(AUTH_CACHE_PREFIX);
        redisCacheManager.setExpire((int) projectProperties.getSessionTimeout() / 1000);
        return redisCacheManager;
    }

    @Bean
    public SessionDAO sessionDAO(IRedisManager redisManager) {
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager);
        sessionDAO.setKeyPrefix(SESSION_CACHE_PREFIX);
        return sessionDAO;
    }

    /**
     * 会话管理
     *
     * @param cacheManager      缓存管理
     * @param sessionDAO        会话
     * @param projectProperties 项目配置
     * @return r
     */
    @Bean
    public SessionManager sessionManager(CacheManager cacheManager, SessionDAO sessionDAO, ProjectProperties projectProperties) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(sessionDAO);
        // session 超时时间
        sessionManager.setGlobalSessionTimeout(projectProperties.getSessionTimeout());
        // session 定期校验
        sessionManager.setSessionValidationSchedulerEnabled(true);
        // session 验证间隔时间(毫秒)
        sessionManager.setSessionValidationInterval(projectProperties.getSessionTimeout() / 3);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setCacheManager(cacheManager);

        return sessionManager;
    }

    /**
     * rememberMe cookie配置
     *
     * @return r
     */
    @Bean
    public Cookie cookie() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(10000);
        return cookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager(Cookie cookie) {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(cookie);
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kpr3sd3ag=="));
        return cookieRememberMeManager;
    }

    @Bean
    public SecurityManager securityManager(CacheManager cacheManager, SysUserRealm sysUserRealm, SessionManager sessionManager, RememberMeManager rememberMeManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(cacheManager);
        // 登录认证
        // securityManager.setAuthenticator();
        // 权限认证
        // securityManager.setAuthorizer();
        // 登录权限认证
        securityManager.setRealm(sysUserRealm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }

    /**
     * CORS 过滤器,加在Shiro过滤器之前
     *
     * @return r
     */
    @Bean
    @SuppressWarnings("unchecked")
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(CorsConfiguration.ALL);
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("x-requested-with");
        config.addAllowedHeader("X-Custom-Header");
        config.addExposedHeader("Content-Disposition");
        config.addAllowedHeader(JwtConst.AUTHORIZATION);
        config.addAllowedMethod(HttpMethod.GET.name());
        config.addAllowedMethod(HttpMethod.POST.name());
        config.addAllowedMethod(HttpMethod.PUT.name());
        config.addAllowedMethod(HttpMethod.DELETE.name());
        config.addAllowedMethod(HttpMethod.OPTIONS.name());
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

    /**
     * 实现 spring 管理 shiro bean 的生命周期
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 实现spring的自动代理
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        creator.setUsePrefix(true);
        return creator;
    }

    /**
     * 支持权限验证注解
     * <li>{@link org.apache.shiro.authz.annotation.RequiresAuthentication RequiresAuthentication}</li>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresUser RequiresUser}</li>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresGuest RequiresGuest}</li>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresRoles RequiresRoles}</li>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresPermissions RequiresPermissions}</li>
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
