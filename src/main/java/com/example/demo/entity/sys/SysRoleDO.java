package com.example.demo.entity.sys;

import lombok.*;
import com.example.demo.entity.PageBean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 角色 Model
 **/
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysRoleDO extends PageBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * 创建时间
    */
    private LocalDateTime createTime;
    /**
    * 主键
    */
    private Long id;
    /**
    * 角色名称
    */
    private String name;
    /**
    * 备注
    */
    private String remark;
    /**
    * 角色状态
    */
    private Integer status;
    /**
    * 角色标题
    */
    private String title;
    /**
    * 角色类型
    */
    private Integer type;
    /**
    * 修改时间
    */
    private LocalDateTime updateTime;

    /* ------------------ 非数据库数据分割线 ------------------ */

    /**
     * 关联的权限集合
     */
    private Set<Long> permissionIdSet;

}
