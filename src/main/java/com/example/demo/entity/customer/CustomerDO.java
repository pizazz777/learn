package com.example.demo.entity.customer;

import lombok.*;
import com.example.demo.entity.PageBean;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

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
@Document(indexName = "")
public class CustomerDO extends PageBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @Id
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
