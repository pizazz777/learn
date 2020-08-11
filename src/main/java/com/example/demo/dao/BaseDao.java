package com.example.demo.dao;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * @author administrator
 * @date 2020/04/30
 * @description: 类描述: dao基本类
 **/
public interface BaseDao<T extends Serializable> {

    List<T> list(@Param("query") T query);

    int count(@Param("query") T query);

    T getById(@Param("id") Serializable id);

    boolean isExistById(@Param("id") Serializable id);

    int save(T object);

    int update(T object);

    int deleteById(@Param("id") Serializable id);

    int deleteByIds(@Param("ids") Serializable[] ids);
}
