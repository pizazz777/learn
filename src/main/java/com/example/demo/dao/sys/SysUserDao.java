package com.example.demo.dao.sys;

import com.example.demo.dao.BaseDao;
import com.example.demo.entity.sys.SysUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/28
 * @description: 类描述: 系统用户 Dao
 **/
@Mapper
@Repository
public interface SysUserDao extends BaseDao<SysUserDO> {

    /**
     * 登录
     *
     * @param username 用户名
     * @return r
     */
    SysUserDO login(@Param("username") String username);

    /**
     * 根据唯一字段获取(会返回密码)
     *
     * @param id      主键
     * @param account 账号
     * @param email   邮箱
     * @param mobile  手机号码
     * @return 用户
     */
    SysUserDO getByUniqueWithSecret(@Param("id") Long id, @Param("account") String account, @Param("email") String email, @Param("mobile") String mobile);

    /**
     * 根据唯一字段获取
     *
     * @param id      主键
     * @param account 账号
     * @param email   邮箱
     * @param mobile  手机号码
     * @return 用户
     */
    SysUserDO getByUnique(@Param("id") Long id, @Param("account") String account, @Param("email") String email, @Param("mobile") String mobile);

}
