package com.example.demo.entity.sys;

import lombok.*;
import com.example.demo.entity.PageBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hxx
 * @version 1.0.0
 * @date 2020/08/24
 * @description 系统角色和部门关联表
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统角色和部门关联")
public class SysRoleDeptMidDO extends PageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("部门ID")
    private Long deptId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /* ------------------ 非数据库数据分割线 ------------------ */

}
