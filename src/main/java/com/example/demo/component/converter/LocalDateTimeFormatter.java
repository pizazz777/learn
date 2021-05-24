package com.example.demo.component.converter;

import com.huang.util.time.DateConst;
import com.huang.util.time.DateUtil;
import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author Administrator
 * @date 2020/04/31
 * @description: 类描述: 在 HTTP 请求中 字符串转换成 LocalDateTime
 **/
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

    @Override
    @Nonnull
    public LocalDateTime parse(@Nonnull String text, @Nonnull Locale locale) {
        return DateUtil.getDateTimeByString(text);
    }

    @Override
    @NonNull
    public String print(@NonNull LocalDateTime object, @Nonnull Locale locale) {
        return object.format(DateTimeFormatter.ofPattern(DateConst.DEFAULT_DATE_FORMAT));
    }
}
