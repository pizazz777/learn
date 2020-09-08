package com.example.demo.dao.sys;

import com.example.demo.dao.BaseDao;
import com.example.demo.entity.sys.SysUserDeptMidDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author hxx
 * @version 1.0
 * @date 2020/08/20
 * @description: 类描述: 系统用户和部门关联 Dao
 **/
@Mapper
@Repository
public interface SysUserDeptMidDao extends BaseDao<SysUserDeptMidDO> {


    /**
     * 获取用户和部门的关联
     *
     * @param userId 用户id
     * @param deptId 部门id
     * @return list
     */
    List<SysUserDeptMidDO> listByUserIdAndDeptId(@Param("userId") Long userId, @Param("deptId") Long deptId);


    /**
     * 删除用户和部门的关联
     *
     * @param userId 用户id
     * @param deptId 部门id
     * @return int
     */
    int deleteByUserIdAndDeptId(@Param("userId") Long userId, @Param("deptId") Long deptId);

}
