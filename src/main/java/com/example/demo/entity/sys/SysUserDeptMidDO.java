package com.example.demo.entity.sys;

import lombok.*;
import com.example.demo.entity.PageBean;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/08/20
 * @description: 类描述: 系统用户和部门关联 Model
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserDeptMidDO extends PageBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户表ID")
    private Long userId;

    @ApiModelProperty("部门表ID")
    private Long deptId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /* ------------------ 非数据库数据分割线 ------------------ */

}
