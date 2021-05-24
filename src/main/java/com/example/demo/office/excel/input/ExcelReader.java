package com.example.demo.office.excel.input;

import com.example.demo.constant.office.ExcelTypeEnum;
import com.example.demo.office.excel.model.ExcelWorkbook;
import com.huang.exception.ExcelException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Administrator
 * @date 2020-07-22 15:57
 * @description: excel 读入类
 */
public interface ExcelReader {

    /**
     * 读取excel
     *
     * @param file 上传的文件对象
     * @return 读取的Excel信息
     * @throws ExcelException e
     * @throws IOException    e
     */
    ExcelWorkbook read(MultipartFile file) throws ExcelException, IOException;

    /**
     * 读取excel
     *
     * @param file excel文件
     * @return 读取的Excel信息
     * @throws ExcelException e
     * @throws IOException    e
     */
    ExcelWorkbook read(File file) throws ExcelException, IOException;

    /**
     * 读取excel
     *
     * @param inputStream 流
     * @param type        想要得到的excel类型
     * @return 读取的Excel信息
     * @throws ExcelException e
     * @throws IOException    e
     */
    ExcelWorkbook read(InputStream inputStream, ExcelTypeEnum type) throws ExcelException, IOException;

}
