package com.example.demo.entity.sys;

import lombok.*;
import com.example.demo.entity.PageBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author hxx
 * @version 1.0.0
 * @date 2020/08/24
 * @description 系统角色表
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统角色")
public class SysRoleDO extends PageBean implements Serializable {

    /**
     * 角色类型 1.系统角色,2.用户自定义角色
     */
    public static final int TYPE_SYSTEM = 1;
    public static final int TYPE_CUSTOM = 2;
    /**
     * 角色权限范围 1.全部,2.自己所属部门,3.所属部门及以下,4.指定单位
     */
    public static final int PERMISSION_RANGE_ALL = 1;
    public static final int PERMISSION_RANGE_SELF = 2;
    public static final int PERMISSION_RANGE_SUB = 3;
    public static final int PERMISSION_RANGE_ASSIGN = 4;

    private static final long serialVersionUID = -8547656835163720575L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色标题")
    private String title;

    @ApiModelProperty("角色类型 1.系统角色,2.用户自定义角色")
    private Integer type;

    @ApiModelProperty("角色权限范围 1.全部,2.自己所属部门,3.所属机构及以下,4.指定单位")
    private Integer permissionRange;

    @ApiModelProperty("角色状态 1.正常,2.停用")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    /* ------------------ 非数据库数据分割线 ------------------ */

    @ApiModelProperty("数据权限类型为指定部门时,关联的指定部门id集合")
    private List<Long> deptIdList;

    @ApiModelProperty("角色关联的权限id集合")
    private List<Long> permissionIdList;

}
