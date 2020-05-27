package com.example.demo.service.log.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.DelResInfo;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResList;
import com.example.demo.component.response.ResResult;
import com.example.demo.entity.log.ApiLogDO;
import com.example.demo.dao.log.ApiLogDao;
import com.example.demo.service.log.ApiLogService;
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
            ResList<ApiLogDO> resList = ResList.page(list, ((Page) list).getTotal());
            return ResResult.success(resList);
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }

}
