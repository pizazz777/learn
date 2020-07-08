package com.example.demo.service.elasticsearch;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResResult;

/**
 * @author Administrator
 * @date 2020-06-02 14:40
 */
public interface ElasticsearchService {

    /**
     * 查询索引是否存在
     *
     * @param index 索引
     * @return r
     */
    ResResult existsByIndex(String index) throws ServiceException;

    /**
     * 创建索引
     *
     * @param index 索引
     * @return r
     */
    ResResult createIndex(String index) throws ServiceException;

    /**
     * 删除索引
     *
     * @param index 索引
     * @return r
     */
    ResResult deleteIndex(String index) throws ServiceException;

    /**
     * 查看文档是否存在
     *
     * @param index 索引
     * @param docId 文档id
     * @return r
     */
    ResResult existsByDoc(String index, String docId) throws ServiceException;

    /**
     * 添加文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return r
     */
    ResResult addDoc(String index, String docId, Object object) throws ServiceException;

    /**
     * 获取文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return r
     */
    ResResult getDoc(String index, String docId) throws ServiceException;

    /**
     * 修改文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return r
     */
    ResResult updateDoc(String index, String docId, String jsonString) throws ServiceException;

    /**
     * 删除文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return r
     */
    ResResult deleteDoc(String index, String docId) throws ServiceException;

    /**
     * 查询
     *
     * @param index 索引
     * @param field 字段名
     * @param value 要搜索的关键字
     * @param from  开始的偏移量
     * @param size  大小
     */
    ResResult search(String index, String field, String value, Integer from, Integer size) throws ServiceException;

}
