package com.example.demo.entity.sys;

import lombok.Builder;
import lombok.Data;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/03/08
 * @description
 */
@Data
@Builder
public class PermissionGenerateInfo {

    /**
     * 权限资源符
     */
    private String permission;

    /**
     * 资源符描述
     */
    private String description;

    /**
     * 是否是系统资源类型(以sys开头) 1.是,0.否
     */
    private Integer sysType;

}
