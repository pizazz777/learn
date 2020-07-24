package com.example.demo.util.pdf;

import com.example.demo.component.exception.ServiceException;
import com.google.common.base.Charsets;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author administrator
 * @date 2020/07/21
 * @description: 类描述: pdf 工具类
 **/
public class PdfUtil {

    private PdfUtil() {
    }

    /**
     * Content Type
     */
    public static final String CONTENT_TYPE = "application/pdf";

    /**
     * 设置 pdf 文件流响应属性
     *
     * @param response HTTP响应对象
     * @param fileName Excel 文件名
     * @param download true:下载框,false:内嵌显示
     */
    public static void setResponse(HttpServletResponse response, String fileName, boolean download) {
        // 设置响应头
        fileName = StringUtils.isNotBlank(fileName) ? new String(fileName.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1) : fileName;
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        // attachment:浏览器弹出下载框  inline:浏览器内嵌显示
        response.setHeader("Content-disposition", (download ? "attachment" : "inline") + ";filename=" + fileName);
        response.setContentType(CONTENT_TYPE);
    }


    /**
     * 设置 Word 文件流响应属性
     *
     * @param response HTTP响应对象
     * @param fileName Excel 文件名
     */
    public static void setResponseWithUrlEncoding(HttpServletResponse response, String fileName) throws ServiceException {
        try {
            setResponse(response, URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()), true);
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
