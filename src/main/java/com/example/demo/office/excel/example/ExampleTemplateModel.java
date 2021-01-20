package com.example.demo.office.excel.example;

import com.example.demo.component.exception.ExcelException;
import com.example.demo.entity.sys.SysUserDO;
import com.example.demo.office.excel.format.ExcelNumericFormat;
import com.example.demo.office.excel.principal.AbstractUserExcelTemplateModel;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-07-22 17:24
 */
@Setter
@EqualsAndHashCode(callSuper = true)
public class ExampleTemplateModel extends AbstractUserExcelTemplateModel<ExampleTemplateModel, SysUserDO> {

    private Integer titleIndex;
    private Integer levelIndex;
    private Integer descriptionIndex;
    private Integer managerIndex;
    private Integer sexIndex;

    /**
     * 头部行行号
     */
    private static final int[] HEAD_ROW_INDEX_LIST = {0};
    /**
     * 头部行总行数
     */
    private static final int HEAD_ROW_SIZE = 1;

    public ExampleTemplateModel(ExcelNumericFormat format) {
        super(format);
    }

    /**
     * 设置模板除用户信息外的其他信息(需要的时候子类去实现)
     *
     * @param templateModel 模板对象
     * @param columnIndex   列索引
     * @param cellValue     单元格值
     */
    @Override
    protected void setOtherTemplateModel(ExampleTemplateModel templateModel, int columnIndex, String cellValue) {
        switch (cellValue) {
            case "职务名称":
                templateModel.setTitleIndex(columnIndex);
                break;
            case "级别":
                templateModel.setLevelIndex(columnIndex);
                break;
            case "描述":
                templateModel.setDescriptionIndex(columnIndex);
                break;
            case "是否超级管理员":
                templateModel.setManagerIndex(columnIndex);
                break;
            case "性别":
                templateModel.setSexIndex(columnIndex);
                break;
            default:
                break;
        }
    }

    /**
     * 获取头部行索引集合
     *
     * @return r
     */
    @Override
    public int[] getHeadRowIndexList() {
        return HEAD_ROW_INDEX_LIST;
    }

    /**
     * 获取头部行大小
     *
     * @return r
     */
    @Override
    public int getHeadRowSize() {
        return HEAD_ROW_SIZE;
    }

    /**
     * 设置其它值
     *
     * @param sysUser     对象
     * @param columnIndex 索引
     * @param cellValue   值
     * @throws ExcelException e
     */
    @Override
    protected void setOtherDataObject(SysUserDO sysUser, int columnIndex, String cellValue) throws ExcelException {
        if (Objects.equals(columnIndex, titleIndex)) {
            sysUser.setMobile(cellValue);
        } else if (Objects.equals(columnIndex, levelIndex)) {
            sysUser.setLevel(Integer.valueOf(cellValue));
        } else if (Objects.equals(columnIndex, descriptionIndex)) {
            sysUser.setDescription(cellValue);
        } else if (Objects.equals(columnIndex, managerIndex)) {
            sysUser.setManager(Integer.valueOf(cellValue));
        } else if (Objects.equals(columnIndex, sexIndex)) {
            sysUser.setSex(Integer.valueOf(cellValue));
        }
    }

}
