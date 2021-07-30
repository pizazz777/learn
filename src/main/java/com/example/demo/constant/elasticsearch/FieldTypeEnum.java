package com.example.demo.constant.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Administrator
 * @date 2020-07-10 10:53
 */
@Getter
@AllArgsConstructor
public enum  FieldTypeEnum {

    Text("text"),
    Byte("byte"),
    Short("short"),
    Integer("integer"),
    Long("long"),
    Date("date"),
    Half_Float("half_float"),
    Float("float"),
    Double("double"),
    Boolean("boolean"),
    Object("object"),
    Auto("auto"),
    Nested("nested"),
    Ip("ip"),
    Attachment("attachment"),
    // keyword修饰的字段不会分割,也就不会被分词器分词  而text可以
    Keyword("keyword");

    private String value;

}
