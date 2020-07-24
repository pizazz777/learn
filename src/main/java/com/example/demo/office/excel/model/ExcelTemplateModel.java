package com.example.demo.office.excel.model;

import com.example.demo.component.exception.ExcelException;
import org.apache.poi.ss.usermodel.Cell;

/**
 * @author Administrator
 * @date 2020-07-22 17:18
 * @description: excel模板对象
 */
public interface ExcelTemplateModel<T> {

    /**
     * 将单元格的值放入对象
     *
     * @param workbook workbook
     * @param cell     单元格
     * @param object   对象
     * @throws ExcelException e
     */
    void putCellValueToDO(ExcelWorkbook workbook, Cell cell, T object) throws ExcelException;

}
