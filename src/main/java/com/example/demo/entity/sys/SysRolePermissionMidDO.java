package com.example.demo.entity.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import com.example.demo.entity.PageBean;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 角色权限 Model
 **/
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRolePermissionMidDO extends PageBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色主键")
    private Long roleId;

    @ApiModelProperty("权限主键")
    private Long permissionId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /* ------------------ 非数据库数据分割线 ------------------ */

}
