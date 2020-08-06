package com.example.demo.office.excel.example;

import com.example.demo.component.AuthComponent;
import com.example.demo.component.exception.ExcelException;
import com.example.demo.component.exception.ServiceException;
import com.example.demo.dao.sys.SysUserDao;
import com.example.demo.entity.sys.SysUserDO;
import com.example.demo.manager.file.UploadFileRequest;
import com.example.demo.manager.log.ExceptionLogRequest;
import com.example.demo.office.excel.format.ExcelNumericFormat;
import com.example.demo.office.excel.input.ExcelReader;
import com.example.demo.office.excel.principal.AbstractUserExcelTemplateModel;
import com.example.demo.office.excel.principal.AbstractUserImportHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author Administrator
 * @date 2020-07-22 17:23
 * @description: excel导入例子
 */
@Component
public class ExampleImportHandler extends AbstractUserImportHandler {

    private SysUserDao sysUserDao;
    private AuthComponent authComponent;

    @Autowired
    protected ExampleImportHandler(ExcelReader excelReader, ExcelNumericFormat excelNumericFormat,
                                   UploadFileRequest uploadFileRequest, HttpServletRequest request,
                                   ExceptionLogRequest exceptionLogRequest, SysUserDao sysUserDao,
                                   AuthComponent authComponent) {
        super(excelReader, excelNumericFormat, uploadFileRequest, request, exceptionLogRequest);
        this.sysUserDao = sysUserDao;
        this.authComponent = authComponent;
    }

    @Override
    protected AbstractUserExcelTemplateModel getExcelTemplate(ExcelReader excelReader, ExcelNumericFormat excelNumericFormat) {
        return new ExampleTemplateModel(excelNumericFormat);
    }

    @Override
    protected SysUserDO newDataObject() {
        return new SysUserDO();
    }

    @Override
    protected boolean saveMidObject(SysUserDO dataObject, Object midObject) throws ExcelException {
        dataObject.setCreateTime(LocalDateTime.now());
        dataObject.setUpdateTime(LocalDateTime.now());
        dataObject.setEnable(1);
        try {
            dataObject.setCreateUserId(authComponent.getPrimaryPrincipal(Long.class));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        if (sysUserDao.save(dataObject) > 0) {
            return true;
        }
        throw new ExcelException("保存对象失败");
    }
}
