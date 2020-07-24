package com.example.demo.office.excel.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.demo.entity.upload.UploadFileDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author administrator
 * @date 2020/07/22
 * @description: Excel模板导入数据结果对象
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportResult<D> {

    /**
     * 数据总行数
     */
    private Integer totalCount;
    /**
     * 导入成功的数据列表
     */
    private List<D> successList;
    /**
     * 导入后的 workbook(将导入成功的行shiftRows(rowIndex+1,lastRowNum,-1)),剩下的未导入成功的workbook
     */
    @JSONField(serialize = false)
    private ExcelWorkbook excelWorkbook;
    /**
     * 导入失败保存的上传文件对象
     */
    private UploadFileDO failedExcelFile;
}
