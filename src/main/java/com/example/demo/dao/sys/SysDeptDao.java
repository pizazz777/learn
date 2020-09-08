package com.example.demo.dao.sys;

import com.example.demo.dao.BaseDao;
import com.example.demo.entity.sys.SysDeptDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;


/**
 * @author hxx
 * @version 1.0.0
 * @date 2020/08/24
 * @description 系统部门表
 **/
@Mapper
@Repository
public interface SysDeptDao extends BaseDao<SysDeptDO> {


    /**
     * 根据pid和名称判重
     *
     * @param pid  父id
     * @param name 部门名称
     * @return boolean
     */
    boolean isExistsByPidAndName(@Param("pid") Serializable pid, @Param("name") String name);


    /**
     * 获取全部部门id
     *
     * @param status 状态
     * @return list
     */
    List<Long> listAllId(@Param("status") Integer status);


    /**
     * 根据pid和状态获取
     *
     * @param pid    父id
     * @param status 状态
     * @return list
     */
    List<SysDeptDO> listByPit(@Param("pid") Long pid, @Param("status") Integer status);

}
