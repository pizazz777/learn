package com.example.demo.constant.office;

import com.example.demo.util.file.FileUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-07-22 14:08
 * @description: excel类型
 */
@Getter
@AllArgsConstructor
public enum ExcelTypeEnum {

    XLS("xls"),

    XLSX("xlsx");

    private String suffix;

    /**
     * 获取 excel 文件类型
     */
    public static ExcelTypeEnum getType(File file) {
        return getType(file.getName());
    }

    /**
     * 获取 excel 文件类型
     */
    public static ExcelTypeEnum getType(String fileName) {
        if (Objects.equals(FileUtil.getSuffix(fileName), XLS.getSuffix())) {
            return XLS;
        }
        if (Objects.equals(FileUtil.getSuffix(fileName), XLSX.getSuffix())) {
            return XLSX;
        }
        return null;
    }

}
