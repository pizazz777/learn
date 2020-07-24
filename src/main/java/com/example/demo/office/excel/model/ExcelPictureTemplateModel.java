package com.example.demo.office.excel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author administrator
 * @date 2020/07/22
 * @description: 类描述: Excel图片模板模型对象
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelPictureTemplateModel implements Serializable {

    private static final long serialVersionUID = 658196475646629179L;

    /**
     * 图片地址
     */
    private String path;
    /**
     * 图片单元格高度
     */
    private Integer height = 80;
    /**
     * 图片单元格宽度
     */
    private Integer width = 20 * 256;
    /**
     * 图片和单元格的横向大小比例
     */
    private Integer scaleX = 1;
    /**
     * 图片和单元格的纵向大小比例
     */
    private Integer scaleY = 1;

}
