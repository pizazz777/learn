package com.example.demo.manager;


import com.example.demo.component.exception.ServiceException;

import java.io.Serializable;
import java.util.List;

/**
 * @author hxx
 * @version 1.0.0
 * @date 2020/05/15
 * @description: 类描述: manager基本类
 **/
public interface BaseRequest<T extends Serializable> {

    default List<T> list(T query) throws ServiceException {
        return null;
    }

    default Integer count(T query) throws ServiceException {
        return 0;
    }

    default T getById(Serializable id) throws ServiceException {
        return null;
    }

    default Boolean isExistById(Serializable id) throws ServiceException {
        return false;
    }

    default Integer save(T object) throws ServiceException {
        return 0;
    }

    default Integer update(T object) throws ServiceException {
        return 0;
    }

    default Integer deleteById(Serializable id) throws ServiceException {
        return 0;
    }

    default Integer deleteByIds(Serializable[] ids) throws ServiceException {
        return 0;
    }

}
