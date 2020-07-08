package com.example.demo.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2020-05-29 14:19
 */
@Component
public class ElasticsearchComponentImpl implements ElasticsearchComponent {

    /**
     * {@link org.springframework.boot.autoconfigure.elasticsearch.rest RestClientConfigurations.RestHighLevelClientConfiguration}
     */
    private RestHighLevelClient client;

    @Autowired
    public ElasticsearchComponentImpl(RestHighLevelClient client) {
        this.client = client;
    }

    /**
     * 查询索引是否存在
     *
     * @param index 索引
     * @return Boolean
     */
    @Override
    public Boolean existsByIndex(String index) throws IOException {
        return client.indices().exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
    }

    /**
     * 创建索引
     *
     * @param index 索引
     * @return Boolean
     */
    @Override
    public Boolean createIndex(String index) throws IOException {
        if (this.existsByIndex(index)) {
            CreateIndexResponse response = client.indices().create(new CreateIndexRequest(index), RequestOptions.DEFAULT);
            return response.isAcknowledged();
        }
        return true;
    }

    /**
     * 删除索引
     *
     * @param index 索引
     * @return Boolean
     */
    @Override
    public Boolean deleteIndex(String index) throws IOException {
        return client.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT).isAcknowledged();
    }

    /**
     * 查看文档是否存在
     *
     * @param index 索引
     * @param docId 文档id
     * @return Boolean
     */
    @Override
    public Boolean existsByDoc(String index, String docId) throws IOException {
        GetRequest request = new GetRequest(index, docId);
        //只判断索引是否存在不需要获取_source
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        return client.exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 添加文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return String
     */
    @Override
    public String addDoc(String index, String docId, Object object) throws IOException {
        IndexRequest request = new IndexRequest(index);
        if (StringUtils.isNotBlank(docId)) {
            request.id(docId);
        }
        request.timeout(TimeValue.timeValueSeconds(1));
        request.source(JSON.toJSONString(object), XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        RestStatus Status = response.status();
        return Status == RestStatus.OK || Status == RestStatus.CREATED ? response.getId() : "";
    }

    /**
     * 获取文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return GetResponse
     */
    @Override
    public GetResponse getDoc(String index, String docId) throws IOException {
        GetRequest request = new GetRequest(index, docId);
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        if (response.isExists()) {
            return response;
        }
        return null;
    }


    /**
     * 获取文档并转为对应对象
     *
     * @param index 索引
     * @param docId 文档id
     * @param clz   对象类型
     * @return GetResponse
     */
    @Override
    public <T> T getDoc(String index, String docId, Class<T> clz) throws IOException {
        GetRequest request = new GetRequest(index, docId);
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        if (response.isExists()) {
            String string = response.getSourceAsString();
            return JSONObject.parseObject(string, clz);
        }
        return null;
    }

    /**
     * 修改文档
     *
     * @param index  索引
     * @param docId  文档id
     * @param object 对象
     * @return Boolean
     */
    @Override
    public Boolean updateDoc(String index, String docId, Object object) throws IOException {
        UpdateRequest request = new UpdateRequest(index, docId);
        request.doc(JSON.toJSONString(object), XContentType.JSON);
        request.timeout(TimeValue.timeValueSeconds(1));
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        return response.status() == RestStatus.OK;
    }

    /**
     * 删除文档
     *
     * @param index 索引
     * @param docId 文档id
     * @return Boolean
     */
    @Override
    public Boolean deleteDoc(String index, String docId) throws IOException {
        DeleteRequest request = new DeleteRequest(index, docId);
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        return response.status() == RestStatus.OK;
    }

    /**
     * 组合查询
     *
     * @param index 索引
     * @param field 字段名
     * @param value 要搜索的关键字
     * @param from  开始的偏移量
     * @param size  大小
     */
    @Override
    public SearchResponse search(String index, String field, String value, Integer from, Integer size) throws IOException {
        // 检索构建
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 拼装查询条件,复合查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 匹配所有
        // MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        // 精准查询
        // TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(field, value);
        // 模糊查询,?匹配单个字符,*匹配多个字符
        WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery(field, value);
        // id查询
        // IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery();
        // idsQueryBuilder.addIds("id1", "id2", "id3");
        // 匹配单个字段,匹配字段名为field,值为value的文档
        // MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(field, value);
        // 匹配多个字段,匹配字段名为field1,filed2,值为value的文档
        // MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(value, filed1, filed2);
        // 模糊查询,不能用通配符
        // FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("", "").fuzziness(Fuzziness.AUTO);
        // 前缀查询
        // PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("", "");
        // 范围查询 左右闭合
        // RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("");
        // rangeQueryBuilder.gte("");
        // rangeQueryBuilder.lte("");
//        MoreLikeThisQueryBuilder moreLikeThisQueryBuilder = QueryBuilders.moreLikeThisQuery(new String[]{}, new String[]{});

        // 对子查询的结果做union, score沿用子查询的最大值, 广泛用于multi-field查询
        // DisMaxQueryBuilder disMaxQueryBuilder = QueryBuilders.disMaxQuery();
        // disMaxQueryBuilder.add(termQueryBuilder).add(rangeQueryBuilder);

        // 组合查询
        boolQueryBuilder
                // should == or
                .should(wildcardQueryBuilder);
                // must == and
//                .must(matchQueryBuilder);
                // mustNot == not
//                .mustNot(termQueryBuilder);

        // 分页+排序+搜索时长
        sourceBuilder.query(boolQueryBuilder)
                // 第一个数组是需要获取的字段,第二个是过滤的字段,默认获取全部
//                .fetchSource(new String[] {"username","id","level"}, new String[] {"createTime"})
                // 排序
//                .sort(new FieldSortBuilder("id").order(SortOrder.DESC))
                .timeout(TimeValue.timeValueSeconds(5))
                // 开始的偏移量,从0开始!!! 从0开始!!! 从0开始!!!
                .from(from)
                // 大小
                .size(size);

        // 查询请求
        SearchRequest request = new SearchRequest(index);
        request.source(sourceBuilder);
        return client.search(request, RequestOptions.DEFAULT);
    }


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
    @Override
    public <T> List<T> search(String index, String field, String value, Integer from, Integer size, Class<T> clz) throws IOException {
        SearchResponse search = this.search(index, field, value, from, size);
        SearchHits hits = search.getHits();
        List<T> list = Lists.newArrayList();
        hits.forEach(hit -> list.add(JSONObject.parseObject(hit.getSourceAsString(), clz)));
        return list;
    }
}
