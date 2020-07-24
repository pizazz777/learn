package com.example.demo.office.excel.input;

import com.example.demo.component.exception.ExcelException;
import com.example.demo.constant.office.ExcelTypeEnum;
import com.example.demo.office.excel.model.ExcelWorkbook;
import com.example.demo.util.exception.ExceptionUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Administrator
 * @date 2020-07-22 16:02
 * @description: excel 读入类
 */
public class DefaultExcelReader implements ExcelReader {


    /**
     * 读取excel
     *
     * @param file 上传的文件对象
     * @return 读取的Excel信息
     * @throws ExcelException e
     * @throws IOException    e
     */
    @Override
    public ExcelWorkbook read(MultipartFile file) throws ExcelException, IOException {
        ExceptionUtil.requireNonNull(file, new ExcelException("导出文件为空"));
        String filename = file.getOriginalFilename();
        ExcelTypeEnum type = ExcelTypeEnum.getType(filename);
        return read(file.getInputStream(), type);
    }

    /**
     * 从Excel文件中读取
     *
     * @param file excel文件
     * @return 读取的Excel信息
     * @throws ExcelException e
     * @throws IOException    e
     */
    @Override
    public ExcelWorkbook read(File file) throws ExcelException, IOException {
        ExcelTypeEnum type = ExcelTypeEnum.getType(file);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return read(inputStream, type);
        }
    }

    /**
     * 从流中读取
     *
     * @param inputStream 流
     * @param type        想要得到的excel类型
     * @return 读取的Excel信息
     * @throws ExcelException e
     * @throws IOException    e
     */
    @Override
    public ExcelWorkbook read(InputStream inputStream, ExcelTypeEnum type) throws ExcelException, IOException {
        // 获取Workbook
        Workbook workbook = getWorkBook(inputStream, type);
        // 获取工作簿的公式计算器
        FormulaEvaluator evaluator = getFormulaEvaluator(workbook);
        // excel信息
        return ExcelWorkbook.builder()
                .type(type)
                .workbook(workbook)
                .formulaEvaluator(evaluator)
                .sheet(workbook.getSheetAt(0))
                .build();
    }

    /**
     * 获取工作簿的公式计算器
     *
     * @param workbook workbook
     * @return 公式计算器
     * @throws ExcelException e
     */
    private FormulaEvaluator getFormulaEvaluator(Workbook workbook) throws ExcelException {
        FormulaEvaluator evaluator = null;
        if (workbook instanceof HSSFWorkbook) {
            evaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
        }
        if (workbook instanceof XSSFWorkbook) {
            evaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
        }
        ExceptionUtil.requireNonNull(evaluator, new ExcelException("无法获取公式计算器"));
        return evaluator;
    }

    /**
     * 获取Workbook
     *
     * @param inputStream 输入流
     * @param type        类型
     * @return Workbook
     * @throws IOException e
     */
    private Workbook getWorkBook(InputStream inputStream, ExcelTypeEnum type) throws IOException {
        Workbook workbook;
        switch (type) {
            case XLS:
                workbook = new HSSFWorkbook(inputStream);
                break;
            case XLSX:
                workbook = new XSSFWorkbook(inputStream);
                break;
            default:
                throw new IOException("不是excel文件");
        }
        return workbook;
    }

}
