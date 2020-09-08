package com.example.demo.entity.sys;

import com.example.demo.entity.PageBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hxx
 * @version 1.0.0
 * @date 2020/08/24
 * @description 系统部门表
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统部门")
public class SysDeptDO extends PageBean implements Serializable {

    /**
     * 单位状态 1.启用,2.禁用
     */
    public static final int STATUS_ABLE = 1;
    public static final int STATUS_BAN = 2;

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("父级部门")
    private Long pId;

    @ApiModelProperty("父级单位名称")
    private String pName;

    @ApiModelProperty("排序")
    private Integer seq;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("单位负责人")
    private String manager;

    @ApiModelProperty("单位联系人")
    private String contacts;

    @ApiModelProperty("单位联系人电话")
    private String contactsMobile;

    @ApiModelProperty("单位类别")
    private Integer category;

    @ApiModelProperty("单位类型")
    private Integer type;

    @ApiModelProperty("单位状态 1.启用,2.禁用")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    /* ------------------ 非数据库数据分割线 ------------------ */

    @ApiModelProperty("子级列表")
    private List<SysDeptDO> childList;

}
