package com.example.demo;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2020/09/17
 * @description
 */
public class ThreadTask implements Callable<String> {

    private String taskName;

    public ThreadTask(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public String call() throws Exception {
//        System.out.println("执行任务: " + taskName);
        // 睡1~3秒
        int nextInt = new Random().nextInt(2000) + 1000;
        Thread.sleep(nextInt);
        System.out.println(Thread.currentThread().getName() + "  执行的  " + taskName);
        return taskName;
    }


}
