package com.example.demo.office.excel.handler;

import com.example.demo.component.exception.ExcelException;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.component.exception.UploadException;
import com.example.demo.entity.sys.SysUserDO;
import com.example.demo.entity.upload.UploadFileDO;
import com.example.demo.manager.file.UploadFileRequest;
import com.example.demo.manager.log.ExceptionLogRequest;
import com.example.demo.office.excel.format.ExcelNumericFormat;
import com.example.demo.office.excel.input.ExcelReader;
import com.example.demo.office.excel.model.ExcelWorkbook;
import com.example.demo.office.excel.model.ImportResult;
import com.example.demo.office.excel.principal.AbstractUserExcelTemplateModel;
import com.example.demo.util.excel.ExcelUtil;
import com.example.demo.util.exception.ExceptionUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Administrator
 * @date 2020-07-22 16:59
 * @description: 导入处理类
 */
@Slf4j
public abstract class DefaultImportHandler<D extends SysUserDO, O> implements ImportHandler<D, O> {

    private ExcelReader excelReader;
    private ExcelNumericFormat excelNumericFormat;
    private UploadFileRequest uploadFileRequest;
    private ExceptionLogRequest exceptionLogRequest;
    private HttpServletRequest request;

    protected DefaultImportHandler(ExcelReader excelReader, ExcelNumericFormat excelNumericFormat,
                                   UploadFileRequest uploadFileRequest, ExceptionLogRequest exceptionLogRequest,
                                   HttpServletRequest request) {
        this.excelReader = excelReader;
        this.excelNumericFormat = excelNumericFormat;
        this.uploadFileRequest = uploadFileRequest;
        this.exceptionLogRequest = exceptionLogRequest;
        this.request = request;
    }

    /**
     * 导出
     *
     * @param file 上传的文件对象
     * @return 导入结果
     * @throws ExcelException e
     */
    @Override
    public ImportResult<D> handler(MultipartFile file) throws ExcelException, IOException {
        return handler(file, null);
    }

    /**
     * 导出
     *
     * @param file           上传的文件对象
     * @param originalObject 业务对象
     * @return 导入结果
     * @throws ExcelException e
     */
    @Override
    public ImportResult<D> handler(MultipartFile file, O originalObject) throws ExcelException, IOException {
        ExcelWorkbook workbook = excelReader.read(file);
        return handler(workbook, originalObject);
    }

    /**
     * 导出
     *
     * @param file 文件对象
     * @return 导入结果
     * @throws ExcelException e
     */
    @Override
    public ImportResult<D> handler(File file) throws ExcelException, IOException {
        return handler(file, null);
    }

    /**
     * 导出
     *
     * @param file           上传的文件对象
     * @param originalObject 业务对象
     * @return 导入结果
     * @throws ExcelException e
     */
    @Override
    public ImportResult<D> handler(File file, O originalObject) throws ExcelException, IOException {
        ExcelWorkbook workbook = excelReader.read(file);
        return handler(workbook, originalObject);
    }

    /**
     * 导出处理类
     *
     * @param workbook       excel信息对象
     * @param originalObject 业务对象
     * @return r
     * @throws ExcelException e
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public ImportResult<D> handler(ExcelWorkbook workbook, O originalObject) throws ExcelException {
        // 校验excel
        checkExcelWorkbook(workbook);
        // 获取excel信息
        Sheet sheet = workbook.getSheet();
        // 获取模板
        AbstractUserExcelTemplateModel template = getExcelTemplate(excelReader, excelNumericFormat);
        // 获取头部行
        List<Row> headRowList = getHeadRowList(sheet, template.getHeadRowIndexList());
        // 将模板excel头部行信息设置到模板中
        setExcelHeadRow(workbook.getFormulaEvaluator(), template, headRowList);
        return importExcel(workbook, originalObject, sheet, template);
    }

    /**
     * 导入
     *
     * @param workbook       workbook
     * @param originalObject 业务对象
     * @param sheet          sheet页
     * @param template       模板对象
     * @return r
     * @throws ExcelException e
     */
    @SuppressWarnings("unchecked")
    private ImportResult importExcel(ExcelWorkbook workbook, O originalObject, Sheet sheet, AbstractUserExcelTemplateModel template) throws ExcelException {
        // 导入的结果
        ImportResult<D> result = new ImportResult<>();
        // 导入成功的对象集合
        List<D> importList = Lists.newArrayList();
        // 记录需要删除的行号
        List<Integer> deleteRowNumList = Lists.newArrayList();
        // 错误信息样式
        CellStyle errStyle = createErrMsgCellStyle(workbook.getWorkbook());
        // 头部行数量
        int headRowSize = template.getHeadRowSize();
        // 设置总行数
        int rowCount = sheet.getLastRowNum();
        result.setTotalCount(rowCount + 1 - headRowSize);
        for (int rowIndex = headRowSize; rowIndex < rowCount + 1; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            // 数据行
            D object = newDataObject();
            try {
                for (Cell cell : row) {
                    // 将单元格的值放入对象
                    template.putCellValueToDO(workbook, cell, object);
                }
                if (saveMidObject(object, originalObject)) {
                    importList.add(object);
                    // 读取成功后记录行号
                    deleteRowNumList.add(rowIndex);
                }
            } catch (ExcelException e) {
                createErrMsgCell(row, errStyle, e);
            }
        }
        result.setSuccessList(importList);
        // 删除保存成功的行号
        ExcelUtil.deleteSheetRow(sheet, deleteRowNumList);
        // 判断删除导入成功后的sheet是否只剩下头部行,若不是则将 workbook 导出到文件并提供下载方式
        if (!Objects.equals(sheet.getLastRowNum(), headRowSize - 1)) {
            setFailResult(workbook, result);
        }
        return result;
    }

    protected abstract <V extends AbstractUserExcelTemplateModel> V getExcelTemplate(ExcelReader excelReader, ExcelNumericFormat excelNumericFormat);

    protected abstract D newDataObject();

    protected abstract boolean saveMidObject(D dataObject, O midObject) throws ExcelException;

    /**
     * 获取头部行
     *
     * @param sheet            sheet
     * @param headRowIndexList 头部行索引
     * @return 头部行
     * @throws ExcelException e
     */
    private List<Row> getHeadRowList(Sheet sheet, int[] headRowIndexList) throws ExcelException {
        ExceptionUtil.requireNonNull(headRowIndexList, new ExcelException("未设置模板头部行"));
        List<Row> headRowList = Lists.newArrayList();
        for (int headRowIndex : headRowIndexList) {
            Row headRow = Optional.ofNullable(sheet.getRow(headRowIndex)).orElseThrow(() -> new ExcelException("未找到模板头部行"));
            headRowList.add(headRow);
        }
        return headRowList;
    }

    /**
     * 校验excel
     *
     * @param excelWorkbook excel信息
     * @throws ExcelException e
     */
    private void checkExcelWorkbook(ExcelWorkbook excelWorkbook) throws ExcelException {
        Optional.ofNullable(excelWorkbook.getFormulaEvaluator()).orElseThrow(() -> new ExcelException("Excel 文件为空"));
        Optional.ofNullable(excelWorkbook.getWorkbook()).orElseThrow(() -> new ExcelException("Excel 文件为空"));
    }

    /**
     * 创建错误信息单元格
     *
     * @param row   行
     * @param style 样式
     * @param e     异常
     */
    private void createErrMsgCell(Row row, CellStyle errStyle, ExcelException e) {
        if (log.isDebugEnabled()) {
            log.error("Excel 生成异常", e);
        }
        // 把生成的错误消息放到最尾列+1
        Cell errMsgCell = row.createCell(row.getLastCellNum() + 1);
        errMsgCell.setCellStyle(errStyle);
        errMsgCell.setCellValue(e.getMessage());
    }

    /**
     * 创建错误信息单元格的样式
     *
     * @param workbook workbook
     * @return 样式
     */
    private CellStyle createErrMsgCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        // 错误信息设置为红色
        font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        int fontSize = workbook.getNumberOfFontsAsInt();
        if (fontSize > 0) {
            font.setFontHeight(workbook.getFontAt(0).getFontHeight());
        } else {
            font.setFontHeightInPoints((short) 12);
        }
        style.setFont(font);
        return style;
    }

    /**
     * 设置导入失败的信息
     *
     * @param excelWorkbook excel信息对象
     * @param importResult  导入结果
     * @throws ExcelException e
     */
    private void setFailResult(ExcelWorkbook excelWorkbook, ImportResult<D> importResult) throws ExcelException {
        // 生成一个空的excel文件
        UploadFileDO emptyFile;
        try {
            emptyFile = uploadFileRequest.generateEmptyFile(excelWorkbook.getType().getSuffix());
        } catch (UploadException e) {
            throw new ExcelException("生成空文件失败");
        }
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(emptyFile.getPath())))) {
            excelWorkbook.getWorkbook().write(outputStream);
        } catch (IOException e) {
            try {
                exceptionLogRequest.saveExceptionLog(request, e);
            } catch (ServiceException ex) {
                ex.printStackTrace();
            }
        }
        importResult.setFailedExcelFile(emptyFile);
    }

    /**
     * 将模板excel头部行信息设置到模板中
     *
     * @param evaluator     公式计算器
     * @param templateModel 模板对象
     * @param headRowList   头部行列表
     * @param <M>           扩展模板
     * @throws ExcelException e
     */
    @SuppressWarnings("unchecked")
    private <M extends AbstractUserExcelTemplateModel> void setExcelHeadRow(FormulaEvaluator evaluator, M templateModel, List<Row> headRowList) throws ExcelException {
        for (Row headRow : headRowList) {
            for (Cell cell : headRow) {
                // 获取单元格值
                String cellValue = excelNumericFormat.getCellValue(cell, evaluator);
                // 设置模板的用户信息
                templateModel.setTemplateModel(templateModel, cell.getColumnIndex(), cellValue);
            }
        }
    }

}
