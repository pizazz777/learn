package com.example.demo.service.sys.impl;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.DelResInfo;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResList;
import com.example.demo.component.response.ResResult;
import com.example.demo.dao.sys.SysResourceDao;
import com.example.demo.entity.sys.SysResourceDO;
import com.example.demo.service.sys.SysResourceService;
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
 * @date 2020/04/29
 * @description: 类描述: 资源 Service
 **/
@Service
public class SysResourceServiceImpl implements SysResourceService {

    private SysResourceDao sysResourceDao;

    @Autowired
    public SysResourceServiceImpl(SysResourceDao sysResourceDao) {
        this.sysResourceDao = sysResourceDao;
    }

    @Override
    public ResResult list(SysResourceDO query) throws ServiceException {
        PageHelper.startPage(query);
        List<SysResourceDO> list = sysResourceDao.list(query);
        if (ContainerUtil.isNotEmpty(list)) {
            long total = ((Page) list).getTotal();
            ResList<SysResourceDO> resList = ResList.page(list, total);
            return ResResult.success(resList);
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }

    @Override
    public ResResult getById(Serializable id) {
        SysResourceDO object = sysResourceDao.getById(id);
        if (Objects.nonNull(object)) {
            return ResResult.success(object);
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult save(SysResourceDO object) throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        object.setCreateTime(now);
        object.setUpdateTime(now);
        int save = sysResourceDao.save(object);
        if (save > 0) {
            return ResResult.success();
        }
        return ResResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult update(SysResourceDO object) throws ServiceException {
        object.setUpdateTime(LocalDateTime.now());
        int update = sysResourceDao.update(object);
        if (update > 0) {
            return ResResult.success();
        }
        return ResResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult deleteByIds(Serializable[] ids) throws ServiceException {
        DelResInfo delResInfo = new DelResInfo();
        for (Serializable id : ids) {
            if (sysResourceDao.deleteById(id) > 0) {
                // 不存在且已删除则将 id 添加到已删除列表
                delResInfo.addDeleted(id);
            } else {
                delResInfo.addNotDelete(id);
            }
        }
        if (ContainerUtil.isNotEmpty(delResInfo.getNotDelete())) {
            return ResResult.response(ResCode.OK, "有资源表对象删除失败", delResInfo);
        }
        return ResResult.success(delResInfo);
    }
}
