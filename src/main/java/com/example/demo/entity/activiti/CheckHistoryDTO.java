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
public class CheckHistoryDTO implements Serializable {

    private static final long serialVersionUID = 9092826451356492897L;

    /**
     * 流程的树级结构
     */
    private HistoryDTO historyProcess;
    /**
     * 活动的办理人和批注
     */
    private List<ActHistoryDTO> actHistoryDtoList;
}
