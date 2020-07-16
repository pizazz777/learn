package com.example.demo.configuration;

import com.example.demo.annotation.elasticsearch.Document;
import com.example.demo.annotation.elasticsearch.Mapping;
import com.example.demo.component.ElasticsearchComponent;
import com.example.demo.properties.EsProperties;
import com.example.demo.util.clz.ClassUtil;
import com.example.demo.util.container.ContainerUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-07-10 09:51
 */
@Component
@Slf4j
public class EsConfig implements ApplicationRunner {

    private EsProperties esProperties;
    private ElasticsearchComponent elasticsearchComponent;

    @Autowired
    public EsConfig(EsProperties esProperties, ElasticsearchComponent elasticsearchComponent) {
        this.esProperties = esProperties;
        this.elasticsearchComponent = elasticsearchComponent;
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     */
    @Override
    public void run(ApplicationArguments args) {
        try {
            init();
        } catch (IOException | ClassNotFoundException e) {
            log.error("项目初始化Elasticsearch Mapping失败!");
            e.printStackTrace();
        }
    }

    private void init() throws IOException, ClassNotFoundException {
        List<String> scanPackageList = esProperties.getScanPackageList();
        if (ContainerUtil.isEmpty(scanPackageList)) {
            return;
        }
        for (String scanPackage : scanPackageList) {
            List<Class<?>> classList = ClassUtil.getClasses(scanPackage);
            for (Class<?> clz : classList) {
                // 创建索引
                Document document = clz.getDeclaredAnnotation(Document.class);
                if (Objects.nonNull(document)) {
                    String index = document.indexName();
                    // 字段
                    Field[] fields = clz.getDeclaredFields();
                    if (ContainerUtil.isEmpty(fields)) {
                        continue;
                    }
                    XContentBuilder builder = XContentFactory.jsonBuilder().startObject().startObject("properties");
                    boolean classHasMappingFlag = setMapping(fields, builder, true);
                    if (!classHasMappingFlag) {
                        // 如果类中没有设置Mapping注解,就给所有字段加上默认的属性
                        setMapping(fields, builder, false);
                    }
                    builder.endObject().endObject();
                    Boolean success = elasticsearchComponent.createIndex(index, builder);
                    if (!success) {
                        log.error("创建索引失败..");
                    }
                }
            }
        }
    }

    private boolean setMapping(Field[] fields, XContentBuilder builder, boolean requireMappingFlag) throws IOException {
        boolean classHasMappingFlag = false;
        for (Field field : fields) {
            Mapping mapping = field.getDeclaredAnnotation(Mapping.class);
            if (Objects.nonNull(mapping) || !requireMappingFlag) {
                builder.startObject(field.getName())
                        // 字段权重,用于查询时评分,关键字段的权重就会高一些,默认都是1
                        // .field("boost", 1.5)
                        // 自定_all字段,指定某几个字段拼接成自定义
                        // .field("copy_to", "field_name")
                        // 是否会被索引 注:只能在最顶层,并且type为object的时候设置才生效
                        // .field("enabled", true)
                        // 添加文档时忽略该字段的异常数据
                        // .field("ignore_malformed", true)
                        // 控制数据存储方式,设置为true就会单独存储一份
                        // .field("store", true)
                        // 给空值设置一个默认字符串
                        // .field("null_value", "NULL")
                        // .startObject("fields").startObject("raw").field("type", "key_word").endObject().endObject()
                        .field("index", mapping.index())
                        .field("type", mapping.type().getValue());
                if (mapping.index()) {
                    builder.field("analyzer", mapping.analyzer().getValue());
                }
                builder.endObject();
                if (!classHasMappingFlag) {
                    classHasMappingFlag = true;
                }
            }
        }
        return classHasMappingFlag;
    }
}
