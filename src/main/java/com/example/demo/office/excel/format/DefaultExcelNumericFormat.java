package com.example.demo.office.excel.format;

import com.example.demo.util.time.DateConst;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-22 16:31
 * @description: excel数值处理类
 */
public class DefaultExcelNumericFormat implements ExcelNumericFormat {


    /**
     * 默认日期类的 index
     * get the index of the data format. Built in formats are defined at {@link BuiltinFormats}.
     *
     * @see DataFormat
     */
    private static List<Integer> DATE_INDEX = Arrays.asList(14, 31, 57, 58);
    /**
     * 默认时间类的 index
     * get the index of the data format. Built in formats are defined at {@link BuiltinFormats}.
     *
     * @see DataFormat
     */
    private static List<Integer> TIME_INDEX = Arrays.asList(20, 32);

    /**
     * {@link CellType#NUMERIC} 的处理方法,先处理日期,再处理整数,再处理小数,支持科学计数法(E)的处理
     */
    @Override
    public String getNumericValue(Cell cell) {
        if (DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            return DateFormatUtils.format(date, DateConst.DEFAULT_DATE_FORMAT);
        } else {
            double doubleVal = cell.getNumericCellValue();
            int format = cell.getCellStyle().getDataFormat();
            if (DATE_INDEX.contains(format)) {
                // 日期
                Date date = DateUtil.getJavaDate(doubleVal);
                return DateFormatUtils.format(date, DateConst.DEFAULT_DATE_FORMAT);
            } else if (TIME_INDEX.contains(format)) {
                // 时间
                Date date = DateUtil.getJavaDate(doubleVal);
                return DateFormatUtils.format(date, "HH:mm");
            } else {
                // 整数
                long longVal = Math.round(doubleVal);
                if (Double.parseDouble(longVal + ".0") == doubleVal) {
                    return String.valueOf(longVal);
                } else {
                    // 小数
                    return String.valueOf(doubleVal);
                }
            }
        }
    }

    /**
     * 获取单元格值
     *
     * @param cell      单元格
     * @param evaluator 公式计算器
     * @return 值
     */
    @Override
    public String getCellValue(Cell cell, FormulaEvaluator evaluator) {
        String value = "";
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case ERROR:
                value = String.valueOf(cell.getErrorCellValue());
                break;
            case BLANK:
                // nothing to do
                break;
            case STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                value = evaluator.evaluate(cell).formatAsString();
                break;
            case NUMERIC:
                value = getNumericValue(cell);
                break;
            default:
                break;
        }
        return value;
    }

}
