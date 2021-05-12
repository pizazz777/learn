package com.example.demo.util.lang;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/05/12
 * @description
 */
public class MathUtil {

    private MathUtil() {
    }

    /**
     * 阶乘
     *
     * @param number 数
     * @return int
     */
    public static int factorial(int number) {
        if (number <= 1) {
            return 1;
        }
        return factorial(number - 1) * number;
    }

}
