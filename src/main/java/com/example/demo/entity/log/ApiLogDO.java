package com.example.demo.entity.log;

import lombok.*;
import com.example.demo.entity.PageBean;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/05/15
 * @description: 类描述: 远程api日志 Model
 **/
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiLogDO extends PageBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private Long id;
    /**
    * API地址
    */
    private String url;
    /**
    * 请求方式
    */
    private String method;
    /**
    * 请求参数
    */
    private String param;
    /**
    * 响应内容
    */
    private String response;
    /**
    * API描述
    */
    private String description;
    /**
    * 创建时间
    */
    private LocalDateTime createTime;

    /* ------------------ 非数据库数据分割线 ------------------ */

    /* ------------------ 查询使用 ------------------ */
    /**
     * 查询-开始时间
     */
    private LocalDateTime startTime;
    /**
     * 查询-结束时间
     */
    private LocalDateTime endTime;

}
