package com.example.demo.util.str;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author administrator
 * @date 2020/07/16
 * @description: 类描述: 字符串工具类
 **/
public class StrUtil {

    private StrUtil() {
    }

    /**
     * 以数字开头
     */
    public static final Pattern START_WITH_NUMBER = Pattern.compile("[0-9]*");
    /**
     * 下划线接一个字母或数字
     */
    public static final Pattern UNDERLINE_WITH_CHAR = Pattern.compile("(_[A-Za-z0-9])");

    /**
     * 手机号正则
     */
    public static final Pattern MOBILE = Pattern.compile("^1[345789]\\d{9}$");

    /**
     * 根据限制个数数,每到限制位数添加一个空格(例如 999999999 999,999,999)
     *
     * @param original 原字符串
     * @param limit    限定长度
     * @return string
     */
    public static String insertBlankByLimit(@NonNull String original, int limit) {
        return insertByLimit(original, StringUtils.SPACE, limit);
    }

    /**
     * 根据限制个数数,每到限制位数添加一个指定的插入字符串
     *
     * @param original 原始字符串
     * @param insert   插入字符串
     * @param limit    每段字符个数
     * @return string
     */
    private static String insertByLimit(@NonNull String original, String insert, int limit) {
        int length = original.length();
        if (length <= limit) {
            return original;
        }
        int count = (int) (Math.ceil(1.0 * length / limit) - 1);
        int insertLength = insert.length();
        StringBuilder stringBuilder = new StringBuilder(original);
        for (int i = 0; i < count; i++) {
            // 每次插入的偏移量
            int offset = limit * (i + 1) + insertLength * i;
            stringBuilder.insert(offset, insert);
        }
        return stringBuilder.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param underLineString 下换线字符串
     * @return r
     */
    public static String convertUnderLineToCamelCase(@NonNull String underLineString) {
        StringBuilder resultBuilder = new StringBuilder(underLineString);
        String result = underLineString;
        Matcher matcher = UNDERLINE_WITH_CHAR.matcher(resultBuilder);
        while (matcher.find()) {
            String temp = matcher.group(0);
            result = result.replace(temp, temp.replaceAll("_", "").toUpperCase());
        }
        return result;
    }

    /**
     * 下划线转驼峰,首字母大写
     *
     * @param underLineString 下换线字符串
     * @return r
     */
    public static String convertUnderLineToFirstUpperCamelCase(@NonNull String underLineString) {
        String result = convertUnderLineToCamelCase(underLineString);
        return result.substring(0, 1).toUpperCase() + result.substring(1);
    }

    /**
     * 下划线转驼峰,首字母小写
     *
     * @param underLineString 下换线字符串
     * @return r
     */
    public static String convertUnderLineToFirstLowCamelCase(@NonNull String underLineString) {
        String result = convertUnderLineToCamelCase(underLineString);
        return result.substring(0, 1).toLowerCase() + result.substring(1);
    }

    /**
     * 判断字符串是不是以数字开头
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isStartWithNumber(@NonNull String str) {
        return START_WITH_NUMBER.matcher(str.charAt(0) + "").matches();
    }

    /**
     * 判断字符串是否是手机号
     *
     * @param mobile 手机号码
     * @return boolean
     */
    public static boolean isMobile(@NonNull String mobile) {
        return MOBILE.matcher(mobile).matches();
    }
}
