package com.example.demo.component.converter;

import com.example.demo.util.time.DateConst;
import com.example.demo.util.time.DateUtil;
import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author sqm
 * @version 1.0
 * @date 2019/01/31
 * @description: 类描述: 在 HTTP 请求中 字符串转换成 LocalDateTime
 * 注册方式: ConversionService
 **/
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

    @Override
    public LocalDateTime parse(@NonNull String text, @Nullable Locale locale) {
        return DateUtil.getDateTimeByString(text);
    }

    @Override
    public String print(@NonNull LocalDateTime object, @Nullable Locale locale) {
        return object.format(DateTimeFormatter.ofPattern(DateConst.DEFAULT_DATE_FORMAT));
    }
}
