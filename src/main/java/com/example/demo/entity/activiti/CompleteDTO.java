package com.example.demo.entity.activiti;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @version 1.1.0
 * @date 2020/05/08
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompleteDTO implements Serializable {

    private static final long serialVersionUID = -3020266463317754298L;

    /**
     * 当前需要完成任务的id
     */
    private String taskId;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 下一级任务的代办人,用户id集合
     */
    private List<String> candidateUserList;
    /**
     * 分支的值,适用于网关/多条分支
     */
    private Integer branch;
    /**
     * 审核的值
     */
    private Integer check;
    /**
     * 审核批注
     */
    private String comment;
    /**
     * 当前任务的办理人
     */
    private String assignee;

}
