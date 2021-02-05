package com.example.demo.manager.elasticsearch.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.annotation.elasticsearch.Document;
import com.example.demo.constant.elasticsearch.ElasticsearchHitResult;
import com.example.demo.constant.es.AnalyzerTypeEnum;
import com.example.demo.manager.elasticsearch.ElasticsearchRequest;
import com.google.common.collect.Maps;
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
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.demo.constant.es.IndexConst.DEFAULT_INDEX_NAME;

/**
 * @author Administrator
 * @date 2020-05-29 14:19
 */
@Component
public class ElasticsearchRequestImpl implements ElasticsearchRequest {

    /**
     * {@link org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientConfigurations.RestHighLevelClientConfiguration#elasticsearchRestHighLevelClient}
     */
    private RestHighLevelClient client;

    @Autowired
    public ElasticsearchRequestImpl(RestHighLevelClient client) {
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
     * 默认方式创建索引
     *
     * @param index 索引
     * @return Boolean
     */
    @Override
    public Boolean createIndex(String index) throws IOException {
        if (!this.existsByIndex(index)) {
            CreateIndexResponse response = client.indices().create(new CreateIndexRequest(index), RequestOptions.DEFAULT);
            return response.isAcknowledged();
        }
        return true;
    }


    /**
     * 自定义创建索引
     *
     * @param index   索引
     * @param builder mapping设置
     * @return Boolean
     */
    @Override
    public Boolean createIndex(String index, XContentBuilder builder) throws IOException {
        if (!this.existsByIndex(index)) {
            if (Objects.isNull(builder)) {
                return createIndex(index);
            }
            CreateIndexRequest request = new CreateIndexRequest(index);
            request.mapping(builder);
            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
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
     * 添加文档,使用对象注解中的索引名称或默认索引名称
     *
     * @param docId  文档id
     * @param object 保存的数据
     * @return String
     */
    @Override
    public Boolean addDoc(String docId, Object object) throws IOException {
        Document document = object.getClass().getDeclaredAnnotation(Document.class);
        String index;
        if (Objects.isNull(document)) {
            index = DEFAULT_INDEX_NAME;
        } else {
            index = document.indexName();
        }
        return this.addDoc(index, docId, object);
    }

    /**
     * 添加文档,自定义索引名称
     *
     * @param index 索引
     * @param docId 文档id
     * @return String
     */
    @Override
    public Boolean addDoc(String index, String docId, Object object) throws IOException {
        IndexRequest request = new IndexRequest(index);
        if (StringUtils.isNotBlank(docId)) {
            request.id(docId);
        }
        request.timeout(TimeValue.timeValueSeconds(1));
        request.source(JSON.toJSONString(object), XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        RestStatus Status = response.status();
        return Status == RestStatus.OK || Status == RestStatus.CREATED;
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
     * 获取文档并转为对应对象
     *
     * @param docId 文档id
     * @param clz   对象类型
     * @return GetResponse
     */
    @Override
    public <T> T getDoc(String docId, Class<T> clz) throws IOException {
        Document document = clz.getDeclaredAnnotation(Document.class);
        if (Objects.isNull(document)) {
            throw new IllegalArgumentException("没有设置索性!");
        }
        String indexName = document.indexName();
        return getDoc(indexName, docId, clz);
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
        // 拼装查询条件,合并查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 通配符查询,?匹配单个字符,*匹配多个字符
        // WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery(field, value);
        // 词条查询 注:会对传入的文本原封不动地(不分词)去查询,所以使用该查询需要在建立索引的时候没有使用分词器(no_analyze)
        // TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("remark", "过期手机号码".toLowerCase());
        // 匹配查询,会对传入的文本进行分词处理后再去查询,部分命中的结果也会按照评分由高到底显示出来
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(field, value).analyzer(AnalyzerTypeEnum.ik_max_word.getValue());
        // 短语查询,会先进行分词,然后文档需要包含分词后的所有词并且还要保持这些分词的相对顺序和文档中的一致
        // MatchPhrasePrefixQueryBuilder matchPhrasePrefixQueryBuilder = QueryBuilders.matchPhrasePrefixQuery(field, value);
        // 匹配所有
        // MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        // id查询
        // IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery();
        // idsQueryBuilder.addIds("id1", "id2", "id3");
        // 多字段匹配查询,匹配字段名为field1,filed2,值为value的文档
        // MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(value, filed1, filed2);
        // 模糊查询,不能用通配符
        // FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery(field, value).fuzziness(Fuzziness.AUTO);
        // 前缀查询
        // PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery(field, value);
        // 范围查询 左右闭合
        // RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("filed");
        // rangeQueryBuilder.gte("max");
        // rangeQueryBuilder.lte("min");
        // 查询解析查询字符串
        // QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery("");
        // 跨度查询
        // SpanFirstQueryBuilder spanFirstQueryBuilder = QueryBuilders.spanFirstQuery(QueryBuilders.spanTermQuery(field, value), 3000);
        // 筛选查询
        // SpanNearQueryBuilder spanNearQueryBuilder = QueryBuilders.spanNearQuery(QueryBuilders.spanTermQuery(field, value), 2000).addClause(QueryBuilders.spanTermQuery(field, value)).inOrder(false);
        // 筛选查询 参数1: 匹配的字段的值, 参数2: 排除的字段的值
        // SpanNotQueryBuilder spanNotQueryBuilder = QueryBuilders.spanNotQuery(QueryBuilders.spanTermQuery(field, value), QueryBuilders.spanTermQuery(field, value));
        // 正则查询, value可以是正则表达式
        // RegexpQueryBuilder regexpQueryBuilder = QueryBuilders.regexpQuery(field, value);
        // 实现基于内容推荐,支持实现一句话相似文章查询
        // fields: 要匹配的字段,不填默认_all, likeTexts: 匹配的文本, likeItems: 明细
        // MoreLikeThisQueryBuilder moreLikeThisQueryBuilder = QueryBuilders.moreLikeThisQuery(new String[]{"field1", "field2"}, new String[]{"要查询的文本"}, null);
        // 一篇文档中一个词语至少出现次数,小于这个值的词将被忽略,默认值2
        // moreLikeThisQueryBuilder.minTermFreq(2);
        // 一条查询语句中允许最多查询词语的个数,默认是25
        // moreLikeThisQueryBuilder.maxQueryTerms(25);
        // 停止词,匹配时会忽略停止词
        // moreLikeThisQueryBuilder.stopWords("stop");
        // 一个词语最少在多少篇文档中出现,小于这个值的词会将被忽略,默认5
        // moreLikeThisQueryBuilder.minDocFreq(5);
        // 一个词语最多在多少篇文档中出现,大于这个值的词会将被忽略,默认是int最大值
        // moreLikeThisQueryBuilder.maxDocFreq(Integer.MAX_VALUE);
        // 最小的词语长度,默认是0
        // moreLikeThisQueryBuilder.minWordLength(0);
        // 最多的词语长度,默认是0
        // moreLikeThisQueryBuilder.maxWordLength(0);
        // 设置词语权重,默认是1
        // moreLikeThisQueryBuilder.boostTerms(1);
        // 设置查询权重,默认是1
        // moreLikeThisQueryBuilder.boost(1.0f);
        // 设置使用的分词器,使用该字段指定的分词器
        // moreLikeThisQueryBuilder.analyzer("分词器");

        // 对子查询的结果做union, score沿用子查询的最大值, 广泛用于multi-field查询
        // DisMaxQueryBuilder disMaxQueryBuilder = QueryBuilders.disMaxQuery();
        // disMaxQueryBuilder.add(termQueryBuilder).add(rangeQueryBuilder);

        // 组合查询
        boolQueryBuilder
                // must=and 代表返回的文档必须满足must子句的条件,会参与计算分值
                .must(matchQueryBuilder);
        // mustNot=not 代表必须不满足子句的条件
        // .mustNot(termQueryBuilder)
        // filter=and 代表返回的文档必须满足filter子句的条件,但不会参与计算分值
        // .filter(rangeQueryBuilder)
        // should=or 代表返回的文档可能满足should子句的条件,也可能不满足,有多个should时满足任何一个就可以
        // .should(wildcardQueryBuilder)

        // 分页+排序+搜索时长
        sourceBuilder.query(boolQueryBuilder)
                // 第一个数组是需要获取的字段,第二个是过滤的字段,默认获取全部
                // .fetchSource(new String[] {"username","id","level"}, new String[] {"createTime"})
                // 排序
                // .sort(new FieldSortBuilder("id").order(SortOrder.DESC))
                // .timeout(TimeValue.timeValueSeconds(5))
                // 开始的偏移量,从0开始!!! 从0开始!!! 从0开始!!!
                .from(from)
                // 大小
                .size(size);

        // 设置高亮字段 注: 高亮只会对进行查询了的字段有效,没有被查询的字段使用会无效
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        // 高亮的前缀标签和结束标签
        // highlightBuilder.preTags("<red>");
        // highlightBuilder.postTags("</red>");
        // 设置高亮字段
        highlightBuilder.field(field);
        sourceBuilder.highlighter(highlightBuilder);

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
        return Arrays.stream(hits.getHits()).map(hit -> JSONObject.parseObject(hit.getSourceAsString(), clz)).collect(Collectors.toList());
    }


    @Override
    public <T> List<ElasticsearchHitResult<T>> searchWithHighlight(String index, String field, String value, Integer from, Integer size, Class<T> clz) throws IOException {
        SearchResponse search = this.search(index, field, value, from, size);
        SearchHits hits = search.getHits();
        return Arrays.stream(hits.getHits()).map(hit -> {
            ElasticsearchHitResult<T> result = new ElasticsearchHitResult<>();
            result.setObject(JSONObject.parseObject(hit.getSourceAsString(), clz));
            // 设置高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            Map<String, Object> highlightMap = Maps.newHashMap();
            highlightFields.forEach((k, v) -> highlightMap.put(k, Arrays.stream(v.getFragments()).map(Text::string).collect(Collectors.toList())));
            result.setHighlightMap(highlightMap);
            return result;
        }).collect(Collectors.toList());
    }
}
