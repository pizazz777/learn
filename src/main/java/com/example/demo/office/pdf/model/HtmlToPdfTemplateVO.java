package com.example.demo.office.pdf.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: green
 * @version: 1.0
 * @Date: 2019/7/27
 * @description:
 */
@Getter
@Setter
@ToString
public class HtmlToPdfTemplateVO extends TemplateVO {
    /**
     * 富文本的内容
     */
    private String htmlContent;
}
