package com.example.demo.entity.log;

import com.example.demo.constant.log.ActionLogEnum;
import com.example.demo.entity.PageBean;
import com.example.demo.entity.sys.SysUserDO;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/05/15
 * @description: 类描述: 操作日志 Model
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionLogDO extends PageBean implements Serializable {

    private static final long serialVersionUID = -1754421066697144716L;
    /**
     * id
     */
    private Long id;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 操作人身份
     */
    private String identify;
    /**
     * 接口描述
     */
    private String description;
    /**
     * 接口地址
     */
    private String uri;
    /**
     * 请求参数
     */
    private String param;
    /**
     * 接口处理时长(毫秒)
     */
    private Long duration;
    /**
     * ip
     */
    private String ip;
    /**
     * 访问时间
     */
    private LocalDateTime visitTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /* ------------------ 非数据库数据分割线 ------------------ */

    /* ------------------ 展示使用 ------------------ */
    /**
     * 用户名
     */
    private String username;
    /**
     * 登录名
     */
    private String account;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;

    public void putUserInfo(SysUserDO sysUser) {
        if (Objects.nonNull(sysUser)) {
            this.username = sysUser.getUsername();
            this.account = sysUser.getAccount();
            this.mobile = sysUser.getMobile();
            this.email = sysUser.getEmail();
        }
    }

    /* ------------------ 查询使用 ------------------ */
    /**
     * 操作类型列表
     *
     * @see ActionLogEnum#name()
     */
    private List<String> typeList;
    /**
     * 查询-开始时间
     */
    private LocalDateTime startTime;
    /**
     * 查询-结束时间
     */
    private LocalDateTime endTime;

}
