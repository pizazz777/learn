package com.example.demo.entity.sys;

import lombok.*;

import java.io.Serializable;

/**
 * @author sqm
 * @version 1.0
 * @date 2019/08/30
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
