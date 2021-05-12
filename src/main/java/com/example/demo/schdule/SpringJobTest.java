package com.example.demo.schdule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/03/25
 * @description
 */
@Component
public class SpringJobTest {


    // 每天23:59:59执行
    // private static final String CRON = "59 59 23 * * ?";
    // 每30minus执行一次
    private static final String CRON = "* 0/30 * * * ? ";

    @Scheduled(cron = CRON)
    public void execute() {
        System.out.println("Spring自带的定时任务!");
    }

}
