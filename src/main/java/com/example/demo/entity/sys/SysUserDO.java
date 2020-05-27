package com.example.demo.entity.sys;

import com.example.demo.entity.PageBean;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/28
 * @description: 类描述: 系统用户 Model
 **/
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysUserDO extends PageBean implements Serializable {

    /**
     * 是否启用 1.启用,2.停用
     */
    public static final int ENABLE_NORMAL = 1;
    public static final int ENABLE_BAN = 2;
    /**
     * 是否超级管理员 1.是,2.否
     */
    public static final int MANAGER_YES = 1;
    public static final int MANAGER_NO = 2;
    /**
     * 性别 1.男,2.女
     */
    public static final int SEX_MAN = 1;
    public static final int SEX_WOMAN = 2;

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 登录名
     */
    private String account;
    /**
     * 姓名
     */
    private String username;
    /**
     * 电话
     */
    private String phone;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 备注
     */
    private String remark;
    /**
     * 描述
     */
    private String description;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否启用 1.启用,2.停用
     */
    private Integer enable;
    /**
     * 是否超级管理员 1.是,2.否
     */
    private Integer manager;
    /**
     * 性别 1.男,2.女
     */
    private Integer sex;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建人
     */
    private Long createUserId;

    /* ------------------ 非数据库数据分割线 ------------------ */

    /**
     * 关联的角色
     */
    private List<SysRoleDO> roleList;


}
