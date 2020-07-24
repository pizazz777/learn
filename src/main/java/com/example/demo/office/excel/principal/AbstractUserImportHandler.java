package com.example.demo.office.excel.principal;

import com.example.demo.entity.sys.SysUserDO;
import com.example.demo.manager.file.UploadFileRequest;
import com.example.demo.manager.log.ExceptionLogRequest;
import com.example.demo.office.excel.format.ExcelNumericFormat;
import com.example.demo.office.excel.handler.DefaultImportHandler;
import com.example.demo.office.excel.input.ExcelReader;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * @date 2020-07-23 17:19
 * @description: 处理用户信息导出类
 */
public abstract class AbstractUserImportHandler extends DefaultImportHandler<SysUserDO, Object> {

    protected AbstractUserImportHandler(ExcelReader excelReader, ExcelNumericFormat excelNumericFormat,
                                        UploadFileRequest uploadFileRequest, HttpServletRequest request,
                                        ExceptionLogRequest exceptionLogRequest) {
        super(excelReader, excelNumericFormat, uploadFileRequest, exceptionLogRequest, request);
    }

}
