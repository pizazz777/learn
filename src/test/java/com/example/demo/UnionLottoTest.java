package com.example.demo;

import com.example.demo.util.lang.MathUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/05/12
 * @description 双色球机选  N红+N蓝,可以选复式选号
 */
public class UnionLottoTest {

    // 红球个数  默认6个,超过是复式
    private static final int RED_NUMBER = 6;
    // 蓝球个数  默认1个,超过是复式
    private static final int BLUE_NUMBER = 1;
    // 几注 默认1注
    private static final int NUMBERS = 5;
    // 倍数 默认1倍
    private static final int MULTIPLE = 5;

    @Test
    public void generateTest() {

        for (int index = 0; index < NUMBERS; index++) {

            List<String> redBallList = lottery("red", RED_NUMBER);
            List<String> blueBallList = lottery("blue", BLUE_NUMBER);

            // 打印
            System.out.print(getSeq(index + 1) + (redBallList.toString() + " * " + blueBallList.toString())
                    .replaceAll("\\[", "").replaceAll("]", "")
                    + " [" + MULTIPLE + "倍" + "]");
            System.out.println();

        }

        int money = 2 * BLUE_NUMBER * MULTIPLE * NUMBERS
                // 双色球复式计算公式 R!/(R-6)!/6!*B   R->红球个数,33>=R>=6; B->蓝球个数16>=B>=1
                * (MathUtil.factorial(RED_NUMBER) / MathUtil.factorial(RED_NUMBER - 6) / MathUtil.factorial(6));
        System.out.println("金额:  " + money + "元");

    }


    /**
     * 机选号码算法
     *
     * @param type   red->红球,blue->蓝球
     * @param number 生成次数
     * @return list
     */
    private List<String> lottery(String type, int number) {
        // 球色和对应概率
        List<Ball> ballList = getBallList(type);
        // 总概率
        int totalProbability = ballList.stream().mapToInt(Ball::getProbability).sum();
        // 通过概率生成球号列表
        List<Integer> ballNumberList = Lists.newArrayList();
        for (Ball ball : ballList) {
            for (int i = 0; i < ball.getProbability(); i++) {
                ballNumberList.add(ball.getName());
            }
        }
        // 打乱顺序
        Collections.shuffle(ballNumberList);
        // 生成球号
        List<Integer> retBallList = Lists.newArrayList();
        int size = 0;
        while (size < number) {
            // 0.0-1.0的伪随机数
            double nextDouble = new Random().nextDouble();
            // 计算位置,注意边界
            int index = new BigDecimal(nextDouble).multiply(new BigDecimal(totalProbability)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue() - 1;
            index = index < 0 ? 0 : index;
            Integer ballNumber = ballNumberList.get(index);
            // 不能添加重复的
            if (!retBallList.contains(ballNumber)) {
                retBallList.add(ballNumber);
                size++;
            }
        }
        // 为了美观,个位数在前面补0
        return retBallList.stream().sorted().map(item -> StringUtils.leftPad(String.valueOf(item), 2, "0")).collect(Collectors.toList());
    }


    private String getSeq(int index) {
        switch (index) {
            case 1:
                return "① ";
            case 2:
                return "② ";
            case 3:
                return "③ ";
            case 4:
                return "④ ";
            case 5:
                return "⑤ ";
            default:
                return " ";
        }
    }

    /**
     * 获取球色和对应概率
     *
     * @param type red->红球,blue->蓝球
     * @return list
     */
    private List<Ball> getBallList(String type) {
        List<Ball> ballList = Lists.newArrayList();
        // 红球
        if (Objects.equals(type, "red")) {
            ballList.add(new Ball(1, 20));
            ballList.add(new Ball(2, 40));
            ballList.add(new Ball(3, 25));
            ballList.add(new Ball(4, 25));
            ballList.add(new Ball(5, 25));
            ballList.add(new Ball(6, 35));
            ballList.add(new Ball(7, 40));
            ballList.add(new Ball(8, 20));
            ballList.add(new Ball(9, 15));
            ballList.add(new Ball(10, 15));
            ballList.add(new Ball(11, 30));
            ballList.add(new Ball(12, 15));
            ballList.add(new Ball(13, 20));
            ballList.add(new Ball(14, 10));
            ballList.add(new Ball(15, 10));
            ballList.add(new Ball(16, 10));
            ballList.add(new Ball(17, 30));
            ballList.add(new Ball(18, 25));
            ballList.add(new Ball(19, 30));
            ballList.add(new Ball(20, 25));
            ballList.add(new Ball(21, 25));
            ballList.add(new Ball(22, 10));
            ballList.add(new Ball(23, 2));
            ballList.add(new Ball(24, 35));
            ballList.add(new Ball(25, 2));
            ballList.add(new Ball(26, 10));
            ballList.add(new Ball(27, 15));
            ballList.add(new Ball(28, 35));
            ballList.add(new Ball(29, 10));
            ballList.add(new Ball(30, 10));
            ballList.add(new Ball(31, 80));
            ballList.add(new Ball(32, 15));
            ballList.add(new Ball(33, 10));
        }
        // 蓝球
        if (Objects.equals(type, "blue")) {
            ballList.add(new Ball(1, 5));
            ballList.add(new Ball(2, 10));
            ballList.add(new Ball(3, 30));
            ballList.add(new Ball(4, 40));
            ballList.add(new Ball(5, 35));
            ballList.add(new Ball(6, 20));
            ballList.add(new Ball(7, 40));
            ballList.add(new Ball(8, 5));
            ballList.add(new Ball(9, 30));
            ballList.add(new Ball(10, 60));
            ballList.add(new Ball(11, 30));
            ballList.add(new Ball(12, 30));
            ballList.add(new Ball(13, 20));
            ballList.add(new Ball(14, 30));
            ballList.add(new Ball(15, 5));
            ballList.add(new Ball(16, 35));
        }
        return ballList;
    }

    @Data
    @AllArgsConstructor
    static class Ball {
        // 名称
        private Integer name;
        // 概率 数字越大概率越大
        public Integer probability;
    }

}
