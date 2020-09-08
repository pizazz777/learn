package com.example.demo.dao.sys;

import com.example.demo.dao.BaseDao;
import com.example.demo.entity.sys.SysRoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author hxx
 * @version 1.0.0
 * @date 2020/08/24
 * @description 系统角色表
 **/
@Mapper
@Repository
public interface SysRoleDao extends BaseDao<SysRoleDO> {

    /**
     * 获取用户的角色名称
     *
     * @param id 用户id
     * @return list
     */
    List<String> listRoleNameByUserId(Long id);


    /**
     * 判断角色名称是否存在
     *
     * @param name 角色名称
     * @return boolean
     */
    Boolean isExistsByName(@Param("name") String name);


    /**
     * 根据用户id和资源权限符获取角色
     *
     * @param userId     用户id
     * @param permission 资源权限符
     * @param status     状态
     * @return list
     */
    List<SysRoleDO> listByUserIdAndPermission(@Param("userId") Long userId, @Param("permission") String permission, @Param("status") Integer status);
}
