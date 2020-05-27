package com.example.demo.dao.system;

import com.example.demo.dao.BaseDao;
import com.example.demo.entity.system.SysConfigDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * @author hxx
 * @version 1.0
 * @date 2020/05/18
 * @description: 类描述: 系统配置 Dao
 **/
@Mapper
@Repository
public interface SysConfigDao extends BaseDao<SysConfigDO> {

    /**
     * 通过键获取值
     *
     * @param configKey 键
     * @return 值
     */
    SysConfigDO getByKey(@Param("configKey") String configKey);

    /**
     * 判断是否存在
     *
     * @param configKey 键
     * @return boolean
     */
    Boolean isExistByKey(@Param("configKey") String configKey);

}
