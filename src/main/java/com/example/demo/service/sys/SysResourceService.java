package com.example.demo.service.sys;

import com.example.demo.component.response.ResResult;
import com.example.demo.entity.sys.SysResourceDO;
import com.example.demo.service.BaseService;
import com.huang.exception.ServiceException;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 资源 Service
 **/
public interface SysResourceService extends BaseService<SysResourceDO> {


    /**
     * 获取资源树
     *
     * @param pid 父id
     * @return r
     * @throws ServiceException e
     */
    ResResult tree(Long pid) throws ServiceException;

}
