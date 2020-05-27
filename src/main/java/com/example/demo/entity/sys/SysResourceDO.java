package com.example.demo.entity.sys;

import lombok.*;
import com.example.demo.entity.PageBean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 资源 Model
 **/
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysResourceDO extends PageBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * 创建时间
    */
    private LocalDateTime createTime;
    /**
    * 资源描述
    */
    private String description;
    /**
    * 资源图标
    */
    private String icon;
    /**
    * 主键
    */
    private Long id;
    /**
    * 资源名称
    */
    private String name;
    /**
    * 资源字符串
    */
    private String permission;
    /**
    * 父级资源主键
    */
    private Long pid;
    /**
    * 前端路由
    */
    private String router;
    /**
    * 资源状态
    */
    private Integer status;
    /**
    * 系统资源类型
    */
    private Integer sysType;
    /**
    * 资源标题
    */
    private String title;
    /**
    * 资源类型
    */
    private Integer type;
    /**
    * 修改时间
    */
    private LocalDateTime updateTime;

    /* ------------------ 非数据库数据分割线 ------------------ */

    private List<SysResourceDO> childList;

}
