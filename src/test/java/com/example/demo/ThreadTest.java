package com.example.demo;

import com.example.demo.util.thread.ThreadUtil;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2020/09/17
 * @description
 */
public class ThreadTest {


    public static void main(String[] args) {

        ExecutorService pool = ThreadUtil.customThreadPool();

        ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(pool);

        for (int index = 0; index < 20; index++) {
            completionService.submit(new ThreadTask("任务" + (index + 1) + "号"));
        }

        for (int index = 0; index < 20; index++) {
            try {
                Future<String> take = completionService.take();
                String result = take.get();
                System.out.println("结果: " + result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("任务结束了吗");
        ThreadUtil.exit(pool);
    }


}
