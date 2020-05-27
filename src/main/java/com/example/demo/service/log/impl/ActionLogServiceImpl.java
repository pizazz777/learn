package com.example.demo.service.log.impl;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.DelResInfo;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResList;
import com.example.demo.component.response.ResResult;
import com.example.demo.dao.log.ActionLogDao;
import com.example.demo.entity.log.ActionLogDO;
import com.example.demo.entity.sys.SysUserDO;
import com.example.demo.manager.sys.SysUserRequest;
import com.example.demo.properties.LogProperties;
import com.example.demo.service.log.ActionLogService;
import com.example.demo.util.container.ContainerUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/05/15
 * @description: 类描述: 操作日志 Service
 **/
@Service
public class ActionLogServiceImpl implements ActionLogService {

    private ActionLogDao actionLogDao;
    private LogProperties logProperties;
    private SysUserRequest sysUserRequest;

    @Autowired
    public ActionLogServiceImpl(ActionLogDao actionLogDao, LogProperties logProperties, SysUserRequest sysUserRequest) {
        this.actionLogDao = actionLogDao;
        this.logProperties = logProperties;
        this.sysUserRequest = sysUserRequest;
    }

    @Override
    public ResResult list(ActionLogDO query) throws ServiceException {
        PageHelper.startPage(query);
        List<ActionLogDO> list = actionLogDao.list(query);
        if (ContainerUtil.isNotEmpty(list)) {
            if (logProperties.getShowUserInfo()) {
                for (ActionLogDO actionLog : list) {
                    Long id = Long.valueOf(actionLog.getIdentify());
                    if (Objects.equals(id, 0L)) {
                        actionLog.setUsername("管理员");
                    } else {
                        SysUserDO sysUser = sysUserRequest.getById(id);
                        actionLog.putUserInfo(sysUser);
                    }
                }
            }
            ResList<ActionLogDO> resList = ResList.page(list, ((Page) list).getTotal());
            return ResResult.success(resList);
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }

}
