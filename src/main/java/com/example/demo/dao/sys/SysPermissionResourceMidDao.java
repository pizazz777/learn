package com.example.demo.dao.sys;

import com.example.demo.dao.BaseDao;
import com.example.demo.entity.sys.SysPermissionResourceMidDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;


/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 权限资源 Dao
 **/
@Mapper
@Repository
public interface SysPermissionResourceMidDao extends BaseDao<SysPermissionResourceMidDO> {


    /**
     * 根据权限id和资源id删除
     *
     * @param permissionId 权限id
     * @param resourceId   资源id
     * @return r
     */
    int deleteByPermissionIdAndResourceId(@Param("permissionId") Serializable permissionId, @Param("resourceId") Serializable resourceId);


    /**
     * 根据权限id和资源id获取
     *
     * @param permissionId 权限id
     * @param resourceId   资源id
     * @return list
     */
    List<SysPermissionResourceMidDO> listByPermissionIdAndResourceId(@Param("permissionId") Long permissionId, @Param("resourceId") Long resourceId);


    /**
     * 根据是否有权限在使用此资源id
     *
     * @param resourceId 资源id
     * @return r
     */
    boolean isExistByResourceId(@Param("resourceId") Serializable resourceId);

}
