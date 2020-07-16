package com.example.demo.dao.upload;

import com.example.demo.entity.upload.UploadFileDO;
import com.example.demo.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * @author hxx
 * @version 1.0
 * @date 2020/07/16
 * @description: 类描述: 文件上传表 Dao
 **/
@Mapper
@Repository
public interface UploadFileDao extends BaseDao<UploadFileDO> {

}
