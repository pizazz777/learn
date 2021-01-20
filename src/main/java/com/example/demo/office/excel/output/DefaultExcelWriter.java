package com.example.demo.office.excel.output;

import com.example.demo.component.exception.ExcelException;
import com.example.demo.constant.office.ExcelTypeEnum;
import com.example.demo.office.excel.model.ExcelPictureTemplateModel;
import com.example.demo.util.exception.ExceptionUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator
 * @date 2020-07-22 13:52
 * @description: Excel 写出类
 */
public class DefaultExcelWriter implements ExcelWriter {

    /**
     * 默认单元格高度
     */
    private final static int DEFAULT_CELL_HEIGHT = 20;
    /**
     * 默认单元格宽度
     */
    private final static int DEFAULT_CELL_WIDTH = 30;
    /**
     * 默认字体高度
     */
    private final static int DEFAULT_FONT_HEIGHT = 16;
    /**
     * 默认字体名称
     */
    private final static String DEFAULT_FONT_NAME = "Calibri";
    /**
     * 头部行的索引值
     */
    private final static int HEADER_ROW_INDEX = 0;
    /**
     * 默认数据行的索引值
     */
    private final static int DEFAULT_FIRST_DATA_ROW_INDEX = 1;


    /**
     * 写出到Excel
     *
     * @param dataMapList  数据
     * @param outputStream 流
     * @param type         输出类型
     * @throws ExcelException e
     */
    @Override
    public void writeToOutputStream(List<LinkedHashMap<String, Serializable>> dataMapList, OutputStream outputStream, ExcelTypeEnum type) throws ExcelException {
        ExceptionUtil.requireNonNull(dataMapList, new ExcelException("导出数据为空!"));
        // 根据需要生成的Excel类型获取Workbook
        Workbook workbook = getWorkbook(type);
        // 初始化sheet页
        Sheet sheet = initSheet(workbook);
        // 设置头部行标题数据
        putDataMapKeyToHeader(workbook, sheet, dataMapList);
        // 写入到输出流
        writeToOutputStream(workbook, sheet, dataMapList, outputStream);
    }

    /**
     * 写入到输出流
     *
     * @param workbook     workbook
     * @param sheet        sheet
     * @param dataMapList  数据
     * @param outputStream 输出流
     */
    private void writeToOutputStream(Workbook workbook, Sheet sheet, List<LinkedHashMap<String, Serializable>> dataMapList, OutputStream outputStream) {
        // 将数据写入到表格的行中
        putDataMapToRow(workbook, sheet, dataMapList);
        // 将workbook写入输出流
        writeWorkbook(workbook, outputStream);
    }

    /**
     * 写入到workbook
     *
     * @param workbook     workbook
     * @param outputStream 流
     */
    private void writeWorkbook(Workbook workbook, OutputStream outputStream) {
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(outputStream)) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将数据写入到表格的行中
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param dataMapList 数据
     */
    private void putDataMapToRow(Workbook workbook, Sheet sheet, List<LinkedHashMap<String, Serializable>> dataMapList) {
        int i = DEFAULT_FIRST_DATA_ROW_INDEX;
        // 数据行的样式
        CellStyle style = generateCellStyle(workbook, false);
        for (LinkedHashMap<String, Serializable> dataMap : dataMapList) {
            Row row = sheet.createRow(i++);
            // 将数据写入到表格的单元格中
            putDataToCell(workbook, sheet, row, style, dataMap);
        }
    }

    /**
     * 将数据写入到表格的单元格中
     *
     * @param workbook workbook
     * @param sheet    sheet
     * @param row      行
     * @param style    样式
     * @param dataMap  数据
     */
    private void putDataToCell(Workbook workbook, Sheet sheet, Row row, CellStyle style, Map<String, Serializable> dataMap) {
        int index = 0;
        for (Map.Entry<String, Serializable> entry : dataMap.entrySet()) {
            Serializable value = entry.getValue();
            if (value instanceof ExcelPictureTemplateModel) {
                // 向单元格写入图片
                putPictureToCell(workbook, sheet, row, style, index, (ExcelPictureTemplateModel) value);
            } else {
                // 向单元格写入数据
                putDataToCell(row, style, index, value);
            }
            index++;
        }

    }

    /**
     * 向单元格写入图片
     *
     * @param workbook             workbook
     * @param sheet                sheet
     * @param row                  行
     * @param style                样式
     * @param column               列索引
     * @param pictureTemplateModel 图片信息对象
     */
    private void putPictureToCell(Workbook workbook, Sheet sheet, Row row, CellStyle style, int column, ExcelPictureTemplateModel pictureTemplateModel) {
        // 获取图片,并将图片写入workbook中
        int pictureIdx;
        try (InputStream is = new FileInputStream(pictureTemplateModel.getPath())) {
            pictureIdx = workbook.addPicture(IOUtils.toByteArray(is), Workbook.PICTURE_TYPE_PNG);
        } catch (Exception e) {
            e.printStackTrace();
            putDataToCell(row, style, column, "图片未找到");
            return;
        }
        // 获取图片写出助手对象
        CreationHelper helper = workbook.getCreationHelper();
        // 创建图片画笔
        Drawing drawing = sheet.createDrawingPatriarch();
        // 添加图片操作对象
        ClientAnchor anchor = helper.createClientAnchor();
        // 设置图片所在的行和列位置和所在单元格的大小
        row.setHeightInPoints(pictureTemplateModel.getHeight());
        sheet.setColumnWidth(column, pictureTemplateModel.getWidth());
        anchor.setCol1(column);
        anchor.setRow1(row.getRowNum());
        // 插入图片
        Picture picture = drawing.createPicture(anchor, pictureIdx);
        // 调整图片大小
        picture.resize(pictureTemplateModel.getScaleX(), pictureTemplateModel.getScaleY());
    }

    /**
     * 设置头部行标题数据
     *
     * @param workbook    workbook
     * @param sheet       sheet
     * @param dataMapList 数据
     */
    private void putDataMapKeyToHeader(Workbook workbook, Sheet sheet, List<LinkedHashMap<String, Serializable>> dataMapList) {
        // 创建头部行
        Row headerRow = sheet.createRow(HEADER_ROW_INDEX);
        // 头部行样式
        CellStyle style = generateCellStyle(workbook, true);
        // 头部行标题数据
        Map<String, Serializable> dataMap = dataMapList.get(0);
        int index = 0;
        for (String title : dataMap.keySet()) {
            putDataToCell(headerRow, style, index++, title);
        }
    }

    /**
     * 向单元格写入数据
     *
     * @param row    行
     * @param style  样式
     * @param column 列索引
     * @param value  值
     */
    private void putDataToCell(Row row, CellStyle style, int column, Serializable value) {
        Cell cell = row.createCell(column, CellType.STRING);
        cell.setCellStyle(style);
        if (Objects.nonNull(value)) {
            cell.setCellValue(String.valueOf(value));
        } else {
            cell.setCellValue("");
        }
    }

    /**
     * 生成样式
     *
     * @param workbook workbook
     * @param isHeader 是否是首行
     * @return 样式
     */
    private CellStyle generateCellStyle(Workbook workbook, boolean isHeader) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setFontName(DEFAULT_FONT_NAME);
        font.setFontHeightInPoints((short) DEFAULT_FONT_HEIGHT);
        if (isHeader) {
            // 首行样式
            font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
            font.setBold(true);
        } else {
            font.setColor(HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.getIndex());
        }

        style.setFont(font);
        return style;
    }

    /**
     * 初始化sheet页
     *
     * @param workbook workbook
     * @return sheet
     */
    private Sheet initSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet();
        // 设置单元格默认宽高
        sheet.setDefaultColumnWidth(DEFAULT_CELL_WIDTH);
        sheet.setDefaultRowHeightInPoints(DEFAULT_CELL_HEIGHT);
        return sheet;
    }


    /**
     * 根据需要生成的Excel类型获取Workbook
     *
     * @param type Excel类型
     * @return workbook
     */
    private Workbook getWorkbook(ExcelTypeEnum type) {
        Workbook workbook = null;
        switch (type) {
            case XLS:
                workbook = new HSSFWorkbook();
                break;
            case XLSX:
                workbook = new XSSFWorkbook();
                break;
            default:
                break;
        }
        return workbook;
    }

}
