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
 * @description: 类描述: 权限资源 Model
 **/
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysPermissionResourceMidDO extends PageBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("权限主键")
    private Long permissionId;

    @ApiModelProperty("资源主键")
    private Long resourceId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /* ------------------ 非数据库数据分割线 ------------------ */

}
