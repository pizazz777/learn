package com.example.demo.configuration;

import com.example.demo.office.excel.format.DefaultExcelNumericFormat;
import com.example.demo.office.excel.format.ExcelNumericFormat;
import com.example.demo.office.excel.input.DefaultExcelReader;
import com.example.demo.office.excel.input.ExcelReader;
import com.example.demo.office.excel.output.DefaultExcelWriter;
import com.example.demo.office.excel.output.ExcelWriter;
import com.example.demo.util.SpringContextHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @date 2020-05-06 11:01
 */
@Configuration
public class AutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public SpringContextHandler springContextHandler() {
        return new SpringContextHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExcelNumericFormat format() {
        return new DefaultExcelNumericFormat();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExcelReader excelReader() {
        return new DefaultExcelReader();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExcelWriter excelWriter() {
        return new DefaultExcelWriter();
    }


}
