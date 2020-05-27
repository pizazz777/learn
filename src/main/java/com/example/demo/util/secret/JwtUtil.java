package com.example.demo.util.secret;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author SQM
 * @date 2019/02/12
 * @description 类说明：Json Web Token 工具
 */
public class JwtUtil {

    private JwtUtil() {
    }

    /**
     * 通过 request 获取 subject(如用户名,手机号,邮箱等)
     *
     * @param request
     * @return
     */
    public static String getIdentify(HttpServletRequest request) {
        Claims claims = JwtUtil.parseJwt(JwtUtil.getToken(request), JwtConst.JWT_SECRET);
        return Objects.isNull(claims) ? null : claims.getSubject();
    }

    /**
     * 通过 request 获取 token
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(JwtConst.AUTHORIZATION);
        if (null == token) {
            token = request.getParameter(JwtConst.AUTHORIZATION);
        }
        return token;
    }

    /**
     * 解析 jwt token
     *
     * @param jsonWebToken   jwt 字符串
     * @param base64Security 制作 jwt 时的安全码
     * @return 解析后的 token 主体
     */
    public static Claims parseJwt(String jsonWebToken, String base64Security) {
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken)
                    .getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * map集合参数为自己用户token的一些信息比如id，姓名，工号等。不要将隐私信息放入
     *
     * @param map            自定义加密参数
     * @param userId         制作 jwt 人的唯一标识
     * @param audience       接收jwt的一方
     * @param issuer         jwt签发者
     * @param ttlMillis      签名有效时间(毫秒)
     * @param base64Security 签名私钥
     * @return token
     */
    public static String createJwt(Map<String, Serializable> map, String userId, String audience, String issuer, long ttlMillis, String base64Security) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 生成签名密钥 就是一个base64加密后的字符串
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        Map<String, Object> claims = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // 添加构成JWT的参数
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("type", JwtConst.JWT_ID)
                // 主要内容
                .setClaims(claims)
                // 创建时间
                .setIssuedAt(now)
                // 主题，也差不多是个人的一些信息
                .setSubject(userId)
                // 发送谁
                .setIssuer(issuer)
                // 个人签名
                .setAudience(audience)
                // 第三段密钥
                .signWith(signatureAlgorithm, signingKey);
        // 添加Token过期时间
        if (ttlMillis >= 0) {
            // 过期时
            long expMillis = nowMillis + ttlMillis;
            // 现在是什么时间
            Date exp = new Date(expMillis);
            // 系统时间之前的token都是不可以被承认的
            builder.setExpiration(exp).setNotBefore(now);
        }
        // 生成JWT
        return builder.compact();
    }
}
