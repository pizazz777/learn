package com.example.demo.dao.sys;

import com.example.demo.dao.BaseDao;
import com.example.demo.entity.sys.SysUserRoleMidDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;


/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 用户角色 Dao
 **/
@Mapper
@Repository
public interface SysUserRoleMidDao extends BaseDao<SysUserRoleMidDO> {


    /**
     * 判断角色是否有关联用户
     *
     * @param roleId 角色id
     * @return boolean
     */
    Boolean isExistsByRoleId(@Param("roleId") Serializable roleId);


    /**
     * 根据用户id和角色id删除
     *
     * @param userId 用户id
     * @param roleId 角色id
     * @return r
     */
    int deleteByUserIdAndRoleId(@Param("userId") Serializable userId, @Param("roleId") Serializable roleId);


}
