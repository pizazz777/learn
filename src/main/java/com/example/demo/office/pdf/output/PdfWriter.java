package com.example.demo.office.pdf.output;

import com.example.demo.entity.upload.UploadFileDO;
import com.huang.exception.ServiceException;

import java.io.File;
import java.io.OutputStream;

/**
 * @author hxx
 * @date 2020/07/17
 * @description: 类描述: 将其它 office文件转为 pdf
 **/
public interface PdfWriter {

    /**
     * office 转 pdf
     *
     * @param file 文件
     * @return UploadFileDO
     * @throws ServiceException e
     */
    UploadFileDO officeToPdf(File file) throws ServiceException;

    /**
     * html转pdf
     *
     * @param htmlContent  html内容
     * @param outputStream 输出流
     * @throws Exception e
     */
    void htmlToPdf(String htmlContent, OutputStream outputStream) throws Exception;

}
