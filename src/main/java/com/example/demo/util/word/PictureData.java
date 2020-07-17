package com.example.demo.util.word;

import lombok.Data;

/**
 * @author administrator
 * @date 2020/07/17
 * @description:
 **/
@Data
public class PictureData {

    /**
     * 路径
     */
    private String path;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * @see org.apache.poi.xwpf.usermodel.Document
     */
    private Integer pictureType;
    /**
     * 宽
     */
    private Integer width;
    /**
     * 高
     */
    private Integer height;
}