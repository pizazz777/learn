package com.example.demo.entity.sys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author administrator
 * @date 2020/05/30
 * @description: 类描述: 登录成功后返回给前端的对象
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo implements Serializable {

    private static final long serialVersionUID = 404285272057046785L;
    /**
     * JWT token
     */
    private String token;

    /**
     * 用户对象
     */
    private SysUserDO user;
}
