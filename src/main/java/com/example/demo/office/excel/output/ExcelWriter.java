package com.example.demo.office.excel.output;

import com.example.demo.constant.office.ExcelTypeEnum;
import com.huang.exception.ExcelException;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-22 13:49
 * @description: Excel 写出类
 */
public interface ExcelWriter {


    /**
     * 写出到Excel
     *
     * @param dataMapList  数据
     * @param outputStream 流
     * @param type         输出类型
     * @throws ExcelException e
     */
    void writeToOutputStream(List<LinkedHashMap<String, Serializable>> dataMapList, OutputStream outputStream, ExcelTypeEnum type) throws ExcelException;

}
