package com.example.demo.office.excel.handler;

import com.example.demo.component.exception.ExcelException;
import com.example.demo.office.excel.model.ImportResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Administrator
 * @date 2020-07-22 16:57
 * @description: 导入逻辑处理类
 */
public interface ImportHandler<D, O> {

    /**
     * 导出
     *
     * @param file 上传的文件对象
     * @return 导入结果
     * @throws ExcelException e
     */
    ImportResult<D> handler(MultipartFile file) throws ExcelException, IOException;

    /**
     * 导出
     *
     * @param file           上传的文件对象
     * @param originalObject 业务对象
     * @return 导入结果
     * @throws ExcelException e
     */
    ImportResult<D> handler(MultipartFile file, O originalObject) throws ExcelException, IOException;

    /**
     * 导出
     *
     * @param file 文件对象
     * @return 导入结果
     * @throws ExcelException e
     */
    ImportResult<D> handler(File file) throws ExcelException, IOException;

    /**
     * 导出
     *
     * @param file           上传的文件对象
     * @param originalObject 业务对象
     * @return 导入结果
     * @throws ExcelException e
     */
    ImportResult<D> handler(File file, O originalObject) throws ExcelException, IOException;

}
