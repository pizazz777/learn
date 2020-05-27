package com.example.demo.entity.activiti;

import lombok.Builder;
import lombok.Data;
import org.activiti.engine.task.Comment;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Administrator
 * @version 1.1.0
 * @date 2020/05/08
 * @description: 历史信息
 */
@Data
@Builder
public class HistoryDO implements Serializable {

    private static final long serialVersionUID = 5131671620392530355L;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 活动id
     */
    private String actId;
    /**
     * 活动名称
     */
    private String actName;
    /**
     * 活动类型
     */
    private String actType;
    /**
     * 办理人id
     */
    private String assignee;
    /**
     * 办理人姓名
     */
    private String assigneeName;
    /**
     * 任务开始时间
     */
    private LocalDateTime startTime;
    /**
     * 任务结束时间
     */
    private LocalDateTime endTime;
    /**
     * 批注
     */
    private List<Comment> commentList;
    /**
     * 调用的子流程的流程实例ID
     */
    private String childProcessInstanceId;
}
