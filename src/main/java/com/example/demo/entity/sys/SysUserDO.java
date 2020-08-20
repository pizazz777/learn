package com.example.demo.entity.sys;

import com.example.demo.entity.PageBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hxx
 * @date 2020/04/28
 * @description: 类描述: 系统用户 Model
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserDO extends PageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态 1.正常,2.禁用
     */
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_BAN = 2;
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
    /**
     * 用户类型 1.系统内用户,2.外部临时用户
     */
    public static final int TYPE_SYSTEM = 1;
    public static final int TYPE_OUTER = 2;
    /**
     * 工作状态 1.正常,2.休假,3.出差
     */
    public static final int WORKING_STATUS_NORMAL = 1;
    public static final int WORKING_STATUS_VACATION = 2;
    public static final int WORKING_STATUS_BUSINESS_TRIP = 3;


    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("部门ID")
    private Long deptId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("用户头像地址")
    private String headImgUrl;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("级别")
    private Integer level;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("编号")
    private String number;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("性别 1.男,2.女")
    private Integer sex;

    @ApiModelProperty("是否超级管理员 1.是,2.否")
    private Integer manager;

    @ApiModelProperty("状态 1.正常,2.禁用")
    private Integer status;

    @ApiModelProperty("职务名称")
    private String title;

    @ApiModelProperty("用户类型 1.系统内用户,2.外部临时用户")
    private Integer type;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("用户姓名")
    private String username;

    @ApiModelProperty("有效期结束时间")
    private LocalDateTime validEndTime;

    @ApiModelProperty("有效期开始时间")
    private LocalDateTime validStartTime;

    @ApiModelProperty("工作状态 1.正常,2.休假,3.出差")
    private Integer workingStatus;

    /* ------------------ 非数据库数据分割线 ------------------ */

    /**
     * 关联的角色
     */
    private List<SysRoleDO> roleList;

}
