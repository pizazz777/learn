package com.example.demo.util.pdf;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.constant.file.FileTypeEnum;
import com.example.demo.entity.upload.UploadFileDO;
import com.example.demo.manager.file.UploadFileRequest;
import com.example.demo.properties.OfficeProperties;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.*;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import freemarker.template.TemplateException;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Objects;


/**
 * @author hxx
 * @date 2020/07/17
 * @description: 类描述: 将其它 office文件转为 pdf
 **/
@Component
public class DefaultPdfWriter implements PdfWriter {

    private UploadFileRequest uploadFileRequest;
    private OfficeDocumentConverter officeDocumentConverter;
    private OfficeProperties officeProperties;
    private TemplateHandler templateHandler;
    /**
     * html to pdf 上下文
     */
    private HtmlPipelineContext htmlPipelineContext;

    @Autowired
    public DefaultPdfWriter(UploadFileRequest uploadFileRequest, OfficeDocumentConverter officeDocumentConverter, OfficeProperties officeProperties, TemplateHandler templateHandler) {
        this.uploadFileRequest = uploadFileRequest;
        this.officeDocumentConverter = officeDocumentConverter;
        this.officeProperties = officeProperties;
        this.templateHandler = templateHandler;
    }

    @Override
    public UploadFileDO officeToPdf(File inputFile) throws ServiceException {
        // 生成一个空文件
        UploadFileDO uploadFileDO = uploadFileRequest.generateEmptyFile(FileTypeEnum.PDF.getSuffix());
        File outputFile = new File(uploadFileDO.getPath());
        try {
            // 进行转换
            officeDocumentConverter.convert(inputFile, outputFile);
            return uploadFileDO;
        } catch (OfficeException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void htmlToPdf(String htmlContent, OutputStream outputStream) throws Exception {
        // 注册字体
        FontFactory.registerDirectories();
        // 文档
        Document document = new Document(PageSize.A4);
        // pdf 写出器
        com.itextpdf.text.pdf.PdfWriter pdfWriter = com.itextpdf.text.pdf.PdfWriter.getInstance(document, outputStream);
        // html管道上下文
        HtmlPipelineContext htmlPipelineContext = getHtmlPipelineContext();
        // css处理器
        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        // 管道
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlPipelineContext, new PdfWriterPipeline(document, pdfWriter)));
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser xmlParser = new XMLParser(worker);
        // 写出文档
        document.open();
        parseToDocument(xmlParser, document, htmlContent);
        document.close();
    }

    private void parseToDocument(XMLParser xmlParser, Document document, String htmlContent) throws IOException, TemplateException, ServiceException {
        HtmlToPdfTemplateVO htmlToPdfTemplateVO = new HtmlToPdfTemplateVO();
        htmlToPdfTemplateVO.setHtmlContent(htmlContent);
        try (InputStream inputStream = new ByteArrayInputStream(templateHandler.getHtmlString(htmlToPdfTemplateVO, officeProperties.getPdf().getHtmlToPdfTemplateName()).getBytes())) {
            xmlParser.parse(inputStream);
            document.newPage();
        }
    }

    /**
     * 获取html to pdf 上下文
     */
    private HtmlPipelineContext getHtmlPipelineContext() {
        if (Objects.isNull(htmlPipelineContext)) {
            // css 填充器
            CssAppliers cssAppliers = new CssAppliersImpl(new XMLWorkerFontProvider(getFontsUrl()));
            htmlPipelineContext = new HtmlPipelineContext(cssAppliers);
            // 标签处理器 支持 base64
            TagProcessorFactory tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
            // 默认是com.itextpdf.tool.xml.html.Image |自个定义一个image的处理类
            tagProcessorFactory.addProcessor(new Image(), HTML.Tag.IMG);
            htmlPipelineContext.setTagFactory(tagProcessorFactory);
        }
        return htmlPipelineContext;
    }


    private String getFontsUrl() {
        String pdfFondsRootPath = officeProperties.getPdf().getPdfFontsPath();
        File fontDir = new File(pdfFondsRootPath);
        if (!fontDir.exists()) {
            fontDir.getParentFile().mkdirs();
        }
        return pdfFondsRootPath;
    }

}
