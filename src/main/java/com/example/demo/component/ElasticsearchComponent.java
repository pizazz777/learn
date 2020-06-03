package com.example.demo.component;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;

/**
 * @author Administrator
 * @date 2020-05-29 14:18
 * @description: elasticsearch 操作
 */
public interface ElasticsearchComponent {

    /**
     * 查询索引是否存在
     *
     * @param index 索引
     * @return Boolean
     */
    Boolean existsByIndex(String index) throws IOException;

    /**
     * 创建索引
     *
     * @param index 索引
     * @return Boolean
     */
    Boolean createIndex(String index) throws IOException;

    /**
     * 删除索引
     *
     * @param index 索引
     * @return Boolean
     */
    Boolean deleteIndex(String index) throws IOException;

    /**
     * 查看文档是否存在
     *
     * @param index 索引
     * @param docId 文档id
     * @return Boolean
     */
    Boolean existsByDoc(String index, String docId) throws IOException;

    /**
     * 添加文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return Boolean
     */
    Boolean addDoc(String index, String docId, Object t) throws IOException;

    /**
     * 获取文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return GetResponse
     */
    GetResponse getDoc(String index, String docId) throws IOException;

    /**
     * 修改文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return Boolean
     */
    Boolean updateDoc(String index, String docId, Object t) throws IOException;

    /**
     * 删除文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return Boolean
     */
    Boolean deleteDoc(String index, String docId) throws IOException;

    /**
     * 组合查询
     *
     * @param index 索引
     * @param field 字段名
     * @param value 要搜索的关键字
     * @param from  开始的偏移量
     * @param size  大小
     */
    SearchResponse search(String index, String field, String value, Integer from, Integer size) throws IOException;

}
