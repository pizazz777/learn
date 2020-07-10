package com.example.demo.constant.es;

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
    Keyword("keyword");

    private String value;

}
