package com.example.demo.constant.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Administrator
 * @date 2020-07-10 09:51
 * @description: elasticsearch 分词器类型
 */
@Getter
@AllArgsConstructor
public enum AnalyzerTypeEnum {
    /**
     * 标准分词器,会将中文分为每个字
     */
    standard("standard"),
    /**
     * 会将文本做最细粒度的拆分
     */
    ik_max_word("ik_max_word"),
    /**
     * 会做最粗粒度的拆分
     */
    ik_smart("ik_smart");

    private String value;

}
