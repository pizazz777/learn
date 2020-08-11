package com.example.demo.manager.elasticsearch;

import com.example.demo.constant.elasticsearch.ElasticsearchHitResult;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.List;

/**
 * @author Administrator
 * @date 2020-05-29 14:18
 * @description: elasticsearch 操作
 */
public interface ElasticsearchRequest {

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
     * 创建索引
     *
     * @param index   索引
     * @param builder mapping设置
     * @return Boolean
     */
    Boolean createIndex(String index, XContentBuilder builder) throws IOException;

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
     * @param docId  文档id
     * @param object 保存的数据
     * @return Boolean
     */
    Boolean addDoc(String docId, Object object) throws IOException;


    /**
     * 添加文档
     *
     * @param index  索引
     * @param docId  文档id
     * @param object object
     * @return Boolean
     */
    Boolean addDoc(String index, String docId, Object object) throws IOException;

    /**
     * 获取文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return GetResponse
     */
    GetResponse getDoc(String index, String docId) throws IOException;

    /**
     * 获取文档并转为对应对象
     *
     * @param index 索引
     * @param docId 文档id
     * @param clz   对象类型
     * @return GetResponse
     */
    <T> T getDoc(String index, String docId, Class<T> clz) throws IOException;

    /**
     * 获取文档并转为对应对象
     *
     * @param docId 文档id
     * @param clz   对象类型
     * @return GetResponse
     */
    <T> T getDoc(String docId, Class<T> clz) throws IOException;

    /**
     * 修改文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return Boolean
     */
    Boolean updateDoc(String index, String docId, Object object) throws IOException;

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


    /**
     * 组合查询
     *
     * @param index 索引
     * @param field 字段名
     * @param value 要搜索的关键字
     * @param from  开始的偏移量
     * @param size  大小
     * @param clz   对象类型
     */
    <T> List<T> search(String index, String field, String value, Integer from, Integer size, Class<T> clz) throws IOException;


    /**
     * 组合查询(包含高亮)
     *
     * @param index 索引
     * @param field 字段名
     * @param value 要搜索的关键字
     * @param from  开始的偏移量
     * @param size  大小
     * @param clz   对象类型
     */
    <T> List<ElasticsearchHitResult<T>> searchWithHighlight(String index, String field, String value, Integer from, Integer size, Class<T> clz) throws IOException;

}
