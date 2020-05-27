package com.example.demo.entity.log;

import com.example.demo.entity.PageBean;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/05/15
 * @description: 类描述: 异常日志 Model
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionLogDO extends PageBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    /**
     * 身份
     */
    private String identify;
    /**
     * ip
     */
    private String ip;
    /**
     * 接口地址
     */
    private String uri;
    /**
     * 请求参数
     */
    private String param;
    /**
     * 异常类名
     */
    private String className;
    /**
     * 异常信息
     */
    private String message;
    /**
     * 堆栈追溯信息
     */
    private String stackTrace;
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
