package com.example.demo.advise;

import com.example.demo.component.exception.*;
import com.example.demo.component.response.ResResult;
import com.example.demo.manager.log.ExceptionLogRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.login.AccountExpiredException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import static com.example.demo.component.response.ResCode.*;

/**
 * @author Administrator
 * @date 2020-05-15 16:11
 * @description: @ControllerAdvice 全局异常捕捉处理
 */
@Slf4j
@ControllerAdvice
public class ExceptionLogControllerAdvice {

    private ExceptionLogRequest exceptionLogRequest;
    private HttpServletRequest request;

    @Autowired
    public ExceptionLogControllerAdvice(ExceptionLogRequest exceptionLogRequest, HttpServletRequest request) {
        this.exceptionLogRequest = exceptionLogRequest;
        this.request = request;
    }

    /**
     * 记录异常信息,返回 json 响应信息给前端
     *
     * @param throwable 异常
     * @return r
     */
    @ResponseBody
    @ExceptionHandler
    public String handleException(Throwable throwable) {
        // 保存异常信息
        try {
            exceptionLogRequest.saveExceptionLog(request, throwable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回 json 错误信息
        ResResult result = getResultByException(ResResult.fail(HANDLE_EXCEPTION), throwable);
        return result.getStr();
    }

    /**
     * {@link ServiceException} 服务异常
     * </p>
     * 获取返回结果
     */
    private ResResult getResultByException(ResResult result, Throwable e) {
        Class clazz = e.getClass();
        // 自定义异常
        if (clazz.equals(ServiceException.class) && StringUtils.isNotBlank(e.getMessage())) {
            result.setMsg(e.getMessage());
            return result;
        }
        if (clazz.equals(UploadException.class)) {
            result.setMsg(e.getMessage());
            return result;
        }
        if (clazz.equals(MissingServletRequestParameterException.class)) {
            MissingServletRequestParameterException exception = (MissingServletRequestParameterException) e;
            result.setMsg("请求参数不全,参数名:" + exception.getParameterName() + ";参数类型:" + exception.getParameterType());
            result.setResCode(FAILED);
            return result;
        }
        if (clazz.equals(HttpRequestMethodNotSupportedException.class)) {
            HttpRequestMethodNotSupportedException exception = (HttpRequestMethodNotSupportedException) e;
            result.setMsg("当前请求方式" + exception.getMethod() + "不支持");
            result.setResCode(FAILED);
            return result;
        }
        // shiro 登录
        if (e instanceof AuthenticationException) {
            return getAuthenticationExceptionResult(result, clazz);
        }
        // shiro 权限
        if (e instanceof AuthorizationException) {
            return getAuthorizationExceptionResult(result, clazz);
        }
        if (clazz.equals(NullPointerException.class)) {
            result.setMsg(getExceptionMsg("调用了未经初始化或者是不存在的对象", e));
            return result;
        }
        if (clazz.equals(NotFoundException.class)) {
            result.setResCode(NOT_FOUND);
            return result;
        }
        if (clazz.equals(IllegalParamException.class)) {
            result.setResCode(ILLEGAL_PARAM);
            return result;
        }
        if (clazz.equals(IllegalArgumentException.class)) {
            result.setMsg(getExceptionMsg("方法参数错误", e));
            return result;
        }
        if (clazz.equals(SQLException.class)) {
            result.setMsg("操作数据库异常");
            return result;
        }
        if (clazz.equals(UncategorizedSQLException.class)) {
            result.setMsg("操作数据库异常");
            return result;
        }
        if (clazz.equals(IOException.class)) {
            result.setMsg(getExceptionMsg("IO异常", e));
            return result;
        }
        if (clazz.equals(ClassNotFoundException.class)) {
            result.setMsg("指定的类不存在");
            return result;
        }
        if (clazz.equals(ArithmeticException.class)) {
            result.setMsg("数学运算异常");
            return result;
        }
        if (clazz.equals(ArrayIndexOutOfBoundsException.class)) {
            result.setMsg("数组下标越界");
            return result;
        }
        if (clazz.equals(ClassCastException.class)) {
            result.setMsg("类型强制转换错误");
            return result;
        }
        if (clazz.equals(SecurityException.class)) {
            result.setMsg("违背安全原则异常");
            return result;
        }
        if (clazz.equals(NoSuchMethodError.class)) {
            result.setMsg("方法末找到异常");
            return result;
        }
        if (clazz.equals(InternalError.class)) {
            result.setMsg("Java虚拟机发生内部错误");
            return result;
        }
        return result;
    }


    /**
     * 获取验证异常的结果
     */
    private ResResult getAuthenticationExceptionResult(ResResult result, Class clazz) {
        result.setResCode(INCORRECT_LOGIN_INFO);
        if (clazz.equals(UnknownAccountException.class)) {
            result.setMsg("用户名密码错误");
        } else if (clazz.equals(IncorrectCredentialsException.class)) {
            result.setMsg("用户名密码错误");
        } else if (clazz.equals(LockedAccountException.class)) {
            result.setResCode(BANNED);
            result.setMsg("用户被禁用");
        } else if (clazz.equals(AccountExpiredException.class)) {
            result.setMsg("账号已过期");
        } else {
            result.setMsg("登录信息错误");
        }
        return result;
    }

    /**
     * 获取权限异常的结果
     */
    private ResResult getAuthorizationExceptionResult(ResResult result, Class clazz) {
        result.setResCode(NO_AUTH);
        if (clazz.equals(UnauthorizedException.class)) {
            result.setMsg("没有权限");
        } else if (clazz.equals(NoBackgroundAuthException.class)) {
            result.setMsg("无后台管理登录权限");
        } else if (clazz.equals(UnauthenticatedException.class)) {
            result.setMsg("未登录或登录信息失效");
            result.setResCode(NOT_LOGIN);
        } else {
            result.setMsg("没有权限");
        }
        return result;
    }

//    /**
//     * 获取 NamingException 的结果
//     */
//    private ResResult getNamingExceptionResult(ResResult result, NamingException e) {
//        String msg = e.getMessage();
//        result.setResCode(INCORRECT_LOGIN_INFO);
//        if (msg.contains("525")) {
//            result.setMsg("LDAP:用户不存在");
//        } else if (msg.contains("52e")) {
//            result.setMsg("LDAP:凭证错误");
//        } else if (msg.contains("530")) {
//            result.setResCode(NOT_IN_LEGAL_LOGIN_TIME);
//            result.setMsg("LDAP:此时不允许登录");
//        } else if (msg.contains("531")) {
//            result.setMsg("LDAP:在此工作站上不允许登录");
//        } else if (msg.contains("532")) {
//            result.setMsg("LDAP:密码过期");
//        } else if (msg.contains("533")) {
//            result.setResCode(BANNED);
//            result.setMsg("LDAP:账户禁用");
//        } else if (msg.contains("701")) {
//            result.setResCode(NOT_IN_LEGAL_LOGIN_TIME);
//            result.setMsg("LDAP:账户过期");
//        } else if (msg.contains("773")) {
//            result.setMsg("LDAP:用户必须重置密码");
//        } else if (msg.contains("775")) {
//            result.setResCode(BANNED);
//            result.setMsg("LDAP:用户账户锁定");
//        } else {
//            result.setResCode(HANDLE_EXCEPTION);
//            result.setMsg("LDAP:错误,原始错误信息为:" + e.getMessage());
//        }
//        return result;
//    }

    /**
     * 获取异常信息
     */
    private String getExceptionMsg(String defaultMsg, Throwable e) {
        return Objects.nonNull(e.getMessage()) ? e.getMessage() : defaultMsg;
    }

}
