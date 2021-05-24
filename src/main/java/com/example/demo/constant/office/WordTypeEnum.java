package com.example.demo.constant.office;

import com.huang.util.file.FileUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-07-22 14:08
 * @description: word类型
 */
@Getter
@AllArgsConstructor
public enum WordTypeEnum {

    DOC("doc"),

    DOCX("docx");

    private String suffix;

    /**
     * 获取 word 文件类型
     */
    public static WordTypeEnum getType(File file) {
        return getType(file.getName());
    }

    /**
     * 获取 word 文件类型
     */
    public static WordTypeEnum getType(String fileName) {
        if (Objects.equals(FileUtil.getSuffix(fileName), DOC.getSuffix())) {
            return DOC;
        }
        if (Objects.equals(FileUtil.getSuffix(fileName), DOCX.getSuffix())) {
            return DOCX;
        }
        return null;
    }

}
