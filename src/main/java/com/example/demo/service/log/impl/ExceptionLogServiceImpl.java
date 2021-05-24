package com.example.demo.service.log.impl;

import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResList;
import com.example.demo.component.response.ResResult;
import com.example.demo.dao.log.ExceptionLogDao;
import com.example.demo.entity.log.ExceptionLogDO;
import com.example.demo.service.log.ExceptionLogService;
import com.github.pagehelper.PageHelper;
import com.huang.exception.ServiceException;
import com.huang.util.container.ContainerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/05/15
 * @description: 类描述: 异常日志 Service
 **/
@Service
public class ExceptionLogServiceImpl implements ExceptionLogService {

    private ExceptionLogDao exceptionLogDao;

    @Autowired
    public ExceptionLogServiceImpl(ExceptionLogDao exceptionLogDao) {
        this.exceptionLogDao = exceptionLogDao;
    }

    @Override
    public ResResult list(ExceptionLogDO query) throws ServiceException {
        PageHelper.startPage(query);
        List<ExceptionLogDO> list = exceptionLogDao.list(query);
        if (ContainerUtil.isNotEmpty(list)) {
            return ResResult.success(ResList.page(list));
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }

}
