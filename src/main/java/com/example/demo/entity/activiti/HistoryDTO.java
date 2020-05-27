package com.example.demo.entity.activiti;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class HistoryDTO implements Serializable {

    private static final long serialVersionUID = -7952902053203666958L;

    /**
     * 活动的id
     */
    private String actId;

    /**
     * 活动的名称
     */
    private String name;
    /**
     * 活动的时间
     */
    private LocalDateTime actDateTime;
    /**
     * 当前待办的id
     */
    private String assigneeTaskId;

    /**
     * 该任务的下一个任务列表/有分支的情况
     */
    private List<HistoryDTO> next;

}
