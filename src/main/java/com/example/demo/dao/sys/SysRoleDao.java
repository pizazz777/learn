package com.example.demo.dao.sys;

import com.example.demo.dao.BaseDao;
import com.example.demo.entity.sys.SysRoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;


/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 角色 Dao
 **/
@Mapper
@Repository
public interface SysRoleDao extends BaseDao<SysRoleDO> {

    /**
     * 获取指定用户的角色名称
     *
     * @param userId 用户主键
     * @return list
     */
    List<String> listRoleNameByUserId(@Param("userId") Serializable userId);

}
