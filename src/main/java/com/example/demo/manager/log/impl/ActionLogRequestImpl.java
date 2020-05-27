package com.example.demo.manager.log.impl;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.dao.log.ActionLogDao;
import com.example.demo.entity.log.ActionLogDO;
import com.example.demo.manager.log.ActionLogRequest;
import com.example.demo.properties.LogProperties;
import com.example.demo.util.ip.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Administrator
 * @date 2020-05-15 14:05
 */
@Service
@Slf4j
public class ActionLogRequestImpl implements ActionLogRequest {

    private ActionLogDao actionLogDao;
    private LogProperties logProperties;
    private AuthComponent authComponent;
    private HttpServletRequest request;

    @Autowired
    public ActionLogRequestImpl(ActionLogDao actionLogDao,
                                LogProperties logProperties,
                                AuthComponent authComponent,
                                HttpServletRequest request) {
        this.actionLogDao = actionLogDao;
        this.logProperties = logProperties;
        this.authComponent = authComponent;
        this.request = request;
    }


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
    @Override
    public void saveActionLog(String type, String desc, String param, LocalDateTime startTime, LocalDateTime endTime) throws ServiceException {
        Duration duration = Duration.between(startTime, endTime);
        ActionLogDO actionLog = ActionLogDO.builder()
                .type(type)
                .identify(String.valueOf(authComponent.getPrimaryPrincipal(Long.class)))
                .description(desc)
                .uri(request.getRequestURI())
                .param(param)
                .duration(duration.toMillis())
                .ip(IpUtil.getIp(request))
                .visitTime(startTime)
                .createTime(endTime)
                .build();
        if (logProperties.getWriteToDatabase()) {
            actionLogDao.save(actionLog);
        } else {
            log.debug(actionLog.toString());
        }
    }
}
