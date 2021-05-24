package com.example.demo.service.customer.impl;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.response.DelResInfo;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResList;
import com.example.demo.component.response.ResResult;
import com.example.demo.dao.customer.CustomerDao;
import com.example.demo.entity.customer.CustomerDO;
import com.example.demo.service.customer.CustomerService;
import com.github.pagehelper.PageHelper;
import com.huang.exception.ServiceException;
import com.huang.util.container.ContainerUtil;
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
 * @date 2020/04/28
 * @description: 类描述: 客户 Service
 **/
@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao;
    private AuthComponent authComponent;

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao, AuthComponent authComponent) {
        this.customerDao = customerDao;
        this.authComponent = authComponent;
    }

    @Override
    public ResResult list(CustomerDO query) throws ServiceException {
        PageHelper.startPage(query);
        List<CustomerDO> list = customerDao.list(query);
        if (ContainerUtil.isNotEmpty(list)) {
            return ResResult.success(ResList.page(list));
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }

    @Override
    public ResResult getById(Serializable id) {
        CustomerDO object = customerDao.getById(id);
        if (Objects.nonNull(object)) {
            return ResResult.success(object);
        }
        return ResResult.fail(ResCode.NOT_FOUND);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult save(CustomerDO object) throws ServiceException {
        LocalDateTime now = LocalDateTime.now();
        object.setCreateTime(now);
        object.setUpdateTime(now);
        object.setCreateUserId(authComponent.getPrimaryPrincipal(Long.class));
        int save = customerDao.save(object);
        if (save > 0) {
            return ResResult.success();
        }
        return ResResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult update(CustomerDO object) throws ServiceException {
        object.setUpdateTime(LocalDateTime.now());
        int update = customerDao.update(object);
        if (update > 0) {
            return ResResult.success(object);
        }
        return ResResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResResult deleteByIds(Serializable[] ids) throws ServiceException {
        DelResInfo delResInfo = new DelResInfo();
        for (Serializable id : ids) {
            if (customerDao.deleteById(id) > 0) {
                // 不存在且已删除则将 id 添加到已删除列表
                delResInfo.addDeleted(id);
            } else {
                delResInfo.addNotDelete(id);
            }
        }
        if (ContainerUtil.isNotEmpty(delResInfo.getNotDelete())) {
            return ResResult.response(ResCode.OK, "有客户对象删除失败", delResInfo);
        }
        return ResResult.success(delResInfo);
    }
}
