package com.example.demo.office.excel.model;

import com.example.demo.constant.office.ExcelTypeEnum;
import lombok.*;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author administrator
 * @date 2020/07/22
 * @description: excel信息对象
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelWorkbook {

    /**
     * excel 类型
     */
    private ExcelTypeEnum type;
    /**
     * Excel 工作簿
     */
    private Workbook workbook;
    /**
     * Excel 公式计算器
     */
    private FormulaEvaluator formulaEvaluator;
    /**
     * Excel 第一张表单
     */
    private Sheet sheet;
}
