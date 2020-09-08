package com.example.demo.entity.sys;

import lombok.*;
import com.example.demo.entity.PageBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hxx
 * @version 1.0.0
 * @date 2020/08/26
 * @description 系统权限表
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统权限")
public class SysPermissionDO extends PageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("权限标题")
    private String title;

    @ApiModelProperty("父级权限主键")
    private Long pid;

    @ApiModelProperty("父级权限标题")
    private String pTitle;

    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("权限描述")
    private String description;

    @ApiModelProperty("权限访问路径")
    private String url;

    @ApiModelProperty("权限类型")
    private Integer type;

    @ApiModelProperty("权限排序")
    private Integer seq;

    @ApiModelProperty("权限状态")
    private Integer status;

    @ApiModelProperty("权限图标")
    private String icon;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    /* ------------------ 非数据库数据分割线 ------------------ */

    @ApiModelProperty("子级权限列表")
    private List<SysPermissionDO> childList;

    @ApiModelProperty("关联的资源对象列表")
    private List<SysResourceDO> resourceList;

}
