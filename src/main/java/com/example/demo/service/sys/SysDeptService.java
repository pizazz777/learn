package com.example.demo.service.sys;

import com.example.demo.component.response.ResResult;
import com.example.demo.entity.sys.SysDeptDO;
import com.example.demo.service.BaseService;
import com.huang.exception.ServiceException;

/**
 * @author hxx
 * @version 1.0.0
 * @date 2020/08/24
 * @description 系统部门表
 **/
public interface SysDeptService extends BaseService<SysDeptDO> {


    /**
     * 获取单位树
     *
     * @param pid  父id
     * @param name 部门名称
     * @return r
     * @throws ServiceException e
     */
    ResResult tree(Long pid, String name) throws ServiceException;

}
