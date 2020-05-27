package com.example.demo.dao.log;

import com.example.demo.entity.log.ApiLogDO;
import com.example.demo.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * @author hxx
 * @version 1.0
 * @date 2020/05/15
 * @description: 类描述: 远程api日志 Dao
 **/
@Mapper
@Repository
public interface ApiLogDao extends BaseDao<ApiLogDO> {

}
