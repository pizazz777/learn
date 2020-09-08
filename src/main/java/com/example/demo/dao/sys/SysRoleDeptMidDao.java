package com.example.demo.dao.sys;

import com.example.demo.dao.BaseDao;
import com.example.demo.entity.sys.SysRoleDeptMidDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;


/**
 * @author hxx
 * @version 1.0.0
 * @date 2020/08/24
 * @description 系统角色和部门关联表
 **/
@Mapper
@Repository
public interface SysRoleDeptMidDao extends BaseDao<SysRoleDeptMidDO> {


    /**
     * 根据角色id和部门id获取对应的数据
     *
     * @param roleId 角色id
     * @param deptId 部门id
     * @return list
     */
    List<SysRoleDeptMidDO> listByRoleIdAndDeptId(@Param("roleId") Long roleId, @Param("deptId") Long deptId);


    /**
     * 根据角色id和部门id删除对应的数据
     *
     * @param roleId 角色id
     * @param deptId 部门id
     * @return int
     */
    int deleteByRoleIdAndDeptId(@Param("roleId") Serializable roleId, @Param("deptId") Long deptId);

}
