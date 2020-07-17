package com.example.demo.util.excel;

import com.example.demo.util.container.ContainerUtil;
import com.example.demo.util.file.FileUtil;
import com.google.common.base.Charsets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import static com.example.demo.util.excel.ExcelUtil.TYPE.XLS;
import static com.example.demo.util.excel.ExcelUtil.TYPE.XLSX;

/**
 * @author administrator
 * @date 2020/07/17
 * @description: 类描述: excel文档工具类
 **/
public class ExcelUtil {

    /**
     * Content Type
     */
    private static final String CONTENT_TYPE = "application/msexcel";
    /**
     * Encoding
     */
    private static final String UTF_8 = "UTF-8";

    private ExcelUtil() {
    }

    @Getter
    @AllArgsConstructor
    public enum TYPE {
        /**
         * xls 文件后缀
         */
        XLS("xls"),
        /**
         * xlsx 文件后缀
         */
        XLSX("xlsx");
        private String suffix;

    }

    /**
     * 获取 excel 文件类型
     */
    public static ExcelUtil.TYPE getType(File file) {
        return getType(file.getName());
    }

    /**
     * 获取 excel 文件类型
     */
    public static ExcelUtil.TYPE getType(String fileName) {
        if (Objects.equals(FileUtil.getSuffix(fileName), XLS.getSuffix())) {
            return XLS;
        }
        if (Objects.equals(FileUtil.getSuffix(fileName), XLSX.getSuffix())) {
            return XLSX;
        }
        return null;
    }

    /**
     * 设置 Excel 文件流响应属性
     *
     * @param response        HTTP响应对象
     * @param fileName        Excel 文件名
     * @param fileNameCharset 文件名编码格式
     */
    public static void setResponse(HttpServletResponse response, String fileName, Charset fileNameCharset) {
        // 中文解析
        fileName = StringUtils.isNotBlank(fileName) ? new String(fileName.getBytes(Charsets.UTF_8), fileNameCharset) : fileName;
        // 设置响应头
        // 兼容不同浏览器的中文乱码问题
        response.setCharacterEncoding(UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setContentType(CONTENT_TYPE);
    }

    /**
     * 设置 Excel 文件流响应属性
     *
     * @param response HTTP响应对象
     * @param fileName Excel 文件名
     */
    public static void setResponseWithUrlEncoding(HttpServletResponse response, String fileName) {
        // 中文解析
        try {
            fileName = StringUtils.isNotBlank(fileName) ? URLEncoder.encode(fileName, UTF_8) : fileName;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 设置响应头
        // 兼容不同浏览器的中文乱码问题
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setContentType(CONTENT_TYPE);
    }


    /**
     * 删除指定行
     *
     * @param sheet 页
     * @param row   需要删除的行号
     */
    public static void removeRow(Sheet sheet, int row) {
        int lastRowNum = sheet.getLastRowNum();
        if (row >= 0 && row < lastRowNum) {
            // 直接将后面行的全部往上移动一格,覆盖
            sheet.shiftRows(row + 1, lastRowNum, -1);
        }
        // 最后一行就删除
        if (row == lastRowNum) {
            Row removingRow = sheet.getRow(row);
            if (removingRow != null) {
                sheet.removeRow(removingRow);
            }
        }
    }

    /**
     * 删除指定行号的全部行
     *
     * @param sheet          sheet页
     * @param deleteRowIndex 要删除的行号集合
     */
    public static void deleteSheetRow(Sheet sheet, List<Integer> deleteRowIndex) {
        if (ContainerUtil.isNotEmpty(deleteRowIndex)) {
            // 删除计数器,每次删一行之后,行数都要累减
            int deleteCount = 0;
            for (Integer rowNum : deleteRowIndex) {
                removeRow(sheet, rowNum - deleteCount++);
            }
        }
    }

    /**
     * excel 下拉框设置
     *
     * @param sheet        sheet页
     * @param firstRow     第一行
     * @param lastRow      最后一行
     * @param firstCol     第一列
     * @param lastCol      最后一列
     * @param listOfValues 下拉列表数组
     */
    public static void setCellRangeAddressList(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol, String[] listOfValues) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        //设置行列范围
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        //设置下拉框数据
        DataValidationConstraint constraint = helper.createExplicitListConstraint(listOfValues);
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        //处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }

    /**
     * 合并单元格
     *
     * @param sheet    sheet页
     * @param firstRow 第一行
     * @param lastRow  最后一行
     * @param firstCol 第一列
     * @param lastCol  最后一列
     */
    public static void mergedRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(region);
    }

    /**
     * 添加并复制行到指定位置
     *
     * @param workbook      Excel文档
     * @param sheet         页
     * @param templateIndex 模板行
     * @param addRowCount   添加的行数
     * @param jump          跳过的行数(指定位置到模板行的差值)
     * @param copyValueFlag 是否复制值
     */
    public static void addAndCopyRow(Workbook workbook, Sheet sheet, int templateIndex, int addRowCount, int jump, boolean copyValueFlag) {
        for (int index = 0; index < addRowCount; index++) {
            Row row = sheet.getRow(templateIndex + index);
            // param1:起始行 param2:结束行 param3:移动数量 param4:是否复制行高 param5:是否将原始行的高度设置为默认值
            sheet.shiftRows(templateIndex + jump + index + 1, sheet.getLastRowNum(), 1, true, false);
            Row newRow = sheet.createRow(templateIndex + jump + index + 1);
            CellStyle style = row.getRowStyle();
            if (Objects.nonNull(style)) {
                newRow.setRowStyle(style);
            }
            newRow.setHeight(row.getHeight());
            copyRow(workbook, sheet, row, newRow, copyValueFlag);
        }
    }

    /**
     * 向模板行下面复制一行
     *
     * @param workbook      Excel文档
     * @param sheet         页
     * @param templateIndex 模板行号
     * @param copyValueFlag 是否复制值
     */
    public void copyRow(Workbook workbook, Sheet sheet, int templateIndex, boolean copyValueFlag) {
        Row row = sheet.getRow(templateIndex);
        // 移动行
        sheet.shiftRows(templateIndex + 1, sheet.getLastRowNum(), 1, true, false);
        Row newRow = sheet.createRow(templateIndex + 1);
        CellStyle style = row.getRowStyle();
        if (Objects.nonNull(style)) {
            newRow.setRowStyle(style);
        }
        newRow.setHeight(row.getHeight());
        copyRow(workbook, sheet, row, newRow, copyValueFlag);
    }

    /**
     * 复制行
     *
     * @param workbook      Excel文档
     * @param sheet         页
     * @param sourceRow     原始行
     * @param targetRow     目标行
     * @param copyValueFlag 是否复制值
     */
    public static void copyRow(Workbook workbook, Sheet sheet, Row sourceRow, Row targetRow, boolean copyValueFlag) {
        targetRow.setHeight(sourceRow.getHeight());
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(targetRow.getRowNum(),
                        (targetRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
                        cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
                sheet.addMergedRegion(newCellRangeAddress);
            }
        }
        for (Cell cell : sourceRow) {
            Cell newCell = targetRow.createCell(cell.getColumnIndex());
            copyCell(workbook, cell, newCell, copyValueFlag);
        }
    }

    /**
     * 复制单元格
     *
     * @param workbook      Excel文档
     * @param sourceCell    原始单元格
     * @param targetCell    目标单元格
     * @param copyValueFlag 是否复制值
     */

    public static void copyCell(Workbook workbook, Cell sourceCell, Cell targetCell, boolean copyValueFlag) {
        CellStyle newStyle = workbook.createCellStyle();
        newStyle.cloneStyleFrom(sourceCell.getCellStyle());
        // 样式
        targetCell.setCellStyle(newStyle);
        targetCell.setCellFormula(sourceCell.getCellFormula());
        // 评论
        if (Objects.nonNull(sourceCell.getCellComment())) {
            targetCell.setCellComment(sourceCell.getCellComment());
        }
        // 复制值
        copyCellValue(sourceCell, targetCell, copyValueFlag);
    }

    private static void copyCellValue(Cell srcCell, Cell destCell, boolean copyValueFlag) {
        if (copyValueFlag) {
            CellType cellType = srcCell.getCellType();
            if (CellType.STRING == cellType) {
                destCell.setCellValue(srcCell.getStringCellValue());
            } else if (CellType.NUMERIC == cellType) {
                destCell.setCellValue(srcCell.getNumericCellValue());
            } else if (CellType.FORMULA == cellType) {
                destCell.setCellValue(srcCell.getCellFormula());
            } else if (CellType.BOOLEAN == cellType) {
                destCell.setCellValue(srcCell.getBooleanCellValue());
            } else if (CellType.ERROR == cellType) {
                destCell.setCellValue(srcCell.getErrorCellValue());
            }
        }
    }

}
