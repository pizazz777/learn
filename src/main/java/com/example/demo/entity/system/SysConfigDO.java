package com.example.demo.entity.system;

import lombok.*;
import com.example.demo.entity.PageBean;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/05/18
 * @description: 类描述: 系统配置 Model
 **/
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysConfigDO extends PageBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * 键
    */
    private String configKey;
    /**
    * 值
    */
    private Object configValue;
    /**
    * 创建时间
    */
    private LocalDateTime createTime;
    /**
    * 修改时间
    */
    private LocalDateTime updateTime;

    /* ------------------ 非数据库数据分割线 ------------------ */

}
