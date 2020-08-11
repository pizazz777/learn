package com.example.demo.office.pdf.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: administrator
 * @date 2020/7/27
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
