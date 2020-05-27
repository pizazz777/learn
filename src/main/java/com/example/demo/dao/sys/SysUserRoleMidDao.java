package com.example.demo.dao.sys;

import com.example.demo.entity.sys.SysUserRoleMidDO;
import com.example.demo.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/29
 * @description: 类描述: 用户角色 Dao
 **/
@Mapper
@Repository
public interface SysUserRoleMidDao extends BaseDao<SysUserRoleMidDO> {

}
