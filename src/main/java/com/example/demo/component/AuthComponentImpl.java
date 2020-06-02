package com.example.demo.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.properties.JwtProperties;
import com.example.demo.util.secret.JwtConst;
import com.example.demo.util.secret.JwtUtil;
import com.example.demo.util.secret.Md5Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-04-29 11:20
 */
@Component
public class AuthComponentImpl implements AuthComponent {

    private JwtProperties jwtProperties;

    @Autowired
    public AuthComponentImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * shiro 登录认证, 成功返回 token
     *
     * @param username 用户名
     * @param password 密码
     * @return r
     * @throws ServiceException e
     */
    @Override
    public String loginReturnToken(String username, String password) throws ServiceException {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        return verifyLoginToken(token);
    }

    /**
     * 获取主要验证信息: 用户对象或自增id
     *
     * @param clazz 类型
     * @return r
     * @throws ServiceException e
     */
    @Override
    public <T> T getPrimaryPrincipal(Class<T> clazz) throws ServiceException {
        Subject subject = SecurityUtils.getSubject();
        if (Objects.nonNull(subject)) {
            PrincipalCollection principalCollection = subject.getPrincipals();
            if (Objects.nonNull(principalCollection) && !principalCollection.isEmpty()) {
                return principalCollection.oneByType(clazz);
            }
        }
        throw new ServiceException("未找到登录信息!");
    }

    /**
     * 校验token
     *
     * @param token 令牌
     * @return r
     * @throws ServiceException e
     */
    private String verifyLoginToken(AuthenticationToken token) throws ServiceException {
        Subject subject = SecurityUtils.getSubject();
        // 使用Realm登录
        subject.login(token);
        // 获取登录用户信息中的自增id
        String id = String.valueOf(getPrimaryPrincipal(Long.class));
        Map<String, Serializable> tokenMap = JSON.parseObject(JSON.toJSONString(token), new TypeReference<Map<String, Serializable>>() {
        });
        // 有效时间
        long jwtTimeToLive = jwtProperties.getJwtTimeToLive();
        // 接收方
        String audience = jwtProperties.getAudience();
        // 签发方
        String issuer = jwtProperties.getIssuer();

        return JwtUtil.createJwt(tokenMap, id, audience, issuer, jwtTimeToLive, JwtConst.JWT_SECRET);
    }

    /**
     * 获取数据库内密码保存的形式的密码
     *
     * @param password 密码
     * @return 加密后的密码
     */
    @Override
    public String getPasswordInDb(String password) {
        return Md5Util.getMd5(password);
    }
}
