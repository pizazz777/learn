package com.example.demo.util.time;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;

import static java.time.temporal.ChronoField.*;

/**
 * @author sqm
 * @version 1.0
 * @date 2019/02/13
 * @description: 类描述: 时间\日期常量
 **/
public class DateConst {


    private DateConst() {
    }

    /**
     * 项目默认的 format yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 只精确到日期的 format yyyy-MM-dd
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 只精确到日期的 format yyyy/MM/dd
     */
    public static final String DATE_FORMAT_SLASH = "yyyy/MM/dd";

    /**
     * format yyyy-MM
     */
    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";

    /**
     * formatter yyyy-MM-dd
     */
    public static final DateTimeFormatter DEFAULT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * formatter yyyy年MM月dd日
     */
    public static final DateTimeFormatter DEFAULT_DATE_WITH_CHINESE = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    /**
     * formatter yyyy-MM
     */
    public static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * formatter y-M-d
     */
    public static final DateTimeFormatter DATE_SINGLE = DateTimeFormatter.ofPattern("y-M-d");

    /**
     * formatter HH:mm:ss.SSS
     */
    public static final DateTimeFormatter DEFAULT_TIME;

    static {
        DEFAULT_TIME = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ofPattern("HH:mm"))
                .optionalStart()
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .optionalStart()
                .appendFraction(NANO_OF_SECOND, 0, 9, true)
                .toFormatter();
    }

    /**
     * formatter H:m:s.S
     */
    public static final DateTimeFormatter TIME_SINGLE;

    static {
        TIME_SINGLE = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ofPattern("H:m"))
                .optionalStart()
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 1, 2, SignStyle.NORMAL)
                .optionalStart()
                .appendFraction(NANO_OF_SECOND, 0, 9, true)
                .toFormatter();
    }

    /**
     * 项目默认的 formatter yyyy-MM-dd HH:mm:ss
     */
    public static final DateTimeFormatter DEFAULT_DATE_TIME;

    static {
        DEFAULT_DATE_TIME = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DEFAULT_DATE)
                .appendLiteral(" ")
                .append(DEFAULT_TIME)
                .toFormatter();
    }

    /**
     * 项目默认的 formatter yyyy-MM-dd HH:mm:ss
     */
    public static final DateTimeFormatter DATE_TIME_SINGLE;

    static {
        DATE_TIME_SINGLE = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DATE_SINGLE)
                .appendLiteral(" ")
                .append(TIME_SINGLE)
                .toFormatter();
    }

    /**
     * formatter yyyy/MM/dd
     */
    public static final DateTimeFormatter DATE_SLASH = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    /**
     * formatter y/M/d
     */
    public static final DateTimeFormatter DATE_SLASH_SINGLE = DateTimeFormatter.ofPattern("y/M/d");

    /**
     * formatter yyyy/MM/dd HH:mm:ss
     */
    public static final DateTimeFormatter DATE_TIME_SLASH;

    static {
        DATE_TIME_SLASH = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DATE_SLASH)
                .appendLiteral(" ")
                .append(DEFAULT_TIME)
                .toFormatter();
    }

    public static final DateTimeFormatter DATE_TIME_SLASH_SINGLE;

    static {
        DATE_TIME_SLASH_SINGLE = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DATE_SLASH_SINGLE)
                .appendLiteral(" ")
                .append(TIME_SINGLE)
                .toFormatter();
    }

    /**
     * formatter Hm
     */
    // public static final DateTimeFormatter TIME_NOT_DIVIDED_WHTH_H = DateTimeFormatter.ofPattern("Hm");
    public static final DateTimeFormatter TIME_NOT_DIVIDED_WHTH_Hm;

    static {
        TIME_NOT_DIVIDED_WHTH_Hm = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendValue(HOUR_OF_DAY, 1)
                .optionalStart()
                .appendValue(MINUTE_OF_HOUR, 1)
                .toFormatter();
    }

    /**
     * formatter Hm
     */
    public static final DateTimeFormatter TIME_NOT_DIVIDED_WITH_HHm = DateTimeFormatter.ofPattern("HHm");
    /**
     * formatter Hm
     */
    public static final DateTimeFormatter TIME_NOT_DIVIDED_WITH_Hmm = DateTimeFormatter.ofPattern("Hmm");
    /**
     * formatter Hm
     */
    public static final DateTimeFormatter TIME_NOT_DIVIDED_WITH_HHmm = DateTimeFormatter.ofPattern("HHmm");
}
