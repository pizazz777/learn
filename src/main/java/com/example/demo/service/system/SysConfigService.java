package com.example.demo.service.system;

import com.example.demo.component.response.ResResult;
import com.example.demo.entity.system.SysConfigDO;
import com.example.demo.service.BaseService;
import com.huang.exception.ServiceException;

import java.util.Map;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/05/18
 * @description: 类描述: 系统配置 Service
 **/
public interface SysConfigService extends BaseService<SysConfigDO> {

    /**
     * 获取系统全部的配置
     *
     * @return r
     * @throws ServiceException e
     */
    ResResult get() throws ServiceException;

    /**
     * 获取配置
     *
     * @param key 键
     * @return r
     * @throws ServiceException e
     */
    ResResult getByKey(String key) throws ServiceException;

    /**
     * 更改配置
     *
     * @param configMap 配置信息
     * @return r
     * @throws ServiceException e
     */
    ResResult updateConfig(Map<String, Object> configMap) throws ServiceException;

}
