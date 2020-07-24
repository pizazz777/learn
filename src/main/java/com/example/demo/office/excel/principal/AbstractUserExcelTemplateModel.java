package com.example.demo.office.excel.principal;

import com.example.demo.component.exception.ExcelException;
import com.example.demo.entity.sys.SysUserDO;
import com.example.demo.office.excel.format.ExcelNumericFormat;
import com.example.demo.office.excel.model.ExcelTemplateModel;
import com.example.demo.office.excel.model.ExcelWorkbook;
import com.example.demo.properties.AuthProperties;
import com.example.demo.util.SpringContextHandler;
import com.example.demo.util.str.StrUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-07-23 16:10
 */
@Data
public abstract class AbstractUserExcelTemplateModel<M extends AbstractUserExcelTemplateModel, T extends SysUserDO> implements ExcelTemplateModel<T> {

    /**
     * 账号 表头标题顺序
     */
    private Integer accountIndex;
    /**
     * 姓名 表头标题顺序
     */
    protected Integer usernameIndex;
    /**
     * 手机号码 表头标题顺序
     */
    protected Integer mobileIndex;
    /**
     * 邮箱 表头标题顺序
     */
    protected Integer emailIndex;

    protected ExcelNumericFormat format;

    public AbstractUserExcelTemplateModel(ExcelNumericFormat format) {
        this.format = format;
    }

    /**
     * 设置模板的用户信息
     *
     * @param templateModel 模板对象
     * @param columnIndex   列索引
     * @param cellValue     单元格值
     * @throws ExcelException e
     */
    public void setTemplateModel(M templateModel, int columnIndex, String cellValue) throws ExcelException {
        switch (cellValue) {
            case "账号":
                templateModel.setAccountIndex(columnIndex);
                break;
            case "姓名":
                templateModel.setUsernameIndex(columnIndex);
                break;
            case "手机号码":
                templateModel.setMobileIndex(columnIndex);
                break;
            case "邮箱":
                templateModel.setEmailIndex(columnIndex);
                break;
            default:
                setOtherTemplateModel(templateModel, columnIndex, cellValue);
                break;
        }
    }

    /**
     * 设置模板除用户信息外的其他信息(需要的时候子类去实现)
     *
     * @param templateModel 模板对象
     * @param columnIndex   列索引
     * @param cellValue     单元格值
     * @throws ExcelException e
     */
    protected abstract void setOtherTemplateModel(M templateModel, int columnIndex, String cellValue) throws ExcelException;

    /**
     * 将单元格的值放入对象
     *
     * @param workbook workbook
     * @param cell     单元格
     * @param object   对象
     * @throws ExcelException e
     */
    @Override
    public void putCellValueToDO(ExcelWorkbook workbook, Cell cell, T object) throws ExcelException {
        int columnIndex = cell.getColumnIndex();
        String cellValue = format.getCellValue(cell, workbook.getFormulaEvaluator());
        setDataObject(object, columnIndex, cellValue);
    }

    /**
     * 获取头部行索引集合
     *
     * @return r
     */
    public abstract int[] getHeadRowIndexList();

    /**
     * 获取头部行大小
     *
     * @return r
     */
    public abstract int getHeadRowSize();

    /**
     * 设置用户信息
     *
     * @param object      对象
     * @param columnIndex 索引
     * @param cellValue   值
     * @throws ExcelException e
     */
    private void setDataObject(T sysUserDO, int columnIndex, String cellValue) throws ExcelException {
        if (Objects.equals(columnIndex, accountIndex)) {
            sysUserDO.setAccount(cellValue);
        } else if (Objects.equals(columnIndex, emailIndex)) {
            sysUserDO.setEmail(cellValue);
        } else if (Objects.equals(columnIndex, usernameIndex)) {
            sysUserDO.setUsername(cellValue);
        } else if (Objects.equals(columnIndex, mobileIndex)) {
            verifierMobile(cellValue);
            sysUserDO.setMobile(cellValue);
        } else {
            setOtherDataObject(sysUserDO, columnIndex, cellValue);
        }
    }

    /**
     * 设置其它值
     *
     * @param object      对象
     * @param columnIndex 索引
     * @param cellValue   值
     * @throws ExcelException e
     */
    protected abstract void setOtherDataObject(T object, int columnIndex, String cellValue) throws ExcelException;

    /**
     * 校验手机号码
     *
     * @param cellValue 手机号码
     * @throws ExcelException e
     */
    private void verifierMobile(String cellValue) throws ExcelException {
        AuthProperties authProperties = SpringContextHandler.getBean(AuthProperties.class);
        if (StringUtils.isNotBlank(cellValue) && authProperties.getCheckMobile() && !StrUtil.isMobile(cellValue)) {
            throw new ExcelException("手机号格式错误");
        }
    }

}
