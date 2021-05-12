package com.example.demo;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/04/08
 * @description java8 Predicate函数
 */
public class PredicateTest {

    public static void main(String[] args) {

        // test: 等于(==)
        Predicate<Integer> predicate = value -> value % 2 == 0;
        System.out.println("test === " + predicate.test(7)); // false
        System.out.println("test === " + predicate.test(12));    // true

        // and: 逻辑与(&&)
        System.out.println("and === " + and("pizazz",
                value -> Objects.equals(value, "pizazz"),
                value -> value.length() > 7,
                value -> {
                    System.out.println("and === " + "有没有短路1");  // 没有执行
                    return value.contains("pi");
                }
        )); // false
        System.out.println("and === " + and("Spring Cloud",
                value -> Objects.equals(value, "Spring Cloud"),
                value -> value.length() > 7,
                value -> {
                    System.out.println("and === " + "有没有短路2");  // 执行了
                    return value.contains("xxx");
                }
        )); // false

        // negate: 逻辑非(!)
        System.out.println("negate === " + negate("pizazz", value -> value.length() == 6));     // false
        System.out.println("negate === " + negate("Spring", value -> value.contains("java")));  // true

        // or: 逻辑或(||)
        System.out.println("or === " + or("pizazz", value -> value.length() == 6, value -> value.startsWith("za")));    // true
        System.out.println("or === " + or("Spring", value -> value.contains("java"), value -> value.endsWith("ing")));  // true

        // equals: 比较
        System.out.println("equals === " + equals("pizazz", "test"));    // false
        System.out.println("equals === " + equals("Spring", "Spring"));  // true

    }

    private static boolean and(String value, Predicate<String> predicate1, Predicate<String> predicate2, Predicate<String> predicate3) {
        return predicate1.and(predicate2).and(predicate3).test(value);
    }

    private static boolean negate(String value, Predicate<String> predicate) {
        return predicate.negate().test(value);
    }

    private static boolean or(String value, Predicate<String> predicate1, Predicate<String> predicate2) {
        return predicate1.or(predicate2).test(value);
    }

    private static boolean equals(String value1, String value2) {
        return Predicate.isEqual(value1).test(value2);
    }


}
