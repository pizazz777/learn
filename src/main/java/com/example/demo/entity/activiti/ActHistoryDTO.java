package com.example.demo.entity.activiti;

import lombok.*;
import org.activiti.engine.task.Comment;

import java.io.Serializable;
import java.time.LocalDateTime;
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
public class ActHistoryDTO implements Serializable {

    private static final long serialVersionUID = 2210262150478665773L;

    /**
     * 活动的id
     */
    private String actId;
    /**
     * 活动的名称
     */
    private String actName;
    /**
     * 该活动的办理人列表
     */
    private List<Assignee> assigneeList;

    /**
     * 该活动第一次办理时间
     */
    private LocalDateTime assigneeTime;

    @Data
    @Builder
    public static class Assignee {
        /**
         * 办理人id
         */
        private String assigneeId;
        /**
         * 办理人姓名
         */
        private String assigneeName;
        /**
         * 办理人所在单位
         */
        private String assigneeCorpName;
        /**
         * 意见
         */
        private String comment;
        /**
         * 审核
         */
        private String check;
        /**
         * 办理时间
         */
        private LocalDateTime assigneeTime;
    }
}
