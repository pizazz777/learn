package com.example.demo.dao.sys;

import com.example.demo.dao.BaseDao;
import com.example.demo.entity.sys.SysPermissionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;


/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 权限 Dao
 **/
@Mapper
@Repository
public interface SysPermissionDao extends BaseDao<SysPermissionDO> {

    /**
     * 获取权限字符串
     *
     * @param userId 用户主键
     * @return list
     */
    List<String> listPermissionStringByUserId(@Param("userId") Serializable userId);


    /**
     * 根据父id和状态获取
     *
     * @param pid    父级id
     * @param status 状态
     * @return list
     */
    List<SysPermissionDO> listByPidAndStatus(@Param("pid") Long pid, @Param("status") Integer status);


    /**
     * 根据pid更新pTitle
     *
     * @param pTitle 父标题
     * @param pid    父id
     * @return r
     */
    int updatePTitleByPid(@Param("pTitle") String pTitle, @Param("pid") Long pid);


    /**
     * 根据pid判断是否存在
     *
     * @param pid 父id
     * @return boolean
     */
    Boolean isExistsByPid(@Param("pid") Serializable pid);
}
