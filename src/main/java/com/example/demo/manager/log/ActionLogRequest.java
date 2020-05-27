package com.example.demo.manager.log;

import com.example.demo.component.exception.ServiceException;

import java.time.LocalDateTime;

/**
 * @author Administrator
 * @date 2020-05-15 14:05
 */
public interface ActionLogRequest {

    /**
     * 保存操作日志
     *
     * @param type      操作类型
     * @param desc      描述
     * @param param     参数
     * @param startTime 操作开始时间
     * @param endTime   操作结束时间
     * @throws ServiceException e
     */
    void saveActionLog(String type, String desc, String param, LocalDateTime startTime, LocalDateTime endTime) throws ServiceException;

}
