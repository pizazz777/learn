package com.example.demo.office.excel.format;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

/**
 * @author Administrator
 * @date 2020-07-22 16:29
 * @description: excel数值处理类
 */
public interface ExcelNumericFormat {

    /**
     * 单元格数值的处理方法,先处理日期,再处理整数,再处理小数,支持科学计数法(E)的处理
     *
     * @param cell 单元格
     * @return 值
     * {@link org.apache.poi.ss.usermodel.CellType#NUMERIC}
     */
    String getNumericValue(Cell cell);

    /**
     * 获取单元格值
     *
     * @param cell      单元格
     * @param evaluator 公式计算器
     * @return 值
     */
    String getCellValue(Cell cell, FormulaEvaluator evaluator);

}
