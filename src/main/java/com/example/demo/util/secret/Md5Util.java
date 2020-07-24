package com.example.demo.util.secret;

import com.google.common.base.Charsets;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;

/**
 * @author administrator
 * @date 2020/07/24
 * @description: 类描述: md5工具类
 **/
public class Md5Util {

    private Md5Util() {
    }

    /**
     * md5 名称
     */
    private static final String MD5_NAME = "MD5";

    /**
     * 16进制字符数组
     */
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    /**
     * 获取字符串的 MD5码
     *
     * @param s 字符串
     * @return MD5码
     * @see Md5Util#getMd5(String)
     */
    @Deprecated
    public static String getMd5Old(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance(MD5_NAME);
            byte[] bytes = digest.digest(s.getBytes(Charsets.UTF_8));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取字符串的 MD5码
     *
     * @param s 字符串
     * @return MD5码
     */
    public static String getMd5(String s) {
        return DigestUtils.md5Hex(s);
    }

    public static void main(String[] args) {
        String md5 = getMd5("123456");
        System.out.println(md5);
    }

    /**
     * MD5工具-转16位
     *
     * @param bytes 字节数组
     * @return 16位字符串
     */
    private static String toHex(byte[] bytes) {
        StringBuilder string = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            string.append(HEX_DIGITS[(b >> 4) & 0x0f]);
            string.append(HEX_DIGITS[b & 0x0f]);
        }
        return string.toString();
    }
}
