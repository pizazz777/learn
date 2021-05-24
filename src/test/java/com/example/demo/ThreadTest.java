package com.example.demo;

import com.huang.util.thread.ThreadUtil;

import java.util.concurrent.*;

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
            // 提交任务给线程执行
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
        ThreadUtil.exit(pool, 3000L, TimeUnit.MILLISECONDS);
    }


}
