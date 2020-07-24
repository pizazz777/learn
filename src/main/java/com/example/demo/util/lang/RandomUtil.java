package com.example.demo.util.lang;

import java.util.Random;

/**
 * @author Administrator
 * @date 2020-07-24 18:14
 * @description: 随机码工具类
 */
public class RandomUtil {

    private RandomUtil() {
    }

    /**
     * 数字
     */
    private static final String NUMBER_SOURCE = "0123456789";
    /**
     * 验证码
     */
    private static final String VERIFY_CODE_SOURCE = NUMBER_SOURCE + "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 密码
     */
    private static final String PASSWORD_SOURCE = VERIFY_CODE_SOURCE + "!@#$%^&*()_+";

    /**
     * 获取 N 位随机数
     *
     * @param n 个数
     * @return 随机字符串
     */
    public static String getRandomNumber(int n) {
        return getBySource(n, NUMBER_SOURCE);
    }

    /**
     * 获取 N 位验证码
     *
     * @param n 个数
     * @return 随机字符串
     */
    public static String getVerifyCode(int n) {
        return getBySource(n, VERIFY_CODE_SOURCE);
    }

    /**
     * 获取 N 位密码
     *
     * @param n 个数
     * @return 随机字符串
     */
    public static String getPassword(int n) {
        return getBySource(n, PASSWORD_SOURCE);
    }

    /**
     * 从数据源中随机抽取n个字符作为随机字符串
     *
     * @param n      个数
     * @param source 源字符串
     * @return 随机字符串
     */
    private static String getBySource(int n, String source) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        int length = source.length();
        for (int i = 0; i < n; i++) {
            result.append(source.charAt(random.nextInt(length)));
        }
        return result.toString();
    }

}
