package com.example.demo.manager.log.impl;

import com.example.demo.dao.log.ApiLogDao;
import com.example.demo.entity.log.ApiLogDO;
import com.example.demo.manager.log.ApiLogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @date 2020-05-15 15:44
 */
@Service
public class ApiLogRequestImpl implements ApiLogRequest {

    private ApiLogDao apiLogDao;

    @Autowired
    public ApiLogRequestImpl(ApiLogDao apiLogDao) {
        this.apiLogDao = apiLogDao;
    }

    /**
     * 保存
     *
     * @param apiLog p
     */
    @Override
    public Integer save(ApiLogDO apiLog) {
        return apiLogDao.save(apiLog);
    }
}
