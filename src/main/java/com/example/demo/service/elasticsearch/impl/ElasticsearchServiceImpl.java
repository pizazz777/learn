package com.example.demo.service.elasticsearch.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.component.ElasticsearchComponent;
import com.example.demo.component.ElasticsearchHitResult;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.response.ResCode;
import com.example.demo.component.response.ResResult;
import com.example.demo.entity.customer.CustomerDO;
import com.example.demo.service.elasticsearch.ElasticsearchService;
import com.example.demo.util.container.ContainerUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author Administrator
 * @date 2020-06-02 14:40
 */
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private ElasticsearchComponent elasticsearchComponent;

    @Autowired
    public ElasticsearchServiceImpl(ElasticsearchComponent elasticsearchComponent) {
        this.elasticsearchComponent = elasticsearchComponent;
    }

    /**
     * 查询索引是否存在
     *
     * @param index 索引
     * @return r
     */
    @Override
    public ResResult existsByIndex(String index) throws ServiceException {
        try {
            return elasticsearchComponent.existsByIndex(index) ? ResResult.success() : ResResult.fail();
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 创建索引
     *
     * @param index 索引
     * @return r
     */
    @Override
    public ResResult createIndex(String index) throws ServiceException {
        try {
            return elasticsearchComponent.createIndex(index) ? ResResult.success() : ResResult.fail();
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 删除索引
     *
     * @param index 索引
     * @return r
     */
    @Override
    public ResResult deleteIndex(String index) throws ServiceException {
        try {
            return elasticsearchComponent.deleteIndex(index) ? ResResult.success() : ResResult.fail();
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 查看文档是否存在
     *
     * @param index 索引
     * @param docId 文档id
     * @return r
     */
    @Override
    public ResResult existsByDoc(String index, String docId) throws ServiceException {
        try {
            return elasticsearchComponent.existsByDoc(index, docId) ? ResResult.success() : ResResult.fail();
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 添加文档
     *
     * @param index  索引
     * @param docId  文档id
     * @param object 对象
     * @return 生成的id
     */
    @Override
    public ResResult addDoc(String index, String docId, Object object) throws ServiceException {
        try {
            String id = elasticsearchComponent.addDoc(index, docId, object);
            return StringUtils.isNotBlank(id) ? ResResult.success(id) : ResResult.fail();
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 获取文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return r
     */
    @Override
    public ResResult getDoc(String index, String docId) throws ServiceException {
        try {
//            GetResponse response = elasticsearchComponent.getDoc(index, docId);
            CustomerDO customer = elasticsearchComponent.getDoc(index, docId, CustomerDO.class);

            // 返回key value形式
            // Map<String, Object> source = response.getSource();
            // 返回json格式
            // String json = response.getSourceAsString();
            // CustomerDO customer = JSON.parseObject(json, CustomerDO.class);
            // 也是kv形式
            // Map<String, Object> sourceAsMap = response.getSourceAsMap();
            /*
                {
                    "code": 1000,
                    "msg": "获取文档:请求成功",
                    "data": {
                        "exists": true,
                        "fields": {},
                        "fragment": false,
                        "id": "zs",
                        "index": "people",
                        "primaryTerm": 1,
                        "seqNo": 1,
                        "source": {
                            "createTime": "2020-06-02T15:58:01.756",
                            "level": 3,
                            "sex": 1,
                            "mobile": "18581598259",
                            "start": 0,
                            "id": 2,
                            "username": "张三"
                        },
                        "sourceAsBytes": "eyJjcmVhdGVUaW1lIjoiMjAyMC0wNi0wMlQxNTo1ODowMS43NTYiLCJpZCI6MiwibGV2ZWwiOjMsIm1vYmlsZSI6IjE4NTgxNTk4MjU5Iiwic2V4IjoxLCJzdGFydCI6MCwidXNlcm5hbWUiOiLlvKDkuIkifQ==",
                        "sourceAsBytesRef": {
                            "fragment": true
                        },
                        "sourceAsMap": {
                            "createTime": "2020-06-02T15:58:01.756",
                            "level": 3,
                            "sex": 1,
                            "mobile": "18581598259",
                            "start": 0,
                            "id": 2,
                            "username": "张三"
                        },
                        "sourceAsString": "{\"createTime\":\"2020-06-02T15:58:01.756\",\"id\":2,\"level\":3,\"mobile\":\"18581598259\",\"sex\":1,\"start\":0,\"username\":\"张三\"}",
                        "sourceEmpty": false,
                        "sourceInternal": {
                            "fragment": true
                        },
                        "type": "_doc",
                        "version": 2
                    }
                }
             */
            return ResResult.success(customer);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 修改文档
     *
     * @param index  索引
     * @param docId  文档id
     * @param jsonString object
     * @return r
     */
    @Override
    public ResResult updateDoc(String index, String docId, String jsonString) throws ServiceException {
        try {
            CustomerDO customer = JSONObject.parseObject(jsonString, CustomerDO.class);
            return elasticsearchComponent.updateDoc(index, docId, customer) ? ResResult.success() : ResResult.fail();
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 删除文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return r
     */
    @Override
    public ResResult deleteDoc(String index, String docId) throws ServiceException {
        try {
            return elasticsearchComponent.deleteDoc(index, docId) ? ResResult.success() : ResResult.fail();
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 查询
     *
     * @param index 索引
     * @param field 字段名
     * @param value 要搜索的关键字
     * @param from  开始的偏移量
     * @param size  大小
     */
    @Override
    public ResResult search(String index, String field, String value, Integer from, Integer size) throws ServiceException {
        try {
//            SearchResponse searchWithHighlight = elasticsearchComponent.searchWithHighlight(index, field, value, from, size);
            List<ElasticsearchHitResult<CustomerDO>> list = elasticsearchComponent.searchWithHighlight(index, field, value, from, size, CustomerDO.class);
            return ContainerUtil.isNotEmpty(list) ? ResResult.success(list) : ResResult.fail(ResCode.NOT_FOUND);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

}
