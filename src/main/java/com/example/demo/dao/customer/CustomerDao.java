package com.example.demo.dao.customer;

import com.example.demo.entity.customer.CustomerDO;
import com.example.demo.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/28
 * @description: 类描述: 客户 Dao
 **/
@Mapper
@Repository
public interface CustomerDao extends BaseDao<CustomerDO> {

}
