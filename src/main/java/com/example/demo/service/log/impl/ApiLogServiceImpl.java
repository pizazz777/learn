package com.example.demo.service.log.impl;

import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResList;
import com.example.demo.component.response.ResResult;
import com.example.demo.dao.log.ApiLogDao;
import com.example.demo.entity.log.ApiLogDO;
import com.example.demo.service.log.ApiLogService;
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
 * @description: 类描述: 远程api日志 Service
 **/
@Service
public class ApiLogServiceImpl implements ApiLogService {

    private ApiLogDao apiLogDao;

    @Autowired
    public ApiLogServiceImpl(ApiLogDao apiLogDao) {
        this.apiLogDao = apiLogDao;
    }

    @Override
    public ResResult list(ApiLogDO query) throws ServiceException {
        PageHelper.startPage(query);
        List<ApiLogDO> list = apiLogDao.list(query);
        if (ContainerUtil.isNotEmpty(list)) {
            return ResResult.success(ResList.page(list));
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }

}
