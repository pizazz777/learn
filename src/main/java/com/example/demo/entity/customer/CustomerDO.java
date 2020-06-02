package com.example.demo.entity.customer;

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
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
/*
 * @Document
 * indexName: 索引库的名字,建议以项目的名称命名
 * type: 类型,建议以实体的名称命名
 * shards: 默认分区数
 * replica: 每个分区默认的备份数
 * refreshInterval: 刷新间隔
 * indexStoreType: 索引文件存储类型
 * createIndex: 是否创建索引
 */
//@Document(indexName = "learn", type = "customer", shards = 3)
public class CustomerDO extends PageBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    // 主键注解
//    @Id
    private Long id;
    /**
     * 姓名
     */
    // @Field默认是可以不加的,默认会把所有属性都添加到ES中.加上@Field后,@Document默认把所有字段加上索引失效,只有加@Field才会被索引
//    @Field
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

}
