package com.example.demo.entity.sys;

import lombok.*;
import com.example.demo.entity.PageBean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 权限 Model
 **/
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysPermissionDO extends PageBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * 创建时间
    */
    private LocalDateTime createTime;
    /**
    * 权限描述
    */
    private String description;
    /**
    * 权限图标
    */
    private String icon;
    /**
    * 主键
    */
    private Long id;
    /**
    * 权限名称
    */
    private String name;
    /**
    * 父级权限标题
    */
    private String pTitle;
    /**
    * 父级权限主键
    */
    private Long pid;
    /**
    * 权限排序
    */
    private Integer seq;
    /**
    * 权限状态
    */
    private Integer status;
    /**
    * 权限标题
    */
    private String title;
    /**
    * 权限类型
    */
    private Integer type;
    /**
    * 修改时间
    */
    private LocalDateTime updateTime;
    /**
    * 权限访问路径
    */
    private String url;

    /* ------------------ 非数据库数据分割线 ------------------ */

    private List<SysPermissionDO> childList;

    private Set<Long> resourceIdSet;

    private List<SysResourceDO> resourceList;

}
