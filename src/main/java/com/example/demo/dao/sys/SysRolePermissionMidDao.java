package com.example.demo.dao.sys;

import com.example.demo.dao.BaseDao;
import com.example.demo.entity.sys.SysRolePermissionMidDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;


/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 角色权限 Dao
 **/
@Mapper
@Repository
public interface SysRolePermissionMidDao extends BaseDao<SysRolePermissionMidDO> {


    /**
     * 根据角色id和权限id获取对应数据
     *
     * @param roleId       角色id
     * @param permissionId 权限id
     * @return list
     */
    List<SysRolePermissionMidDO> listByRoleIdAndPermissionId(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);


    /**
     * 根据角色id和权限id删除对应的数据
     *
     * @param roleId       角色id
     * @param permissionId 权限id
     * @return int
     */
    int deleteByRoleIdAndPermissionId(@Param("roleId") Serializable roleId, @Param("permissionId") Serializable permissionId);


    /**
     * 根据用户id和角色状态获取关联的权限
     *
     * @param userId 用户id
     * @param status 状态
     * @return list
     */
    List<Long> listPermissionIdByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);


    /**
     * 通过 permissionId 判断是否存在
     *
     * @param permissionId 权限id
     * @return boolean
     */
    boolean isExistByPermissionId(@Param("permissionId") Serializable permissionId);

}
