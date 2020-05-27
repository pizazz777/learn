package com.example.demo.service.log.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.DelResInfo;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResList;
import com.example.demo.component.response.ResResult;
import com.example.demo.entity.log.ExceptionLogDO;
import com.example.demo.dao.log.ExceptionLogDao;
import com.example.demo.service.log.ExceptionLogService;
import com.example.demo.util.container.ContainerUtil;
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
            long total = ((Page) list).getTotal();
            ResList<ExceptionLogDO> resList = ResList.page(list, total);
            return ResResult.success(resList);
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }

}
