package com.example.demo.util.time;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Objects;

/**
 * @author administrator
 * @date 2020/07/24
 * @description: 类描述: 时间\日期处理工具类
 **/
public class DateUtil {

    private DateUtil() {
    }

    /**
     * 旧的时间类转化为字符串
     *
     * @param date    日期
     * @param pattern 格式
     * @return r
     */
    public static String getDateString(Date date, String pattern) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(pattern);
    }

    /**
     * 将日期和时间字符串转换为时间对象,必须指定日期
     *
     * @param dateTime 日期时间字符串
     * @return 时间对象
     */
    public static LocalDateTime getDateTimeByString(String dateTime) {
        dateTime = formatTime(dateTime);
        // yyyy-M-d'T'H:m:s
        if (dateTime.matches("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        // yyyy-M-d'T'H:m:s+Z
        if (dateTime.matches("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}\\+\\d{1,2}:\\d{1,2}$")) {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
        // yyyy-M-d H:m:s.S
        if (dateTime.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}$")) {
            return LocalDateTime.parse(dateTime, DateConst.DATE_TIME_SINGLE);
        }
        // yyyy-M-d H:m:s
        if (dateTime.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return LocalDateTime.parse(dateTime, DateConst.DATE_TIME_SINGLE);
        }
        // yyyy/M/d H:m:s
        if (dateTime.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return LocalDateTime.parse(dateTime, DateConst.DATE_TIME_SLASH_SINGLE);
        }
        // yyyy-M-d
        if (dateTime.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return LocalDate.parse(dateTime, DateConst.DATE_SINGLE).atStartOfDay();
        }
        // yyyy/M/d
        if (dateTime.matches("^\\d{4}/\\d{1,2}/\\d{1,2}$")) {
            return LocalDate.parse(dateTime, DateConst.DATE_SLASH_SINGLE).atStartOfDay();
        }
        // yyyy-MM
        if (dateTime.matches("^\\d{4}-\\d{1,2}$")) {
            dateTime = dateTime.concat("-01");
            return LocalDate.parse(dateTime, DateConst.DEFAULT_DATE).atStartOfDay();
        }
        throw new IllegalArgumentException("非法值:'" + dateTime + "'");
    }

    /**
     * 将日期和时间字符串转换为时间对象,必须指定日期
     *
     * @param date 日期对象
     * @param time 时间字符串
     * @return 时间对象
     */
    public static LocalDateTime getDateTimeByString(String date, String time) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        date = formatDate(date);
        LocalDate localDate = getDateByString(date);
        // 时间为空则为当日开始时间
        if (StringUtils.isBlank(time) && Objects.nonNull(localDate)) {
            return LocalDateTime.of(localDate, LocalTime.MIN);
        }
        time = formatTime(time);
        LocalTime localTime = getTimeByString(time);
        if (Objects.nonNull(localDate) && Objects.nonNull(localTime)) {
            return LocalDateTime.of(localDate, localTime);
        }
        return null;
    }

    /**
     * 从日期字符串获取日期对象
     *
     * @param date 日期
     * @return r
     */
    public static LocalDate getDateByString(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        date = formatDate(date);
        if (date.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return LocalDate.parse(date, DateConst.DATE_SINGLE);
        }
        if (date.matches("^\\d{4}/\\d{1,2}/\\d{1,2}$")) {
            return LocalDate.parse(date, DateConst.DATE_SLASH_SINGLE);
        }
        throw new IllegalArgumentException("非法值:'" + date + "'");
    }

    /**
     * 从时间字符串获取时间对象
     *
     * @param time 时间字符串
     * @return r
     */
    public static LocalTime getTimeByString(String time) {
        if (StringUtils.isBlank(time)) {
            return null;
        }
        time = formatTime(time);
        if (time.matches("^\\d{1,2}:\\d{1,2}:\\d{1,2}")) {
            return LocalTime.parse(time, DateConst.TIME_SINGLE);
        }
        if (time.matches("^\\d{1,2}:\\d{1,2}")) {
            return LocalTime.parse(time, DateConst.TIME_SINGLE);
        }
        if (time.matches("^\\d{1,2}\\d{1,2}")) {
            //2位数
            int length = time.length();
            if (Objects.equals(2, length)) {
                return LocalTime.parse(time, DateConst.TIME_NOT_DIVIDED_WHTH_Hm);
            }
            // 3位数
            if (Objects.equals(3, length)) {
                // 以1开头的数字前两位被视为小时 Hour
                if (time.startsWith("1")) {
                    return LocalTime.parse(time, DateConst.TIME_NOT_DIVIDED_WITH_HHm);
                } else if (time.startsWith("2")) {
                    String prefix = time.substring(0, 2);
                    int prefixInt = Integer.parseInt(prefix);
                    if (prefixInt <= 24) {
                        return LocalTime.parse(time, DateConst.TIME_NOT_DIVIDED_WITH_HHm);
                    } else {
                        return LocalTime.parse(time, DateConst.TIME_NOT_DIVIDED_WITH_Hmm);
                    }
                } else {
                    return LocalTime.parse(time, DateConst.TIME_NOT_DIVIDED_WITH_Hmm);
                }
            }
            // 4位数
            if (Objects.equals(4, length)) {
                return LocalTime.parse(time, DateConst.TIME_NOT_DIVIDED_WITH_HHmm);
            }
        }
        throw new IllegalArgumentException("非法值:'" + time + "'");
    }

    /**
     * 格式化时间
     *
     * @param time 时间字符串
     * @return r
     */
    private static String formatTime(String time) {
        time = time.trim();
        // 中文时间分隔符换成英文时间分隔符
        if (time.contains("：")) {
            time = time.replaceAll("：", ":");
        }
        return time;
    }

    /**
     * 格式化日期,不能用于格式化日期+时间
     *
     * @param date 时间字符串
     * @return r
     */
    private static String formatDate(String date) {
        if (Objects.nonNull(date)) {
            date = date.replaceAll("\\s*", "");
        }
        return date;
    }

    /**
     * 获取当前年的开始时间
     */
    public static LocalDateTime getFirstDayOfYear() {
        return LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
    }

    /**
     * 获取当前年的结束时间
     */
    public static LocalDateTime getLastDayOfYear() {
        return LocalDateTime.now().with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
    }

    /**
     * Date转LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * localDateTime 转 Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * localDateTime 转 String
     */
    public static String localDateTimeToString(DateTimeFormatter formatter, LocalDateTime localDateTime) {
        if (Objects.nonNull(localDateTime)) {
            return formatter.format(localDateTime);
        }
        return null;
    }

    /**
     * 获取两个时间之间的一段时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return r
     */
    public static Duration duration(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime);
    }

}
