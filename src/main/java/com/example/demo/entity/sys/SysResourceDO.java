package com.example.demo.entity.sys;

import com.example.demo.entity.PageBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2020/09/07
 * @description 系统资源表
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统资源")
public class SysResourceDO extends PageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("资源标题")
    private String title;

    @ApiModelProperty("前端路由")
    private String router;

    @ApiModelProperty("资源字符串")
    private String permission;

    @ApiModelProperty("资源名称")
    private String name;

    @ApiModelProperty("资源描述")
    private String description;

    @ApiModelProperty("资源类型 1.系统资源,2.自定义资源")
    private Integer type;

    @ApiModelProperty("资源状态")
    private Integer status;

    @ApiModelProperty("系统资源类型")
    private Integer sysType;

    @ApiModelProperty("父级资源主键")
    private Long pid;

    @ApiModelProperty("资源图标")
    private String icon;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    /* ------------------ 非数据库数据分割线 ------------------ */

    @ApiModelProperty("子级列表")
    private List<SysResourceDO> childList;

    @ApiModelProperty("用户是否具有该资源的权限")
    private Boolean hasPermission;

}
