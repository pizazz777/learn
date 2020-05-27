package com.example.demo.entity.activiti;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author jlf
 * @version 1.0
 * @date 2019/09/18
 * @description: 类描述: 流程信息类（已完成节点ID集合，进行中节点ID集合等）
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityInfoDTO {

    /**
     * 已经完成节点ID集合
     */
    private List<String> finishFlowElementIdList;
    /**
     * 进行中节点ID集合
     */
    private List<String> underwayFlowElementIdList;
    /**
     * 进行中节点名称集合
     */
    private List<String> underwayFlowElementNameList;
    /**
     * 开始节点时间
     */
    private List<LocalDateTime> startTimeList;

}
