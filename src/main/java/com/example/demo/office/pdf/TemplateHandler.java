package com.example.demo.office.pdf;

import com.example.demo.office.pdf.model.TemplateVO;
import com.example.demo.properties.OfficeProperties;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;

/**
 * @author administrator
 * @date 2020/07/17
 * @description: 类描述: 模板处理器
 **/
@Component
public class TemplateHandler {

    private OfficeProperties officeProperties;

    @Autowired
    public TemplateHandler(OfficeProperties officeProperties) {
        this.officeProperties = officeProperties;
    }

    /**
     * freemarker config
     */
    protected Configuration configuration;

    /**
     * 获取模板生成的 html 页面
     *
     * @param templateVO   模板对象
     * @param templateName 模板名称
     * @return HTML
     * @throws IOException       e
     * @throws TemplateException e
     */
    public String getHtmlString(TemplateVO templateVO, String templateName) throws IOException, TemplateException {
        Template template = getConfiguration().getTemplate(templateName);
        try (StringWriter writer = new StringWriter()) {
            template.process(templateVO, writer);
            writer.flush();
            return writer.toString();
        }
    }

    /**
     * 设置/获取模板设置
     *
     * @return 模板配置
     * @throws IOException e
     */
    private Configuration getConfiguration() throws IOException {
        if (Objects.isNull(configuration)) {
            File dir = new File(officeProperties.getPdf().getPdfTemplatePath());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileTemplateLoader fileTemplateLoader = new FileTemplateLoader(dir);
            configuration = new Configuration(Configuration.VERSION_2_3_25);
            configuration.setDefaultEncoding("utf-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setLogTemplateExceptions(false);
            configuration.setTemplateLoader(fileTemplateLoader);
        }
        return configuration;
    }
}
