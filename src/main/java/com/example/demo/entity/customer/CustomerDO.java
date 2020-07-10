package com.example.demo.entity.customer;

import com.example.demo.annotation.elasticsearch.Document;
import com.example.demo.annotation.elasticsearch.Mapping;
import com.example.demo.constant.es.AnalyzerTypeEnum;
import com.example.demo.constant.es.FieldTypeEnum;
import com.example.demo.entity.PageBean;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hxx
 * @version 1.0
 * @date 2020/04/28
 * @description: 类描述: 客户 Model
 **/
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
/*
 * org.springframework.data.elasticsearch.annotations @Document
 * indexName: 索引库的名字,建议以项目的名称命名
 * type: 类型,建议以实体的名称命名
 * shards: 默认分区数
 * replica: 每个分区默认的备份数
 * refreshInterval: 刷新间隔
 * indexStoreType: 索引文件存储类型
 * createIndex: 是否创建索引
 */
//@Document(indexName = "learn", type = "customer", shards = 3)
@Document(indexName = "customer")
public class CustomerDO extends PageBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 姓名
     */
    private String username;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 备注
     */
    @Mapping(type = FieldTypeEnum.Text, analyzer = AnalyzerTypeEnum.ik_max_word)
    private String remark;
    /**
     * 归属用户
     */
    private Long belongToUserId;
    /**
     * 客户级别
     */
    private Integer level;
    /**
     * 客户来源
     */
    private Integer source;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建人
     */
    private Long createUserId;

    /* ------------------ 非数据库数据分割线 ------------------ */


    private String intro;

}
