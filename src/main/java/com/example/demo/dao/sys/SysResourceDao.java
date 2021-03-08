package com.example.demo.dao.sys;

import com.example.demo.dao.BaseDao;
import com.example.demo.entity.sys.SysResourceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;


/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 资源 Dao
 **/
@Mapper
@Repository
public interface SysResourceDao extends BaseDao<SysResourceDO> {

    /**
     * 判断此用户id的资源权限是否存在
     *
     * @param id         用户id
     * @param permission 权限资源字符串
     * @return boolean
     */
    boolean isExistByUserIdAndPermission(@Param("id") Serializable id, @Param("permission") String permission);

    /**
     * 获取权限字符串
     *
     * @param userId 用户主键
     * @return list
     */
    List<String> listResourcePermissionStringByUserId(@Param("userId") Serializable userId);


    /**
     * 根据权限id和状态获取对应的资源
     *
     * @param permissionId 权限id
     * @param status       状态
     * @return list
     */
    List<SysResourceDO> listByPermissionAndStatus(@Param("permissionId") Long permissionId, @Param("status") Integer status);


    /**
     * 根据父id和状态获取
     *
     * @param pid    父id
     * @param status 状态
     * @return list
     */
    List<SysResourceDO> listByPidAndStatus(@Param("pid") Long pid, @Param("status") Integer status);


    /**
     * 获取用户允许的资源
     *
     * @param userId 用户id
     * @return list
     */
    List<Long> listByUserId(@Param("userId") Long userId);


    /**
     * 获取全部的资源符
     *
     * @return string
     */
    List<String> listAllResource();

}
