package com.example.demo.service;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResResult;

import java.io.Serializable;

/**
 * @author sqm
 * @version 1.0
 * @date 2019/01/30
 * @description: 类描述: service基本类
 **/
public interface BaseService<T extends Serializable> {

    default ResResult list(T query) throws ServiceException {
        return ResResult.fail();
    }

    default ResResult count(T query) throws ServiceException {
        return ResResult.fail();
    }

    default ResResult getById(Serializable id) throws ServiceException {
        return ResResult.fail();
    }

    default ResResult isExistById(Serializable id) throws ServiceException {
        return ResResult.fail();
    }

    default ResResult save(T object) throws ServiceException {
        return ResResult.fail();
    }

    default ResResult update(T object) throws ServiceException {
        return ResResult.fail();
    }

    default ResResult deleteById(Serializable id) throws ServiceException {
        return ResResult.fail();
    }

    default ResResult deleteByIds(Serializable[] ids) throws ServiceException {
        return ResResult.fail();
    }

}
